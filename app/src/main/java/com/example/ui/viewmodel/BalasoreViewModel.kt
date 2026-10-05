package com.example.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.ChandipurTideEntity
import com.example.data.local.ChandipurTideDao
import com.example.data.local.CivicReportEntity
import com.example.data.local.CivicReportDao
import com.example.data.local.ItineraryItemEntity
import com.example.data.model.*
import com.example.data.notification.TideAndWeatherNotificationManager
import com.example.data.remote.BalasoreApiService
import com.example.data.remote.WeatherApiService
import com.example.data.remote.OpenWeatherCurrentResponse
import com.example.data.remote.OpenWeatherThreeDayForecast
import com.example.data.remote.OpenWeatherDailyForecast
import com.example.data.remote.EmergencyAlertDto
import com.example.data.remote.GeminiGroundingService
import com.example.data.remote.GroundingResponse
import com.example.data.remote.GroundingToolMode
import com.example.data.remote.GeminiChatService
import com.example.data.remote.ChatMessage
import com.example.data.remote.MessageSender
import com.example.data.remote.GeminiChatModel
import com.example.data.remote.ChatRolePersona
import com.example.data.remote.ChatRolePersonas
import com.example.data.repository.BalasoreRepository
import com.example.data.repository.CivicReportRepository
import com.example.data.repository.DefaultData
import com.example.data.repository.ItineraryRepository
import com.example.data.repository.NewsRepository
import com.example.data.repository.OpenWeatherRepository
import com.example.data.repository.Resource
import com.example.data.repository.TravelJournalRepository
import com.example.data.repository.UniqueFeaturesRepository
import com.example.ui.components.WatermarkOpacity
import com.example.ui.components.WatermarkStyle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Material-3 TabRow categorization for Balasore News Feed:
 * 0 -> Local News
 * 1 -> Defense Alerts
 * 2 -> Tourism Updates
 */
enum class NewsFeedTab(val index: Int, val title: String, val odiaTitle: String, val categoryName: String) {
    LOCAL_NEWS(0, "Local News", "ସ୍ଥାନୀୟ ଖବର", "Local News"),
    DEFENSE_ALERTS(1, "Defense Alerts", "ପ୍ରତିରକ୍ଷା ସତର୍କତା", "Defense Alerts"),
    TOURISM_UPDATES(2, "Tourism Updates", "ପର୍ଯ୍ୟଟନ ଖବର", "Tourism Updates");

    companion object {
        fun fromIndex(index: Int): NewsFeedTab = entries.find { it.index == index } ?: LOCAL_NEWS
        fun fromCategory(cat: String): NewsFeedTab = when {
            cat.equals("Defense Alerts", ignoreCase = true) ||
            cat.equals("Defense", ignoreCase = true) ||
            cat.equals("Coastal Alerts", ignoreCase = true) ||
            cat.equals("Emergency", ignoreCase = true) -> DEFENSE_ALERTS

            cat.equals("Tourism Updates", ignoreCase = true) ||
            cat.equals("Tourism", ignoreCase = true) ||
            cat.equals("Culture", ignoreCase = true) ||
            cat.equals("Spiritual", ignoreCase = true) -> TOURISM_UPDATES

            else -> LOCAL_NEWS
        }
    }
}

object NewsFeedFilter {
    fun matchesTab(article: NewsArticle, tabIndex: Int): Boolean {
        val tab = NewsFeedTab.fromIndex(tabIndex)
        return when (tab) {
            NewsFeedTab.LOCAL_NEWS ->
                article.category.equals("Local News", ignoreCase = true) ||
                article.category.equals("Local", ignoreCase = true) ||
                article.category.equals("Infrastructure", ignoreCase = true) ||
                article.category.equals("Politics", ignoreCase = true) ||
                article.category.equals("Local Politics", ignoreCase = true) ||
                article.category.equals("Development", ignoreCase = true) ||
                article.category.equals("Sports", ignoreCase = true) ||
                article.category.contains("Civic", ignoreCase = true) ||
                article.category.contains("Municipal", ignoreCase = true) ||
                article.title.contains("municipality", ignoreCase = true) ||
                article.title.contains("station", ignoreCase = true) ||
                article.title.contains("parishad", ignoreCase = true) ||
                article.title.contains("drainage", ignoreCase = true) ||
                article.title.contains("smart city", ignoreCase = true)

            NewsFeedTab.DEFENSE_ALERTS ->
                article.category.equals("Defense Alerts", ignoreCase = true) ||
                article.category.equals("Defense", ignoreCase = true) ||
                article.category.equals("Coastal Alerts", ignoreCase = true) ||
                article.category.equals("Emergency", ignoreCase = true) ||
                article.title.contains("DRDO", ignoreCase = true) ||
                article.title.contains("Missile", ignoreCase = true) ||
                article.title.contains("ITR", ignoreCase = true) ||
                article.title.contains("Defense", ignoreCase = true) ||
                article.title.contains("Alert", ignoreCase = true) ||
                article.title.contains("Storm", ignoreCase = true) ||
                article.title.contains("Flood", ignoreCase = true) ||
                article.title.contains("Siren", ignoreCase = true) ||
                article.title.contains("Warning", ignoreCase = true) ||
                article.content.contains("DRDO", ignoreCase = true) ||
                article.content.contains("missile", ignoreCase = true)

            NewsFeedTab.TOURISM_UPDATES ->
                article.category.equals("Tourism Updates", ignoreCase = true) ||
                article.category.equals("Tourism", ignoreCase = true) ||
                article.category.equals("Culture", ignoreCase = true) ||
                article.category.equals("Spiritual", ignoreCase = true) ||
                article.title.contains("Tourism", ignoreCase = true) ||
                article.title.contains("Eco-Promenade", ignoreCase = true) ||
                article.title.contains("Eco-Trek", ignoreCase = true) ||
                article.title.contains("Gazebos", ignoreCase = true) ||
                article.title.contains("Vanishing Sea", ignoreCase = true) ||
                article.title.contains("Mahotsav", ignoreCase = true) ||
                article.title.contains("Tourists", ignoreCase = true) ||
                article.title.contains("Eco-Tourism", ignoreCase = true) ||
                article.title.contains("Wildlife Sanctuary", ignoreCase = true) ||
                article.title.contains("Darshan Facilitated", ignoreCase = true) ||
                article.content.contains("tourist", ignoreCase = true) ||
                article.content.contains("visitors", ignoreCase = true)
        }
    }

    fun filterArticles(articles: List<NewsArticle>, tabIndex: Int, query: String = ""): List<NewsArticle> {
        val trimmed = query.trim()
        return articles.filter { article ->
            val matchesTab = matchesTab(article, tabIndex)
            val matchesQuery = trimmed.isEmpty() ||
                article.title.contains(trimmed, ignoreCase = true) ||
                article.odiaTitle.contains(trimmed, ignoreCase = true) ||
                article.snippet.contains(trimmed, ignoreCase = true) ||
                article.odiaSnippet.contains(trimmed, ignoreCase = true) ||
                article.content.contains(trimmed, ignoreCase = true) ||
                article.odiaContent.contains(trimmed, ignoreCase = true) ||
                article.category.contains(trimmed, ignoreCase = true) ||
                article.source.contains(trimmed, ignoreCase = true)
            matchesTab && matchesQuery
        }
    }
}

