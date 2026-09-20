package com.example.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.ChandipurTideEntity
import com.example.data.local.ChandipurTideDao
import com.example.data.local.ItineraryItemEntity
import com.example.data.model.*
import com.example.data.remote.BalasoreApiService
import com.example.data.remote.WeatherApiService
import com.example.data.remote.EmergencyAlertDto
import com.example.data.remote.GeminiGroundingService
import com.example.data.remote.GroundingResponse
import com.example.data.remote.GroundingToolMode
import com.example.data.repository.BalasoreRepository
import com.example.data.repository.DefaultData
import com.example.data.repository.ItineraryRepository
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
import java.util.Date
import java.util.Locale

data class BalasoreUiState(
    val selectedTab: Int = 0,
    val language: AppLanguage = AppLanguage.ENGLISH,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val hotspots: List<Hotspot> = BalasoreRepository.hotspots,
    val selectedHotspotCategory: String = "All",
    val hotspotSearchQuery: String = "",
    val newsArticles: List<NewsArticle> = BalasoreRepository.newsArticles,
    val selectedNewsCategory: String = "All",
    val newsSearchQuery: String = "",
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
    val isFahrenheit: Boolean = false,
    val selectedForecastDayIndex: Int = 0,
    val activeFeatureSheet: UniqueFeatureSheetType? = null,
    val isRefreshing: Boolean = false,
    val bookmarkedIds: Set<String> = emptySet(),
    val favoriteHotspotIds: Set<String> = emptySet(),
    val groundingState: GroundingState = GroundingState(),
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
    val watermarkOpacity: WatermarkOpacity = WatermarkOpacity.MEDIUM
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
    // Hydrology & Artisan Marketplace Sheets:
    RIVER_FLOOD_TELEMETRY,
    MATI_MANISHA_ARTISANS,
    // Google Maps Explorer (Hotspots & Cyclone Shelters)
    BALASORE_MAP_EXPLORER
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
    private val apiService: BalasoreApiService by lazy { BalasoreApiService.create() }
    private val weatherApiService: WeatherApiService by lazy { WeatherApiService.create() }

    private val _uiState = MutableStateFlow(BalasoreUiState())
    val uiState: StateFlow<BalasoreUiState> = _uiState.asStateFlow()

    init {
        try {
            if (android.os.Looper.getMainLooper() != null) {
                fetchLiveEmergencyAlerts()
                fetchRealTimeWeather()
                // Periodic auto-update ticker for daily data variance (every 60 seconds)
                viewModelScope.launch {
                    while (true) {
                        delay(60_000L)
                        refreshDailyPulse()
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
        if (itineraryRepo != null && tideDao != null) return
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
        } catch (t: Throwable) {
            android.util.Log.e("BalasoreViewModel", "Failed to initialize database: ${t.message}")
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

    fun setHotspotSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(hotspotSearchQuery = query)
    }

    fun setHotspotCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedHotspotCategory = category)
    }

    fun setNewsCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedNewsCategory = category)
    }

    fun setNewsSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(newsSearchQuery = query)
    }

    fun toggleBookmark(articleId: String) {
        val current = _uiState.value.bookmarkedIds
        val updated = if (current.contains(articleId)) current - articleId else current + articleId
        _uiState.value = _uiState.value.copy(bookmarkedIds = updated)
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
            delay(800)
            _uiState.value = _uiState.value.copy(isRefreshing = false)
        }
    }

    /**
     * Fetches real-time meteorological data for Balasore using the Open-Meteo public API
     * and updates the UI state prominently with temperature, humidity, wind, UV index, and conditions.
     */
    fun fetchRealTimeWeather() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isWeatherLoading = true)
            try {
                val response = weatherApiService.getBalasoreForecast(
                    latitude = 21.4934,
                    longitude = 86.9135
                )
                val current = response.current
                val daily = response.daily
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
                        isLiveApi = true
                    )
                    _uiState.value = _uiState.value.copy(
                        weather = updatedWeather,
                        isWeatherLoading = false
                    )
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
        _uiState.value = _uiState.value.copy(
            dailyPulse = com.example.data.daily.DailyUpdateEngine.getDailyPulse()
        )
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
            ThemeMode.DARK -> ThemeMode.SYSTEM
        }
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
}
