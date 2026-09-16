package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.remote.GeminiGroundingService
import com.example.data.remote.GroundingResponse
import com.example.data.remote.GroundingToolMode
import com.example.data.repository.BalasoreRepository
import com.example.data.repository.DefaultData
import com.example.data.repository.UniqueFeaturesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BalasoreUiState(
    val selectedTab: Int = 0,
    val language: AppLanguage = AppLanguage.ENGLISH,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val hotspots: List<Hotspot> = BalasoreRepository.hotspots,
    val selectedHotspotCategory: String = "All",
    val hotspotSearchQuery: String = "",
    val newsArticles: List<NewsArticle> = BalasoreRepository.newsArticles,
    val selectedNewsCategory: String = "All",
    val weather: WeatherInfo = BalasoreRepository.weather,
    val emergencyContacts: List<EmergencyContact> = BalasoreRepository.emergencyContacts,
    val transitList: List<TransitSchedule> = BalasoreRepository.transitSchedules,
    // 6 Unique Features
    val tidalClock: TidalClockData = BalasoreRepository.tidalClock,
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
    val groundingState: GroundingState = GroundingState()
)

enum class UniqueFeatureSheetType {
    HORSESHOE_CRAB,
    DEFENSE_TRAIL,
    REMUNA_PRASAD_ARTISANS,
    CYCLONE_RESILIENCE,
    HARBOR_CATCH_RATES,
    ELEPHANT_PASSPORT
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

    private val _uiState = MutableStateFlow(BalasoreUiState())
    val uiState: StateFlow<BalasoreUiState> = _uiState.asStateFlow()

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
            delay(1200) // Simulates network synchronization with local caching
            _uiState.value = _uiState.value.copy(isRefreshing = false)
        }
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
}