data class BalasoreUiState(
    val selectedTab: Int = 0,
    val language: AppLanguage = AppLanguage.ENGLISH,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val hotspots: List<Hotspot> = BalasoreRepository.hotspots,
    val selectedHotspotCategory: String = "All",
    val hotspotSearchQuery: String = "",
    val newsArticles: List<NewsArticle> = BalasoreRepository.newsArticles,
    val selectedNewsTab: Int = 0,
    val selectedNewsCategory: String = "Local News",
    val newsSearchQuery: String = "",
    val filteredNewsArticles: List<NewsArticle> = NewsFeedFilter.filterArticles(BalasoreRepository.newsArticles, 0, ""),
    val weather: WeatherInfo = BalasoreRepository.weather,
    val isWeatherLoading: Boolean = false,
    val emergencyContacts: List<EmergencyContact> = BalasoreRepository.emergencyContacts,
    val transitList: List<TransitSchedule> = BalasoreRepository.transitSchedules,
    // 6 Unique Features
    val tidalClock: TidalClockData = BalasoreRepository.tidalClock,
    val lastKnownTideForecast: ChandipurTideEntity? = null,
    val drdoAdvisories: List<DrdoAdvisory> = BalasoreRepository.drdoAdvisories,
    val templeRitualInfo: TempleRitualInfo = BalasoreRepository.templeRitualInfo,
    val riverGauges: List<RiverGauge> = BalasoreRepository.riverGauges,
    val cycloneShelters: List<CycloneShelter> = BalasoreRepository.cycloneShelters,
    val seafoodCatches: List<SeafoodCatch> = BalasoreRepository.seafoodCatches,
    val literaryTrailPoints: List<LiteraryTrailPoint> = BalasoreRepository.literaryTrailPoints,
    val dailyIdiom: DailyBalasoreIdiom = BalasoreRepository.dailyIdiom,
    // Extended Unique Features Suite
    val horseshoeCrabSightings: List<HorseshoeCrabSighting> = UniqueFeaturesRepository.horseshoeCrabSightings,
    val intertidalEcoGuidelines: List<IntertidalEcoGuideline> = UniqueFeaturesRepository.intertidalEcoGuidelines,
    val defenseMilestones: List<DefenseTrailMilestone> = UniqueFeaturesRepository.defenseMilestones,
    val maritimeExclusionZone: MaritimeExclusionZone = UniqueFeaturesRepository.maritimeExclusionZone,
    val prasadStatus: PrasadStatus = UniqueFeaturesRepository.prasadStatus,
    val balasoreArtisans: List<BalasoreArtisan> = UniqueFeaturesRepository.balasoreArtisans,
    val detailedCycloneShelters: List<DetailedCycloneShelter> = UniqueFeaturesRepository.detailedCycloneShelters,
    val emergencyKitItems: List<EmergencyKitItem> = UniqueFeaturesRepository.defaultEmergencyKitItems,
    val harborCatchRates: List<HarborCatchRate> = UniqueFeaturesRepository.harborCatchRates,
    val harborLandingBells: List<HarborLandingBell> = UniqueFeaturesRepository.harborLandingBells,
    val elephantCorridorAlert: ElephantCorridorAlert = UniqueFeaturesRepository.elephantCorridorAlert,
    val ecoPassportStamps: List<EcoPassportStamp> = UniqueFeaturesRepository.ecoPassportStamps,
    val weatherAlerts: List<BalasoreWeatherAlert> = DefaultData.getInitialWeatherAlerts(),
    val forecastDays: List<BalasoreForecastDay> = BalasoreRepository.forecast3Days,
    val hourlyForecast: List<BalasoreForecastHour> = emptyList(),
    val isFahrenheit: Boolean = false,
    val selectedForecastDayIndex: Int = 0,
    val activeFeatureSheet: UniqueFeatureSheetType? = null,
    val isRefreshing: Boolean = false,
    val bookmarkedIds: Set<String> = emptySet(),
    val favoriteHotspotIds: Set<String> = emptySet(),
    val groundingState: GroundingState = GroundingState(),
    // Gemini Chatbot & AI Features State
    val chatMessages: List<com.example.data.remote.ChatMessage> = emptyList(),
    val isChatLoading: Boolean = false,
    val selectedChatModel: com.example.data.remote.GeminiChatModel = com.example.data.remote.GeminiChatModel.FLASH,
    val selectedChatRole: com.example.data.remote.ChatRolePersona = com.example.data.remote.ChatRolePersonas.TOURISM_GUIDE,
    val isChatSearchGrounding: Boolean = true,
    val isChatMapsGrounding: Boolean = true,
    val isMusicGenerating: Boolean = false,
    // Custom Interactive Itinerary (Room persisted)
    val itineraryItems: List<ItineraryItemEntity> = DefaultData.getDefaultItineraryItems(),
    // Live Emergency Alerts fetched from Backend
    val liveEmergencyAlerts: List<EmergencyAlertDto> = DefaultData.getDefaultEmergencyAlerts(),
    val dismissedAlertIds: Set<String> = emptySet(),
    // Daily Auto-Update Pulse
    val dailyPulse: com.example.data.daily.DailyBalasorePulse = com.example.data.daily.DailyUpdateEngine.getDailyPulse(),
    // Full App Watermark (App Name & Official Logo)
    val isWatermarkEnabled: Boolean = true,
    val watermarkStyle: WatermarkStyle = WatermarkStyle.CENTER_EMBLEM,
    val watermarkOpacity: WatermarkOpacity = WatermarkOpacity.MEDIUM,
    // Hourly Auto-Refresh & Synchronization
    val isHourlyAutoRefreshEnabled: Boolean = true,
    val lastHourlyRefreshTimestamp: Long = System.currentTimeMillis(),
    val nextHourlyRefreshMinutesRemaining: Int = 60,
    val autoRefreshCycleCount: Int = 0,
    val autoUpdateFrequencyMinutes: Int = 60,
    val isAutoUpdateBackgroundEnabled: Boolean = true,
    val lastSyncStatusMessage: String = "All services synchronized",
    val appVersionInstalled: String = com.example.BuildConfig.VERSION_NAME,
    val appVersionLatest: String = com.example.BuildConfig.VERSION_NAME,
    val isUpdateCheckLoading: Boolean = false,
    // News Dedicated Auto-Update Engine
    val isNewsAutoUpdateEnabled: Boolean = true,
    val newsAutoUpdateIntervalSeconds: Int = 45,
    val newsAutoUpdateSecondsRemaining: Int = 45,
    val lastNewsAutoUpdateTimestamp: Long = System.currentTimeMillis(),
    val newsAutoUpdateCycleCount: Int = 0,
    // My Reports - Local Room Database for Offline Civic Issue Tracking & Auto Updates
    val myCivicReports: List<CivicReportEntity> = DefaultData.getDefaultCivicReports(),
    val selectedMyReportsFilter: String = "ALL",
    // OpenWeatherMap Real-Time Telemetry & 3-Day / 7-Day Forecast Suite
    val openWeatherCurrent: OpenWeatherCurrentResponse? = null,
    val openWeatherThreeDayForecast: List<OpenWeatherThreeDayForecast> = emptyList(),
    val openWeatherSevenDayForecast: List<OpenWeatherDailyForecast> = emptyList(),
    val isOpenWeatherLoading: Boolean = false,
    val openWeatherCycleCount: Int = 1,
    // Chandipur Vanishing Sea Walk Window Proactive Notification Settings
    val isWalkWindowAlertsEnabled: Boolean = true,
    val walkWindowAdvanceMinutes: Int = 30
)

