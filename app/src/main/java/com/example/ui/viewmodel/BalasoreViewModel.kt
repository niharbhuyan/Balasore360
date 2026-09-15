package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AppLanguage
import com.example.data.model.CycloneShelter
import com.example.data.model.DailyBalasoreIdiom
import com.example.data.model.DrdoAdvisory
import com.example.data.model.EmergencyContact
import com.example.data.model.Hotspot
import com.example.data.model.LiteraryTrailPoint
import com.example.data.model.NewsArticle
import com.example.data.model.RiverGauge
import com.example.data.model.SeafoodCatch
import com.example.data.model.TempleRitualInfo
import com.example.data.model.TidalClockData
import com.example.data.model.TransitSchedule
import com.example.data.model.WeatherInfo
import com.example.data.repository.BalasoreRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BalasoreUiState(
    val selectedTab: Int = 0,
    val language: AppLanguage = AppLanguage.ENGLISH,
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
    val isRefreshing: Boolean = false,
    val bookmarkedIds: Set<String> = emptySet(),
    val favoriteHotspotIds: Set<String> = emptySet()
)

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
}