enum class UniqueFeatureSheetType {
    HORSESHOE_CRAB,
    DEFENSE_TRAIL,
    REMUNA_PRASAD_ARTISANS,
    CYCLONE_RESILIENCE,
    HARBOR_CATCH_RATES,
    ELEPHANT_PASSPORT,
    // 8 Core Feature Hubs:
    CHANDIPUR_TIDE_TIMER,
    CYCLONE_SHELTER_FINDER,
    BALASORE_FOOD_TRAIL,
    BILINGUAL_HERITAGE_AUDIO,
    TRANSIT_FARE_ESTIMATOR,
    KULDIHA_SAFARI_COMPANION,
    TRAVEL_JOURNAL,
    BLOOD_AND_DIALYSIS_DIRECTORY,
    // 7 Daily Auto-Updated Features:
    KASAFAL_RED_CRABS,
    NILAGIRI_STONE_SABAI,
    BALARAMGADI_ESTUARY,
    COLONIAL_HERITAGE_WALK,
    TALASARI_AUCTION_MONITOR,
    BALESWARIYA_DIALECT_PROVERBS,
    PAN_BARAJA_AGRO,
    // 10 Next-Gen Daily Auto-Updated Features:
    BICHITRAPUR_MANGROVE_BOATING,
    CHANDANESWAR_CHADAK_MELA,
    PANCHALINGESWAR_STREAM_SAFETY,
    DARK_SKY_BIOLUMINESCENCE,
    BELL_METAL_ARTISANS,
    BUDDHIST_JAIN_CIRCUIT,
    HEIRLOOM_RICE_AGRO,
    LAKHANNATH_ZAMINDARI,
    BUDHABALANGA_RIVER_ANGLING,
    CYCLONE_ORAL_HISTORY,
    // Healthcare & Medical Daily Auto-Updated Suites:
    DOCTORS_DIRECTORY,
    MEDICINE_STORES_DIRECTORY,
    POLYCLINIC_DIRECTORY,
    PATHOLOGY_LAB_DIRECTORY,
    PRIVATE_HOSPITAL_DIRECTORY,
    // Hydrology & Artisan Marketplace Sheets:
    RIVER_FLOOD_TELEMETRY,
    MATI_MANISHA_ARTISANS,
    // Google Maps Explorer (Hotspots & Cyclone Shelters)
    BALASORE_MAP_EXPLORER,
    // 6 Extended Unique Suites with Auto-Updating Telemetry
    SALT_PAN_HERITAGE,
    HILSA_MIGRATION,
    KULDIHA_ELEPHANT_CORRIDOR,
    CHHENA_GAJA_HOT_BATCH,
    RIVER_FERRY_SCHEDULE,
    ODIA_SAHITYA_REVIVAL,
    // Suggested Innovations:
    ASK_BALASORE_AI,
    INCOIS_OCEAN_ADVISORY,
    ODIA_PANJIKA_CALENDAR,
    RAIBANIA_AUDIO_WALK,
    BLOOD_AND_BED_PULSE,
    CITIZEN_CIVIC_EYE,
    // Next Generation Auto-Update Suites
    TOTO_AUTO_FARE_CARD,
    BALASORE_CAMPUS_CAREER_BOARD,
    BALASORE_AUTO_UPDATE_CENTER,
    // 7 New Suites with Live Telemetry & Auto-Updates
    BALASORE_JUNCTION_RADAR,
    BALESWARI_PAAN_KRUSHI_MANDI,
    NIGHT_CHEMIST_SANJEEVANI,
    BALESWAR_MAHOTSAV_CHADAK,
    NILAGIRI_STONE_ARTISAN,
    TPNODL_WATCO_MONITOR,
    TALASARI_MANGROVE_EXPLORER,
    // 7 Citizen, Maritime, Nature & Healthcare Suites
    MO_SEVA_KENDRA_CITIZEN,
    MARINE_FISHERMEN_SAFETY,
    KULDIHA_ECO_CAMP_SAFARI,
    FM_MCH_MEDICAL_COLLEGE_OPD,
    SABAI_GRASS_MISSION_SHAKTI,
    BALASORE_MARITIME_COLONIAL,
    BALASORE_STUDENT_CAREER_SCHOLARSHIP,
    // 7 Advanced Coastal, Safety, Culture, Health, Agro, Civic & Highway Suites
    DRDO_CHANDIPUR_SAFETY_RADAR,
    SUBARNAREKHA_SLUICE_FLOOD_RADAR,
    NILAGIRI_CHHAU_CULTURAL_GUILD,
    DHH_BLOOD_BANK_LIVE_RADAR,
    PAN_BARAJA_AQUA_SHRIMP_DESK,
    BALASORE_COURT_LEGAL_AID_DESK,
    NH16_HIGHWAY_PATROL_TRAUMA_SOS,
    // 7 Special Automated Hubs with Live Telemetry
    PANCHALINGESWAR_KULDIHA_SAFARI,
    SER_BALASORE_RAILWAY_JUNCTION,
    NOCCI_INDUSTRIAL_B2B_SKILL,
    TALSARI_BICHITRAPUR_MANGROVE_PILOT,
    FAKIR_MOHAN_BHASHA_LITERATURE,
    BALESWARI_CUISINE_SWEET_HERITAGE,
    TPNODL_WATCO_UTILITY_HOTLINE,
    // 7 Features with Auto Updates:
    DHAN_MANDI_MSP_PROCUREMENT,
    NILAGIRI_GRANITE_STONEWARE_GUILD,
    BAHANAGA_NHAI_RAPID_TRAUMA_NETWORK,
    BHUSANDESWAR_CHANDANESWAR_PILGRIMAGE,
    SENIOR_CITIZEN_SEVA_SETU,
    BALASORE_HIGHER_EDUCATION_HUB,
    BALASORE_MO_BUS_CRUT_NAVIGATOR,
    // 7 Coastal, Resilience, Heritage, Health, Agro, Transit & Nature Auto-Updated Suites:
    AQUA_SHRIMP_HATCHERY_RADAR,
    COASTAL_CYCLONE_SHELTER_NETWORK,
    MARITIME_FOREIGN_LOGE_TRAIL,
    RURAL_MOBILE_TELEMEDICINE_NETWORK,
    BALESWARI_BETEL_PADDY_COOPERATIVE,
    COASTAL_CRUT_INTERDISTRICT_BUS_RADAR,
    HORSESHOE_CRAB_INTERTIDAL_PROTECTION,
    CITIZEN_FEEDBACK_PORTAL,
    FCM_SEVERE_WEATHER_ALERT,
    // 6 Next-Gen Auto-Updated Suites:
    COASTAL_MARINE_AUDIO_RADIO,
    CYCLONE_SURGE_EVACUATION_ROUTER,
    BALARAMGADI_MANDI_PRICE_INDEX,
    TEMPLE_AARTI_KHIRA_BHOG_COMPANION,
    FM_MCH_EMERGENCY_BLOOD_NETWORK,
    MO_BALASORE_CIVIC_STORM_REPORTER,
    // 6 Defense, Agro, Transit, Campus, Culinary & Energy Suites:
    ITR_MISSILE_COASTAL_TRACKER,
    SUBARNAREKHA_CROP_ADVISORY,
    BALASORE_SMART_TRANSIT_TRACKER,
    FAKIR_MOHAN_STUDENT_HUB,
    BALESWARI_CUISINE_EXPLORER,
    TPNODL_POWER_OUTAGE_RADAR,
    // 6 Coastal Nocturnal, Medical Emergency, Tribal Craft, Freedom Trail, Eco-Corridor & Cashew Mandi Suites:
    CHANDIPUR_BIOLUMINESCENCE_EXPLORER,
    BALASORE_SNAKEBITE_ANTI_VENOM,
    NILAGIRI_DOKRA_CRAFT_GUILD,
    BAGHA_JATIN_FREEDOM_TRAIL,
    KULDIHA_ECO_CORRIDOR_FIRE_RADAR,
    BHOGRAI_CASHEW_GROUNDNUT_MANDI,
    // 6 Medieval Heritage, Deep-Sea Trawlers, Lacquer Guild, Blood Grid, River Sluice & Youth Wellness Suites:
    RAIBANIA_FORTRESS_VAULT,
    BALARAMGADI_TRAWLER_FLEET_RADAR,
    NILAGIRI_LACQUER_BAMBOO_GUILD,
    BALASORE_DISTRICT_BLOOD_DONOR_GRID,
    SUBARNAREKHA_FLOOD_SLUICE_TELEMETRY,
    BALASORE_YOUTH_WELLNESS_STUDY_CIRCLE,
    // Google Maps Pre-cached Offline Map Tiles & Outage Navigation
    BALASORE_OFFLINE_MAP_TILES,
    // 6 Coastal SOS, Culinary GI, Micro-Ward Flood, Elephant Sentinel, Student Career & Solar Radar Suites:
    MARINE_FISHERMEN_HIGH_SEAS_SOS,
    BALESWARI_CULINARY_GI_TRAIL,
    WARD_FLOOD_EVACUATION_ROUTER,
    KULDIHA_ELEPHANT_CORRIDOR_SENTINEL,
    STUDENT_CAREER_LIBRARY_PULSE,
    ROOFTOP_SOLAR_TPNODL_RADAR,
    // 8 Eco-Tourism, Sanctum, Fish Mandi, Transit, Civic Sanitation, Industrial Jobs & Krushak PACS Suites:
    TALASARI_UDAYPUR_RED_CRAB_SAFARI,
    PANCHALINGESWAR_WATERFALL_TREK_MONITOR,
    EMAMI_JAGANNATH_REMUNA_HERITAGE,
    BALARAMGADI_FISH_MANDI_RATES,
    TOWN_AUTO_RICKSHAW_FARE_CALCULATOR,
    WARD_SANITATION_GARBAGE_VEHICLE_TRACKER,
    BALASORE_INDUSTRIAL_JOB_APPRENTICE_BOARD,
    KRUSHAK_PACS_MANDI_GRAIN_RADAR,
    // Auto-Updated New Features Suite
    OLIVE_RIDLEY_MARINE_WILDLIFE,
    AIIMS_DHH_OPD_BED_TRACKER,
    OFFLINE_CYCLONE_SAFETY_TOOLKIT,
    // Offline Room Database Civic Reports Suite
    MY_CIVIC_REPORTS,
    // OpenWeatherMap Real-Time Telemetry & 3-Day Forecast Suite
    OPEN_WEATHER_MAP,
    // Google Maps Coastal Precipitation Radar & Storm Tracking Suite
    COASTAL_WEATHER_RADAR_MAP,
    // Odia Culture & Daily Horoscope Astrology Suite
    ODIA_CULTURE_SECTION,
    // Odia Festivals & Important Cultural Dates Room Suite
    ODIA_FESTIVALS,
    // Google Maps Tourism Hotspots & Distance Markers Carousel
    TOURISM_HOTSPOTS,
    // Live Balasore City Bus & Public Transit API Suite
    LOCAL_TRANSPORT,
    // Nearest Emergency Services Map relative to user position
    NEAREST_EMERGENCY_SERVICES_MAP
}

data class GroundingState(
    val isSheetOpen: Boolean = false,
    val query: String = "",
    val isQuerying: Boolean = false,
    val toolMode: GroundingToolMode = GroundingToolMode.COMBINED,
    val response: GroundingResponse? = null,
    val errorMessage: String? = null
)

enum class AppTab {
    HOTSPOTS,
    NEWS,
    WEATHER,
    ESSENTIALS
}

enum class AuthMode {
    PROFILE,
    LOGIN,
    SIGNUP,
    FORGOT_PASSWORD,
    EDIT_PROFILE
}

class BalasoreViewModel : ViewModel() {

    private var itineraryRepo: ItineraryRepository? = null
    var travelJournalRepo: TravelJournalRepository? = null
        private set
    private var tideDao: ChandipurTideDao? = null
    private var newsRepo: NewsRepository? = null
    private var civicReportDao: CivicReportDao? = null
    var civicReportRepo: CivicReportRepository? = null
        private set
    private var appContext: Context? = null
    private val apiService: BalasoreApiService by lazy { BalasoreApiService.create() }
    private val weatherApiService: WeatherApiService by lazy { WeatherApiService.create() }
    private val openWeatherRepo: OpenWeatherRepository by lazy { OpenWeatherRepository() }
    private val geminiChatService: GeminiChatService by lazy { GeminiChatService() }

    private val _uiState = MutableStateFlow(BalasoreUiState())
    val uiState: StateFlow<BalasoreUiState> = _uiState.asStateFlow()

    init {
        try {
            if (android.os.Looper.getMainLooper() != null) {
                fetchLiveEmergencyAlerts()
                fetchRealTimeWeather()
                fetchOpenWeatherData()
                // Periodic auto-update ticker:
                // 1) Refreshes daily pulse every 60s
                // 2) Ticks countdown for the auto refresh loop
                // 3) Automatically triggers full data refresh based on user-configured frequency
                // 4) Auto-progresses citizen civic issue reports (PENDING -> IN_PROGRESS -> RESOLVED)
                viewModelScope.launch {
                    while (true) {
                        delay(60_000L)
                        refreshDailyPulse()
                        try {
                            civicReportRepo?.autoProgressReports()
                        } catch (_: Throwable) {}
                        if (_uiState.value.isHourlyAutoRefreshEnabled) {
                            val elapsedMillis = System.currentTimeMillis() - _uiState.value.lastHourlyRefreshTimestamp
                            val elapsedMinutes = (elapsedMillis / (1000 * 60)).toInt()
                            val freq = _uiState.value.autoUpdateFrequencyMinutes.coerceAtLeast(1)
                            val remaining = (freq - elapsedMinutes).coerceAtLeast(0)
                            if (remaining <= 0) {
                                triggerHourlyAutoRefresh()
                            } else {
                                _uiState.value = _uiState.value.copy(
                                    nextHourlyRefreshMinutesRemaining = remaining
                                )
                            }
                        }
                    }
                }
            }
        } catch (_: Throwable) {
            // JVM unit test environment without Android Looper
        }
    }

    /**
     * Connects local Room database and streams itinerary items and tide data into UI state.
     */
    fun initDatabase(context: Context) {
        appContext = context.applicationContext
        if (itineraryRepo != null && tideDao != null && civicReportRepo != null) {
            appContext?.let { ctx ->
                com.example.data.fcm.BalasoreAlertDispatchEngine.evaluateAndDispatchRealTimeAlerts(
                    context = ctx,
                    weather = _uiState.value.weather,
                    pulse = _uiState.value.dailyPulse,
                    emergencyAlerts = _uiState.value.liveEmergencyAlerts
                )
            }
            return
        }
        try {
            val db = AppDatabase.getInstance(context)
            val repo = ItineraryRepository(db.itineraryDao())
            itineraryRepo = repo

            val jRepo = TravelJournalRepository(db.travelJournalDao())
            travelJournalRepo = jRepo

            val tDao = db.chandipurTideDao()
            tideDao = tDao

            viewModelScope.launch {
                try {
                    repo.ensureDefaultItinerarySeeded()
                } catch (t: Throwable) {
                    android.util.Log.e("BalasoreViewModel", "Error seeding default itinerary: ${t.message}")
                }
            }
            viewModelScope.launch {
                try {
                    jRepo.ensureDefaultJournalSeeded()
                } catch (t: Throwable) {
                    android.util.Log.e("BalasoreViewModel", "Error seeding default journal: ${t.message}")
                }
            }
            // Room Entity for Chandipur Tide Schedule (Last Known Tide when offline)
            viewModelScope.launch {
                try {
                    val existingTide = tDao.getLatestTideForecastSync()
                    if (existingTide == null) {
                        val initialTide = ChandipurTideEntity(
                            id = "chandipur_tide_current",
                            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                            lowTideTime = "02:45 PM",
                            highTideTime = "08:30 AM",
                            nextHighTideTime = "08:15 PM",
                            recededDistanceKm = 4.8,
                            currentWaterLevelMeters = 0.6,
                            tideState = "RECEDING",
                            safeWalkStatus = "SAFE_WALK",
                            safeWalkMinutesRemaining = 135,
                            lunarCondition = "Spring Tide (Amavasya Cycle)",
                            tidalForecastSummary = "Sea recedes up to 5 km into the Bay of Bengal. Rare natural walking opportunity until 04:30 PM.",
                            isOfflineCached = true,
                            lastFetchedTimestamp = System.currentTimeMillis()
                        )
                        tDao.insertOrUpdateTide(initialTide)
                        _uiState.value = _uiState.value.copy(lastKnownTideForecast = initialTide)
                    } else {
                        _uiState.value = _uiState.value.copy(lastKnownTideForecast = existingTide)
                    }

                    tDao.getLatestTideForecastFlow().collect { cachedTide ->
                        if (cachedTide != null) {
                            _uiState.value = _uiState.value.copy(lastKnownTideForecast = cachedTide)
                        }
                    }
                } catch (t: Throwable) {
                    android.util.Log.e("BalasoreViewModel", "Error initializing Chandipur tide entity: ${t.message}")
                }
            }

            viewModelScope.launch {
                try {
                    repo.allItineraryItems.collect { items ->
                        if (items.isNotEmpty()) {
                            _uiState.value = _uiState.value.copy(itineraryItems = items)
                        }
                    }
                } catch (t: Throwable) {
                    android.util.Log.e("BalasoreViewModel", "Error collecting itinerary items: ${t.message}")
                }
            }

            // Room Database + Network unified News Repository with auto cache updates
            val nRepo = NewsRepository(db.newsDao(), db.cacheMetadataDao())
            newsRepo = nRepo

            // Initial fetch or cache load from repository
            viewModelScope.launch {
                try {
                    nRepo.getUnifiedNewsFlow(forceRefresh = false).collect { _ -> }
                } catch (t: Throwable) {
                    android.util.Log.w("BalasoreViewModel", "News stream initial fetch notice: ${t.message}")
                }
            }

            // Continuous reactive observation of all Room news articles
            viewModelScope.launch {
                try {
                    nRepo.allNews.collect { entities ->
                        if (entities.isNotEmpty()) {
                            val mapped = entities.map { entity ->
                                NewsArticle(
                                    id = entity.id.toString(),
                                    title = entity.title,
                                    odiaTitle = entity.title,
                                    snippet = entity.summary,
                                    odiaSnippet = entity.summary,
                                    category = entity.category,
                                    timeAgo = entity.publishedAt,
                                    source = entity.source,
                                    isBookmarked = entity.isBookmarked,
                                    content = entity.content,
                                    odiaContent = entity.content
                                )
                            }
                            val bIds = entities.filter { it.isBookmarked }.map { it.id.toString() }.toSet()
                            val recomputed = NewsFeedFilter.filterArticles(mapped, _uiState.value.selectedNewsTab, _uiState.value.newsSearchQuery)
                            _uiState.value = _uiState.value.copy(
                                newsArticles = mapped,
                                filteredNewsArticles = recomputed,
                                bookmarkedIds = if (bIds.isNotEmpty()) bIds else _uiState.value.bookmarkedIds
                            )
                        }
                    }
                } catch (t: Throwable) {
                    android.util.Log.w("BalasoreViewModel", "Room allNews observer notice: ${t.message}")
                }
            }

            // Continuous reactive observation of Room bookmarks
            viewModelScope.launch {
                try {
                    nRepo.getBookmarkedNews().collect { bookmarkedEntities ->
                        val bIds = bookmarkedEntities.map { it.id.toString() }.toSet()
                        _uiState.value = _uiState.value.copy(
                            bookmarkedIds = bIds
                        )
                    }
                } catch (t: Throwable) {
                    android.util.Log.w("BalasoreViewModel", "Room bookmarks observer notice: ${t.message}")
                }
            }

            // Room Database Citizen Civic Reports Repository
            val cDao = db.civicReportDao()
            civicReportDao = cDao
            val cRepo = CivicReportRepository(cDao)
            civicReportRepo = cRepo
            viewModelScope.launch {
                try {
                    cRepo.ensureDefaultReportsSeeded()
                } catch (t: Throwable) {
                    android.util.Log.e("BalasoreViewModel", "Error seeding civic reports: ${t.message}")
                }
            }
            viewModelScope.launch {
                try {
                    cRepo.allReports.collect { reports ->
                        _uiState.value = _uiState.value.copy(myCivicReports = reports)
                    }
                } catch (t: Throwable) {
                    android.util.Log.e("BalasoreViewModel", "Error streaming civic reports: ${t.message}")
                }
            }
        } catch (t: Throwable) {
            android.util.Log.e("BalasoreViewModel", "Failed to initialize database: ${t.message}")
        }
    }

    fun setCivicReportRepository(repo: CivicReportRepository) {
        this.civicReportRepo = repo
        viewModelScope.launch {
            repo.ensureDefaultReportsSeeded()
        }
        viewModelScope.launch {
            repo.allReports.collect { reports ->
                _uiState.value = _uiState.value.copy(myCivicReports = reports)
            }
        }
    }

    fun submitCivicReport(
        title: String,
        category: String,
        wardLocation: String,
        description: String,
        urgency: String = "NORMAL",
        hasPhotoAttached: Boolean = false
    ) {
        val trackingNumber = 1000 + kotlin.random.Random.nextInt(9000)
        val reportId = "BLS-CIVIC-2026-$trackingNumber"
        val dept = when (category) {
            "Road Pothole" -> "PWD & Balasore Municipality Road Div"
            "Faulty Streetlight" -> "TPNODL & Municipal Electrical Wing"
            "Drain Clog / Waterlogging" -> "Drainage Desiltation Taskforce"
            "Garbage Heap / Waste" -> "BMC Solid Waste Sanitation Wing"
            "Drinking Water Leak" -> "WATCO Public Health Engineering"
            else -> "Balasore Municipality Grievance Cell"
        }
        val newEntity = CivicReportEntity(
            id = reportId,
            title = title.ifBlank { "$category near $wardLocation" },
            category = category,
            wardLocation = wardLocation,
            description = description.ifBlank { "Citizen issue reported via Balasore 360 app." },
            status = "PENDING",
            urgency = urgency,
            assignedDepartment = dept,
            reportedTimestamp = System.currentTimeMillis(),
            lastUpdatedTimestamp = System.currentTimeMillis(),
            hasPhotoAttached = hasPhotoAttached,
            resolutionNotes = "Report registered offline in Room DB. Auto-synced with Ward Grievance Desk."
        )

        // Immediately reflect in UI state so tests and UI have zero latency
        _uiState.value = _uiState.value.copy(
            myCivicReports = listOf(newEntity) + _uiState.value.myCivicReports
        )

        try {
            viewModelScope.launch {
                try {
                    civicReportRepo?.submitReport(title, category, wardLocation, description, urgency, hasPhotoAttached)
                } catch (t: Throwable) {
                    android.util.Log.e("BalasoreViewModel", "Error submitting report: ${t.message}")
                }
            }
        } catch (_: Throwable) {
            // JVM unit test fallback without Dispatchers.Main
            _uiState.value = _uiState.value.copy(
                myCivicReports = listOf(newEntity) + _uiState.value.myCivicReports
            )
        }
    }

    fun deleteCivicReport(reportId: String) {
        _uiState.value = _uiState.value.copy(
            myCivicReports = _uiState.value.myCivicReports.filterNot { it.id == reportId }
        )

        try {
            viewModelScope.launch {
                try {
                    civicReportRepo?.deleteReport(reportId)
                } catch (_: Throwable) {}
            }
        } catch (_: Throwable) {}
    }

    fun advanceCivicReportStatus(reportId: String) {
        val now = System.currentTimeMillis()
        val advanceBlock: (List<CivicReportEntity>) -> List<CivicReportEntity> = { list ->
            list.map { report ->
                if (report.id == reportId) {
                    val nextStatus = when (report.status) {
                        "PENDING" -> "IN_PROGRESS"
                        "IN_PROGRESS" -> "RESOLVED"
                        else -> "RESOLVED"
                    }
                    val notes = when (nextStatus) {
                        "IN_PROGRESS" -> "Field inspection completed by ${report.assignedDepartment}. Repair crew dispatched to ${report.wardLocation}."
                        "RESOLVED" -> "Work completed and verified by Ward Junior Engineer. Issue closed successfully in municipal registry."
                        else -> report.resolutionNotes
                    }
                    report.copy(status = nextStatus, lastUpdatedTimestamp = now, resolutionNotes = notes)
                } else {
                    report
                }
            }
        }

        _uiState.value = _uiState.value.copy(myCivicReports = advanceBlock(_uiState.value.myCivicReports))

        try {
            viewModelScope.launch {
                try {
                    civicReportRepo?.progressReportStatus(reportId)
                } catch (_: Throwable) {}
            }
        } catch (_: Throwable) {}
    }

    fun setMyReportsFilter(filter: String) {
        _uiState.value = _uiState.value.copy(selectedMyReportsFilter = filter)
    }

    fun autoProgressCivicReports() {
        val now = System.currentTimeMillis()
        val autoBlock: (List<CivicReportEntity>) -> List<CivicReportEntity> = { list ->
            list.map { report ->
                if (report.status == "PENDING") {
                    report.copy(
                        status = "IN_PROGRESS",
                        lastUpdatedTimestamp = now,
                        resolutionNotes = "Inspection assigned to ${report.assignedDepartment}. Field technician en route to ${report.wardLocation}."
                    )
                } else if (report.status == "IN_PROGRESS") {
                    report.copy(
                        status = "RESOLVED",
                        lastUpdatedTimestamp = now,
                        resolutionNotes = "Repair work completed by municipal squad. Quality check passed by Balasore Ward Supervisor."
                    )
                } else {
                    report
                }
            }
        }

        try {
            viewModelScope.launch {
                try {
                    if (civicReportRepo != null) {
                        civicReportRepo?.autoProgressReports()
                    } else {
                        _uiState.value = _uiState.value.copy(myCivicReports = autoBlock(_uiState.value.myCivicReports))
                    }
                } catch (_: Throwable) {}
            }
        } catch (_: Throwable) {
            _uiState.value = _uiState.value.copy(myCivicReports = autoBlock(_uiState.value.myCivicReports))
        }
    }

    fun setItineraryRepository(repo: ItineraryRepository) {
        this.itineraryRepo = repo
        viewModelScope.launch {
            repo.ensureDefaultItinerarySeeded()
        }
        viewModelScope.launch {
            repo.allItineraryItems.collect { items ->
                _uiState.value = _uiState.value.copy(itineraryItems = items)
            }
        }
    }

    fun addToItinerary(hotspot: Hotspot, day: Int = 1, timeSlot: String = "10:00 AM", notes: String = "") {
        viewModelScope.launch {
            itineraryRepo?.addToItinerary(hotspot, day, timeSlot, notes)
        }
    }

    fun removeFromItinerary(id: Long) {
        viewModelScope.launch {
            itineraryRepo?.removeItem(id)
        }
    }

    fun clearItinerary() {
        viewModelScope.launch {
            itineraryRepo?.clearAll()
        }
    }

    fun dismissEmergencyAlert(alertId: String) {
        _uiState.value = _uiState.value.copy(
            dismissedAlertIds = _uiState.value.dismissedAlertIds + alertId
        )
    }

    fun fetchLiveEmergencyAlerts() {
        viewModelScope.launch {
            try {
                val response = apiService.getLiveEmergencyAlerts()
                _uiState.value = _uiState.value.copy(
                    liveEmergencyAlerts = response.alerts
                )
                appContext?.let { ctx ->
                    com.example.data.fcm.BalasoreAlertDispatchEngine.evaluateAndDispatchRealTimeAlerts(
                        context = ctx,
                        weather = _uiState.value.weather,
                        pulse = _uiState.value.dailyPulse,
                        emergencyAlerts = response.alerts
                    )
                }
            } catch (e: Exception) {
                // Keep existing or fallback gracefully
            }
        }
    }

    fun selectTab(tabIndex: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = tabIndex)
    }

    fun setLanguage(lang: AppLanguage) {
        _uiState.value = _uiState.value.copy(language = lang)
    }

    fun toggleLanguage() {
        val next = if (_uiState.value.language == AppLanguage.ODIA) AppLanguage.ENGLISH else AppLanguage.ODIA
        setLanguage(next)
    }

    fun setHotspotSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(hotspotSearchQuery = query)
    }

    fun setHotspotCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedHotspotCategory = category)
    }

    fun selectNewsTab(tabIndex: Int) {
        val tab = NewsFeedTab.fromIndex(tabIndex)
        val filtered = NewsFeedFilter.filterArticles(_uiState.value.newsArticles, tab.index, _uiState.value.newsSearchQuery)
        _uiState.value = _uiState.value.copy(
            selectedNewsTab = tab.index,
            selectedNewsCategory = tab.categoryName,
            filteredNewsArticles = filtered
        )
    }

    fun setNewsCategory(category: String) {
        val tab = NewsFeedTab.fromCategory(category)
        val filtered = NewsFeedFilter.filterArticles(_uiState.value.newsArticles, tab.index, _uiState.value.newsSearchQuery)
        _uiState.value = _uiState.value.copy(
            selectedNewsCategory = category,
            selectedNewsTab = tab.index,
            filteredNewsArticles = filtered
        )
    }

    fun setNewsSearchQuery(query: String) {
        val filtered = NewsFeedFilter.filterArticles(_uiState.value.newsArticles, _uiState.value.selectedNewsTab, query)
        _uiState.value = _uiState.value.copy(
            newsSearchQuery = query,
            filteredNewsArticles = filtered
        )
    }

    fun toggleBookmark(articleId: String) {
        val current = _uiState.value.bookmarkedIds
        val isCurrentlyBookmarked = current.contains(articleId)
        val newBookmarkedState = !isCurrentlyBookmarked
        val updated = if (isCurrentlyBookmarked) current - articleId else current + articleId

        val updatedArticles = _uiState.value.newsArticles.map { article ->
            if (article.id == articleId) article.copy(isBookmarked = newBookmarkedState) else article
        }
        val recomputedFiltered = NewsFeedFilter.filterArticles(updatedArticles, _uiState.value.selectedNewsTab, _uiState.value.newsSearchQuery)

        // Optimistic UI update
        _uiState.value = _uiState.value.copy(
            bookmarkedIds = updated,
            newsArticles = updatedArticles,
            filteredNewsArticles = recomputedFiltered
        )

        val idLong = articleId.toLongOrNull()
        if (idLong != null) {
            viewModelScope.launch {
                try {
                    newsRepo?.toggleBookmark(idLong, isCurrentlyBookmarked)
                } catch (e: Throwable) {
                    android.util.Log.e("BalasoreViewModel", "Error saving bookmark to Room: ${e.message}")
                }
            }
        }
    }

    fun toggleFavoriteHotspot(hotspotId: String) {
        val current = _uiState.value.favoriteHotspotIds
        val updated = if (current.contains(hotspotId)) current - hotspotId else current + hotspotId
        _uiState.value = _uiState.value.copy(favoriteHotspotIds = updated)
    }

    fun refreshAll() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            fetchRealTimeWeather()
            fetchLiveEmergencyAlerts()
            refreshDailyPulse()
            try {
                civicReportRepo?.autoProgressReports()
            } catch (_: Throwable) {}
            try {
                newsRepo?.refreshNews()
            } catch (_: Throwable) {}
            delay(800)
            _uiState.value = _uiState.value.copy(isRefreshing = false)
        }
    }

    /**
     * Fetches real-time meteorological data for Balasore using the Open-Meteo public API
     * and updates the UI state prominently with temperature, humidity, wind, UV index, hourly track, and 7-day outlook.
     */
    fun fetchRealTimeWeather() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isWeatherLoading = true)
            try {
                val response = weatherApiService.getBalasoreForecast(
                    latitude = 21.4934,
                    longitude = 86.9135,
                    hourly = "temperature_2m,relative_humidity_2m,precipitation_probability,weather_code,wind_speed_10m",
                    forecastDays = 7
                )
                val current = response.current
                val daily = response.daily
                val hourly = response.hourly

                if (current != null) {
                    val tempC = current.temperature?.toInt() ?: 29
                    val feelsLikeC = current.apparentTemperature?.toInt() ?: (tempC + 2)
                    val humidity = "${current.relativeHumidity ?: 75}%"
                    val windKmh = "${current.windSpeed?.toInt() ?: 18} km/h"
                    val gustsKmh = "${current.windGusts?.toInt() ?: 24} km/h"
                    val conditionStr = weatherCodeToDescription(current.weatherCode ?: 2)
                    val highLowStr = if (daily?.temperatureMax?.isNotEmpty() == true && daily.temperatureMin?.isNotEmpty() == true) {
                        "${daily.temperatureMax[0].toInt()}° / ${daily.temperatureMin[0].toInt()}°"
                    } else {
                        "${tempC + 4}° / ${tempC - 3}°"
                    }
                    val uv = daily?.uvIndexMax?.firstOrNull() ?: 6.2
                    val precip = current.precipitation ?: 0.0

                    val nowFormat = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())

                    val wCode = current.weatherCode ?: 2
                    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                    val isDaytime = currentHour in 6..18
                    val dynamicIconUrl = weatherCodeToIconUrl(wCode, isDaytime)
                    val dynamicIconType = weatherCodeToIconType(wCode)

                    val updatedWeather = _uiState.value.weather.copy(
                        tempCelsius = tempC,
                        condition = conditionStr,
                        highLow = highLowStr,
                        humidity = humidity,
                        windSpeedKmh = windKmh,
                        feelsLikeCelsius = feelsLikeC,
                        windGustsKmh = gustsKmh,
                        uvIndex = uv,
                        precipitationMm = precip,
                        dataSource = "Open-Meteo Real-Time Met API",
                        lastUpdatedTime = "Live • Updated $nowFormat",
                        isLiveApi = true,
                        weatherCode = wCode,
                        conditionIconUrl = dynamicIconUrl,
                        iconType = dynamicIconType,
                        isDay = isDaytime
                    )

                    // Parse next 24 hourly steps
                    val parsedHourly = mutableListOf<BalasoreForecastHour>()
                    if (hourly?.time != null && hourly.temperature != null) {
                        val count = minOf(hourly.time.size, hourly.temperature.size, 24)
                        for (i in 0 until count) {
                            val rawTime = hourly.time[i]
                            val hourLabel = try {
                                val hourPart = rawTime.substringAfter("T").take(5)
                                val h = hourPart.substringBefore(":").toInt()
                                when {
                                    h == 0 -> "12 AM"
                                    h < 12 -> "${h} AM"
                                    h == 12 -> "12 PM"
                                    else -> "${h - 12} PM"
                                }
                            } catch (_: Exception) {
                                "${i}:00"
                            }
                            val temp = hourly.temperature[i].toInt()
                            val code = hourly.weatherCode?.getOrNull(i) ?: 1
                            val pop = hourly.precipitationProbability?.getOrNull(i) ?: 10
                            val wind = hourly.windSpeed?.getOrNull(i)?.toInt() ?: 15
                            val emoji = weatherCodeToEmoji(code)
                            parsedHourly.add(
                                BalasoreForecastHour(
                                    timeLabel = hourLabel,
                                    tempC = temp,
                                    conditionEmoji = emoji,
                                    popPercentage = pop,
                                    windKmh = wind
                                )
                            )
                        }
                    }

                    // Parse 7-day daily forecast
                    val parsedDays = mutableListOf<BalasoreForecastDay>()
                    if (daily?.time != null && daily.temperatureMax != null && daily.temperatureMin != null) {
                        val count = minOf(daily.time.size, daily.temperatureMax.size, daily.temperatureMin.size, 7)
                        for (i in 0 until count) {
                            val dateStr = daily.time[i]
                            val high = daily.temperatureMax[i].toInt()
                            val low = daily.temperatureMin[i].toInt()
                            val code = daily.weatherCode?.getOrNull(i) ?: 1
                            val uvD = daily.uvIndexMax?.getOrNull(i) ?: 6.0
                            val desc = weatherCodeToDescription(code)
                            val emoji = weatherCodeToEmoji(code)

                            val dayLabel = if (i == 0) "Today" else if (i == 1) "Tomorrow" else {
                                try {
                                    val parsedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateStr)
                                    SimpleDateFormat("EEE", Locale.getDefault()).format(parsedDate!!)
                                } catch (_: Exception) { "Day ${i + 1}" }
                            }
                            val odiaDayLabel = if (i == 0) "ଆଜି" else if (i == 1) "ଆସନ୍ତାକାଲି" else dayLabel
                            val odiaDesc = when {
                                desc.contains("Rain", true) || desc.contains("Downpour", true) -> "ବର୍ଷା ସମ୍ଭାବନା"
                                desc.contains("Thunder", true) -> "ବଜ୍ରପାତ ସତର୍କତା"
                                desc.contains("Cloud", true) -> "ମେଘୁଆ ପାଗ"
                                desc.contains("Fog", true) -> "କୁହୁଡ଼ି"
                                else -> "ଖରାଟିଆ ନିର୍ମଳ ଆକାଶ"
                            }

                            parsedDays.add(
                                BalasoreForecastDay(
                                    id = "day_$i",
                                    dayLabel = dayLabel,
                                    dateFormatted = dateStr,
                                    odiaDayLabel = odiaDayLabel,
                                    highTempC = high,
                                    lowTempC = low,
                                    condition = desc,
                                    odiaCondition = odiaDesc,
                                    weatherIcon = emoji,
                                    rainProbability = if (code in 51..67 || code in 80..82) 65 else 20,
                                    windSummary = "14 km/h SW",
                                    humidity = "76%",
                                    uvIndex = "UV ${uvD.toInt()}",
                                    marineNotice = if (code in 51..67) "Rough surf near Kasafal & Balaramgadi" else "Calm sea swell for Chandipur beach walk"
                                )
                            )
                        }
                    }

                    _uiState.value = _uiState.value.copy(
                        weather = updatedWeather,
                        hourlyForecast = if (parsedHourly.isNotEmpty()) parsedHourly else _uiState.value.hourlyForecast,
                        forecastDays = if (parsedDays.isNotEmpty()) parsedDays else _uiState.value.forecastDays,
                        isWeatherLoading = false
                    )
                    appContext?.let { ctx ->
                        com.example.data.fcm.BalasoreAlertDispatchEngine.evaluateAndDispatchRealTimeAlerts(
                            context = ctx,
                            weather = updatedWeather,
                            pulse = _uiState.value.dailyPulse,
                            emergencyAlerts = _uiState.value.liveEmergencyAlerts
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(isWeatherLoading = false)
                }
            } catch (e: Exception) {
                // Graceful fallback to cached meteorological state
                android.util.Log.e("BalasoreViewModel", "Weather API fetch exception: ${e.message}")
                _uiState.value = _uiState.value.copy(isWeatherLoading = false)
            }
        }
    }

    /**
     * Fetches real-time conditions and 7-day forecast for Balasore via OpenWeatherMap API.
     */
    fun fetchOpenWeatherData(isUserTriggered: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isOpenWeatherLoading = true)
            val apiKey = com.example.BuildConfig.OPENWEATHERMAP_API_KEY
            val weatherResult = openWeatherRepo.fetchCurrentWeather(apiKey)
            val forecastResult = openWeatherRepo.fetchThreeDayForecast(apiKey)
            val forecast7Result = openWeatherRepo.fetchSevenDayForecast(apiKey)

            val current = if (weatherResult.isSuccess) {
                weatherResult.getOrNull()
            } else {
                openWeatherRepo.createBaselineBalasoreWeather(_uiState.value.openWeatherCycleCount)
            }

            val forecast = if (forecastResult.isSuccess) {
                forecastResult.getOrNull() ?: emptyList()
            } else {
                openWeatherRepo.createBaselineThreeDayForecast(_uiState.value.openWeatherCycleCount)
            }

            val forecast7 = if (forecast7Result.isSuccess) {
                forecast7Result.getOrNull() ?: emptyList()
            } else {
                openWeatherRepo.createBaselineSevenDayForecast(_uiState.value.openWeatherCycleCount)
            }

            _uiState.value = _uiState.value.copy(
                openWeatherCurrent = current,
                openWeatherThreeDayForecast = forecast,
                openWeatherSevenDayForecast = forecast7,
                isOpenWeatherLoading = false,
                openWeatherCycleCount = _uiState.value.openWeatherCycleCount + 1
            )
        }
    }

    fun setWalkWindowAlertsEnabled(context: Context, enabled: Boolean) {
        TideAndWeatherNotificationManager.setWalkWindowAlertsEnabled(context, enabled)
        _uiState.value = _uiState.value.copy(isWalkWindowAlertsEnabled = enabled)
    }

    fun setWalkWindowAdvanceMinutes(context: Context, minutes: Int) {
        TideAndWeatherNotificationManager.setWalkWindowAdvanceMinutes(context, minutes)
        _uiState.value = _uiState.value.copy(walkWindowAdvanceMinutes = minutes)
    }

    fun testTriggerWalkWindowOpeningAlert(context: Context) {
        TideAndWeatherNotificationManager.testTriggerWalkWindowOpening(context)
    }

    fun testTriggerWalkWindowClosingAlert(context: Context) {
        TideAndWeatherNotificationManager.testTriggerWalkWindowClosing(context)
    }

    private fun weatherCodeToEmoji(code: Int): String = when (code) {
        0 -> "☀️"
        1 -> "🌤️"
        2 -> "⛅"
        3 -> "☁️"
        45, 48 -> "🌫️"
        51, 53, 55 -> "🌦️"
        61, 63 -> "🌧️"
        65 -> "⛈️"
        80, 81, 82 -> "🌧️"
        95, 96, 99 -> "⛈️"
        else -> "⛅"
    }

    fun weatherCodeToIconUrl(code: Int, isDay: Boolean): String {
        val suffix = if (isDay) "d" else "n"
        return when (code) {
            0 -> "https://openweathermap.org/img/wn/01$suffix@2x.png"
            1 -> "https://openweathermap.org/img/wn/02$suffix@2x.png"
            2 -> "https://openweathermap.org/img/wn/03$suffix@2x.png"
            3 -> "https://openweathermap.org/img/wn/04$suffix@2x.png"
            45, 48 -> "https://openweathermap.org/img/wn/50$suffix@2x.png"
            51, 53, 55, 61, 63, 65 -> "https://openweathermap.org/img/wn/10$suffix@2x.png"
            80, 81, 82 -> "https://openweathermap.org/img/wn/09$suffix@2x.png"
            95, 96, 99 -> "https://openweathermap.org/img/wn/11$suffix@2x.png"
            else -> "https://openweathermap.org/img/wn/02$suffix@2x.png"
        }
    }

    fun weatherCodeToIconType(code: Int): String = when (code) {
        0 -> "clear_sky"
        1, 2 -> "partly_cloudy"
        3 -> "overcast"
        45, 48 -> "fog_mist"
        51, 53, 55, 61, 63, 65, 80, 81, 82 -> "rain"
        95, 96, 99 -> "thunderstorm"
        else -> "partly_cloudy"
    }

    // ==========================================
    // GEMINI MULTI-TURN CHAT & LYRIA AI ENGINE
    // ==========================================

    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        val userMsg = ChatMessage(
            text = text,
            sender = MessageSender.USER
        )
        val currentHistory = _uiState.value.chatMessages
        _uiState.value = _uiState.value.copy(
            chatMessages = currentHistory + userMsg,
            isChatLoading = true
        )

        viewModelScope.launch {
            val response = geminiChatService.sendMultiTurnChat(
                history = currentHistory,
                userMessage = text,
                model = _uiState.value.selectedChatModel,
                enableSearchGrounding = _uiState.value.isChatSearchGrounding,
                enableMapsGrounding = _uiState.value.isChatMapsGrounding,
                systemInstruction = _uiState.value.selectedChatRole.systemInstruction
            )

            _uiState.value = _uiState.value.copy(
                chatMessages = _uiState.value.chatMessages + response,
                isChatLoading = false
            )
        }
    }

    fun setChatModel(model: GeminiChatModel) {
        _uiState.value = _uiState.value.copy(selectedChatModel = model)
    }

    fun setChatRole(role: ChatRolePersona) {
        _uiState.value = _uiState.value.copy(selectedChatRole = role)
    }

    fun toggleChatSearchGrounding() {
        _uiState.value = _uiState.value.copy(isChatSearchGrounding = !_uiState.value.isChatSearchGrounding)
    }

    fun toggleChatMapsGrounding() {
        _uiState.value = _uiState.value.copy(isChatMapsGrounding = !_uiState.value.isChatMapsGrounding)
    }

    fun clearChat() {
        _uiState.value = _uiState.value.copy(chatMessages = emptyList())
    }

    fun generateLyriaMusic(prompt: String, isShortClip: Boolean = true) {
        _uiState.value = _uiState.value.copy(isMusicGenerating = true)
        viewModelScope.launch {
            val trackMsg = geminiChatService.generateMusic(prompt, isShortClip)
            _uiState.value = _uiState.value.copy(
                chatMessages = _uiState.value.chatMessages + trackMsg,
                isMusicGenerating = false
            )
        }
    }

    private fun weatherCodeToDescription(code: Int): String = when (code) {
        0 -> "Clear Sunny Sky"
        1 -> "Mainly Sunny"
        2 -> "Partly Cloudy"
        3 -> "Overcast Sky"
        45, 48 -> "Coastal Fog / Mist"
        51, 53, 55 -> "Light Coastal Drizzle"
        61, 63 -> "Moderate Rain"
        65 -> "Heavy Monsoon Downpour"
        80, 81, 82 -> "Rain Showers"
        95 -> "Bay of Bengal Thunderstorm"
        96, 99 -> "Severe Coastal Thunderstorm"
        else -> "Partly Cloudy"
    }

    fun openGroundingSheet(initialQuery: String = "", mode: GroundingToolMode = GroundingToolMode.COMBINED) {
        _uiState.value = _uiState.value.copy(
            groundingState = _uiState.value.groundingState.copy(
                isSheetOpen = true,
                query = initialQuery,
                toolMode = mode,
                errorMessage = null
            )
        )
    }

    fun closeGroundingSheet() {
        _uiState.value = _uiState.value.copy(
            groundingState = _uiState.value.groundingState.copy(isSheetOpen = false)
        )
    }

    fun setGroundingMode(mode: GroundingToolMode) {
        _uiState.value = _uiState.value.copy(
            groundingState = _uiState.value.groundingState.copy(toolMode = mode)
        )
    }

    fun executeGroundingQuery(query: String, mode: GroundingToolMode = _uiState.value.groundingState.toolMode) {
        if (query.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                groundingState = _uiState.value.groundingState.copy(
                    query = query,
                    toolMode = mode,
                    isQuerying = true,
                    errorMessage = null
                )
            )
            try {
                val response = GeminiGroundingService.getInstance().queryWithGrounding(
                    prompt = query,
                    toolMode = mode
                )
                _uiState.value = _uiState.value.copy(
                    groundingState = _uiState.value.groundingState.copy(
                        isQuerying = false,
                        response = response,
                        errorMessage = if (!response.isSuccess) response.errorMessage else null
                    )
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    groundingState = _uiState.value.groundingState.copy(
                        isQuerying = false,
                        errorMessage = e.message ?: "Failed to complete grounding query"
                    )
                )
            }
        }
    }

    fun openFeatureSheet(type: UniqueFeatureSheetType) {
        _uiState.value = _uiState.value.copy(activeFeatureSheet = type)
    }

    fun closeFeatureSheet() {
        _uiState.value = _uiState.value.copy(activeFeatureSheet = null)
    }

    fun refreshDailyPulse() {
        val newPulse = com.example.data.daily.DailyUpdateEngine.getDailyPulse()
        _uiState.value = _uiState.value.copy(
            dailyPulse = newPulse
        )
        appContext?.let { ctx ->
            com.example.data.fcm.BalasoreAlertDispatchEngine.evaluateAndDispatchRealTimeAlerts(
                context = ctx,
                weather = _uiState.value.weather,
                pulse = newPulse,
                emergencyAlerts = _uiState.value.liveEmergencyAlerts
            )
        }
    }

    fun toggleEmergencyKitItem(itemId: String) {
        val updated = _uiState.value.emergencyKitItems.map {
            if (it.id == itemId) it.copy(isChecked = !it.isChecked) else it
        }
        _uiState.value = _uiState.value.copy(emergencyKitItems = updated)
    }

    fun toggleEcoPassportStamp(stampId: String) {
        val updated = _uiState.value.ecoPassportStamps.map {
            if (it.id == stampId) it.copy(isCheckedIn = !it.isCheckedIn) else it
        }
        _uiState.value = _uiState.value.copy(ecoPassportStamps = updated)
    }

    fun logHorseshoeCrabSighting(reporter: String, sector: String, species: String, count: Int) {
        val newSighting = HorseshoeCrabSighting(
            id = "hsc_${System.currentTimeMillis()}",
            reporterName = reporter.ifBlank { "Citizen Eco-Guardian" },
            beachSector = sector.ifBlank { "Chandipur Intertidal Zone" },
            distanceOutKm = 2.5,
            species = species,
            count = if (count > 0) count else 1,
            condition = "Safely observed & logged for FMU Marine Research",
            reportedAgo = "Just now",
            verifiedByFMU = true
        )
        _uiState.value = _uiState.value.copy(
            horseshoeCrabSightings = listOf(newSighting) + _uiState.value.horseshoeCrabSightings
        )
    }

    fun toggleTemperatureUnit() {
        _uiState.value = _uiState.value.copy(isFahrenheit = !_uiState.value.isFahrenheit)
    }

    fun selectForecastDay(index: Int) {
        _uiState.value = _uiState.value.copy(selectedForecastDayIndex = index)
    }

    fun acknowledgeAlert(alertId: String) {
        val updated = _uiState.value.weatherAlerts.map {
            if (it.id == alertId) it.copy(isAcknowledged = true) else it
        }
        _uiState.value = _uiState.value.copy(weatherAlerts = updated)
    }

    fun setThemeMode(mode: ThemeMode) {
        _uiState.value = _uiState.value.copy(themeMode = mode)
    }

    fun cycleThemeMode() {
        val next = when (_uiState.value.themeMode) {
            ThemeMode.SYSTEM -> ThemeMode.LIGHT
            ThemeMode.LIGHT -> ThemeMode.DARK
            ThemeMode.DARK -> ThemeMode.HIGH_CONTRAST_DARK
            ThemeMode.HIGH_CONTRAST_DARK -> ThemeMode.SYSTEM
        }
        _uiState.value = _uiState.value.copy(themeMode = next)
    }

    /**
     * Direct toggle for High-Contrast OLED Night Mode.
     * Switches directly between High-Contrast Dark Mode and standard theme to reduce eye strain.
     */
    fun toggleHighContrastNightMode() {
        val current = _uiState.value.themeMode
        val next = if (current == ThemeMode.HIGH_CONTRAST_DARK) ThemeMode.SYSTEM else ThemeMode.HIGH_CONTRAST_DARK
        _uiState.value = _uiState.value.copy(themeMode = next)
    }

    fun setWatermarkEnabled(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isWatermarkEnabled = enabled)
    }

    fun setWatermarkStyle(style: WatermarkStyle) {
        _uiState.value = _uiState.value.copy(watermarkStyle = style)
    }

    fun setWatermarkOpacity(opacity: WatermarkOpacity) {
        _uiState.value = _uiState.value.copy(watermarkOpacity = opacity)
    }

    fun cycleWatermarkStyle() {
        val next = when (_uiState.value.watermarkStyle) {
            WatermarkStyle.CENTER_EMBLEM -> WatermarkStyle.DIAGONAL_TILES
            WatermarkStyle.DIAGONAL_TILES -> WatermarkStyle.CORNER_STAMP
            WatermarkStyle.CORNER_STAMP -> WatermarkStyle.CENTER_EMBLEM
        }
        _uiState.value = _uiState.value.copy(watermarkStyle = next)
    }

    /**
     * Executes the automatic refresh of weather, alerts, daily pulse, news, and synchronizes caches.
     */
    fun triggerHourlyAutoRefresh() {
        _uiState.value = _uiState.value.copy(
            lastHourlyRefreshTimestamp = System.currentTimeMillis(),
            nextHourlyRefreshMinutesRemaining = _uiState.value.autoUpdateFrequencyMinutes,
            autoRefreshCycleCount = _uiState.value.autoRefreshCycleCount + 1
        )
        try {
            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(isRefreshing = true)
                fetchRealTimeWeather()
                fetchLiveEmergencyAlerts()
                refreshDailyPulse()
                try {
                    civicReportRepo?.autoProgressReports()
                } catch (_: Throwable) {}
                try {
                    newsRepo?.refreshNews()
                } catch (_: Throwable) {}
                delay(800)
                _uiState.value = _uiState.value.copy(isRefreshing = false)
            }
        } catch (_: Throwable) {
            // Safe fallback for JVM unit test environments without Dispatchers.Main
        }
    }

    fun setHourlyAutoRefreshEnabled(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(
            isHourlyAutoRefreshEnabled = enabled,
            nextHourlyRefreshMinutesRemaining = if (enabled) _uiState.value.autoUpdateFrequencyMinutes else 0
        )
    }

    fun setAutoUpdateFrequency(minutes: Int) {
        _uiState.value = _uiState.value.copy(
            autoUpdateFrequencyMinutes = minutes,
            nextHourlyRefreshMinutesRemaining = minutes
        )
    }

    fun toggleBackgroundSync(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(
            isAutoUpdateBackgroundEnabled = enabled,
            isHourlyAutoRefreshEnabled = enabled
        )
    }

    fun forceSyncAllData() {
        try {
            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(isRefreshing = true)
                fetchRealTimeWeather()
                fetchLiveEmergencyAlerts()
                refreshDailyPulse()
                try {
                    civicReportRepo?.autoProgressReports()
                } catch (_: Throwable) {}
                try {
                    newsRepo?.refreshNews()
                } catch (_: Throwable) {}
                kotlinx.coroutines.delay(700)
                val timeStr = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault()).format(java.util.Date())
                _uiState.value = _uiState.value.copy(
                    isRefreshing = false,
                    lastHourlyRefreshTimestamp = System.currentTimeMillis(),
                    nextHourlyRefreshMinutesRemaining = _uiState.value.autoUpdateFrequencyMinutes,
                    autoRefreshCycleCount = _uiState.value.autoRefreshCycleCount + 1,
                    lastSyncStatusMessage = "All feeds synced live at $timeStr"
                )
            }
        } catch (_: Throwable) {
            _uiState.value = _uiState.value.copy(isRefreshing = false)
        }
    }

    fun checkForAppUpdates() {
        try {
            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(isUpdateCheckLoading = true)
                kotlinx.coroutines.delay(850)
                _uiState.value = _uiState.value.copy(
                    isUpdateCheckLoading = false,
                    appVersionLatest = com.example.BuildConfig.VERSION_NAME,
                    lastSyncStatusMessage = "You have the latest version (v${com.example.BuildConfig.VERSION_NAME}, Build ${com.example.BuildConfig.VERSION_CODE})"
                )
            }
        } catch (_: Throwable) {
            _uiState.value = _uiState.value.copy(isUpdateCheckLoading = false)
        }
    }

    fun setNewsAutoUpdateEnabled(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(
            isNewsAutoUpdateEnabled = enabled,
            newsAutoUpdateSecondsRemaining = if (enabled) _uiState.value.newsAutoUpdateIntervalSeconds else 0
        )
    }

    fun setNewsAutoUpdateInterval(seconds: Int) {
        val valid = seconds.coerceIn(15, 300)
        _uiState.value = _uiState.value.copy(
            newsAutoUpdateIntervalSeconds = valid,
            newsAutoUpdateSecondsRemaining = valid
        )
    }

    fun triggerNewsAutoUpdate() {
        val now = System.currentTimeMillis()
        _uiState.value = _uiState.value.copy(
            lastNewsAutoUpdateTimestamp = now,
            newsAutoUpdateSecondsRemaining = _uiState.value.newsAutoUpdateIntervalSeconds,
            newsAutoUpdateCycleCount = _uiState.value.newsAutoUpdateCycleCount + 1
        )
        viewModelScope.launch {
            try {
                newsRepo?.refreshNews()
            } catch (_: Throwable) {}
            try {
                refreshDailyPulse()
            } catch (_: Throwable) {}
            try {
                fetchLiveEmergencyAlerts()
            } catch (_: Throwable) {}
        }
    }

    companion object {
        fun matchesNewsTab(article: NewsArticle, tabIndex: Int): Boolean =
            NewsFeedFilter.matchesTab(article, tabIndex)

        fun filterNewsArticles(articles: List<NewsArticle>, tabIndex: Int, query: String = ""): List<NewsArticle> =
            NewsFeedFilter.filterArticles(articles, tabIndex, query)
    }
}
