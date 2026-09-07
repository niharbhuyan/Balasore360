package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.example.data.fcm.FcmManager
import com.example.data.local.CacheSyncMetadataEntity
import com.example.data.local.DailyForecastEntity
import com.example.data.local.HotspotEntity
import com.example.data.local.NewsArticleEntity
import com.example.data.local.ReviewEntity
import com.example.data.local.UserEntity
import com.example.data.local.WeatherCacheEntity
import com.example.data.repository.BalasoreRepository
import com.example.data.sync.NetworkMonitor
import com.example.data.sync.SyncManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab(val title: String) {
    HOTSPOTS("Tourism"),
    NEWS("Local News"),
    WEATHER("Weather & Alerts"),
    ESSENTIALS("Directory & Info")
}

enum class AuthMode {
    PROFILE,
    LOGIN,
    SIGNUP,
    FORGOT_PASSWORD,
    EDIT_PROFILE
}

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

data class UiState(
    val isRefreshing: Boolean = false,
    val isSyncing: Boolean = false,
    val isOnline: Boolean = true,
    val lastSyncTime: Long = 0L,
    val isOfflineSheetOpen: Boolean = false,
    val isAlertsSheetOpen: Boolean = false,
    val selectedTab: AppTab = AppTab.HOTSPOTS,
    val newsCategory: String = "All",
    val hotspotCategory: String = "All",
    val searchQuery: String = "",
    val newsSearchQuery: String = "",
    val selectedHotspot: HotspotEntity? = null,
    val selectedArticle: NewsArticleEntity? = null,
    val userNotice: String? = null,
    val isMapMode: Boolean = false,
    val selectedMapHotspot: HotspotEntity? = null,
    val isAuthSheetOpen: Boolean = false,
    val authMode: AuthMode = AuthMode.PROFILE,
    val authError: String? = null,
    val authSuccessMessage: String? = null,
    val themeMode: ThemeMode = ThemeMode.SYSTEM
)

class BalasoreViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BalasoreRepository.getInstance(application)
    private val networkMonitor = NetworkMonitor(application)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val currentUser: StateFlow<UserEntity?> = repository.currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val rawHotspots: StateFlow<List<HotspotEntity>> = repository.allHotspots
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val rawNews: StateFlow<List<NewsArticleEntity>> = repository.allNews
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val weatherState: StateFlow<WeatherCacheEntity?> = repository.weatherCache
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val dailyForecasts: StateFlow<List<DailyForecastEntity>> = repository.dailyForecasts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cacheMetadata: StateFlow<List<CacheSyncMetadataEntity>> = repository.cacheMetadataList
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allReviews: StateFlow<List<ReviewEntity>> = repository.allReviews
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Real-time Push & FCM Notification State
    val weatherAlertsEnabled: StateFlow<Boolean> = FcmManager.weatherAlertsEnabled
    val breakingNewsEnabled: StateFlow<Boolean> = FcmManager.breakingNewsEnabled
    val fcmToken: StateFlow<String?> = FcmManager.fcmToken

    // Filtered Hotspots based on category and search query
    val filteredHotspots: StateFlow<List<HotspotEntity>> = combine(rawHotspots, _uiState) { list, state ->
        val query = state.searchQuery.trim()
        list.filter { hotspot ->
            val matchesCategory = when (state.hotspotCategory) {
                "All" -> true
                "Favorites" -> hotspot.isFavorite
                else -> hotspot.category.equals(state.hotspotCategory, ignoreCase = true)
            }
            val matchesSearch = query.isBlank() ||
                    hotspot.name.contains(query, ignoreCase = true) ||
                    hotspot.odiaName.contains(query, ignoreCase = true) ||
                    hotspot.shortDescription.contains(query, ignoreCase = true) ||
                    hotspot.fullDescription.contains(query, ignoreCase = true) ||
                    hotspot.highlights.contains(query, ignoreCase = true) ||
                    hotspot.specialty.contains(query, ignoreCase = true) ||
                    hotspot.category.contains(query, ignoreCase = true) ||
                    hotspot.localTip.contains(query, ignoreCase = true)

            matchesCategory && matchesSearch
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered News based on category and search query
    val filteredNews: StateFlow<List<NewsArticleEntity>> = combine(rawNews, _uiState) { list, state ->
        val query = if (state.searchQuery.isNotBlank()) state.searchQuery.trim() else state.newsSearchQuery.trim()
        list.filter { article ->
            val matchesCategory = state.newsCategory == "All" ||
                    (state.newsCategory == "Saved" && article.isBookmarked) ||
                    article.category.equals(state.newsCategory, ignoreCase = true)

            val matchesSearch = query.isBlank() ||
                    article.title.contains(query, ignoreCase = true) ||
                    article.summary.contains(query, ignoreCase = true) ||
                    article.content.contains(query, ignoreCase = true) ||
                    article.category.contains(query, ignoreCase = true) ||
                    article.source.contains(query, ignoreCase = true)

            matchesCategory && matchesSearch
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Load saved theme preference
        try {
            val prefs = getApplication<Application>().getSharedPreferences("balasore_app_prefs", Context.MODE_PRIVATE)
            val savedTheme = prefs.getString("theme_mode", ThemeMode.SYSTEM.name) ?: ThemeMode.SYSTEM.name
            val initialTheme = try { ThemeMode.valueOf(savedTheme) } catch (_: Exception) { ThemeMode.SYSTEM }
            _uiState.value = _uiState.value.copy(themeMode = initialTheme)
        } catch (_: Exception) {}

        viewModelScope.launch {
            repository.initializeIfNeeded()
            val lastSync = repository.getLastSyncTimestamp()
            _uiState.value = _uiState.value.copy(lastSyncTime = lastSync)
            
            // Schedule periodic background sync using WorkManager (caches news, weather, tourism)
            SyncManager.schedulePeriodicSync(getApplication())
            
            // Trigger initial sync to ensure Room database is fresh
            refreshData(silent = true)
        }

        // Monitor Network State
        viewModelScope.launch {
            networkMonitor.isOnline.collect { online ->
                val wasOffline = !_uiState.value.isOnline && online
                _uiState.value = _uiState.value.copy(isOnline = online)
                if (wasOffline) {
                    // When device comes back online, immediately run background sync
                    SyncManager.triggerImmediateSync(getApplication())
                    refreshData(silent = true)
                }
            }
        }

        // Observe WorkManager Background Sync Status
        viewModelScope.launch {
            SyncManager.observeImmediateSync(getApplication()).collect { workInfo ->
                if (workInfo != null) {
                    val isRunning = workInfo.state == WorkInfo.State.RUNNING || workInfo.state == WorkInfo.State.ENQUEUED
                    val lastSync = repository.getLastSyncTimestamp()
                    _uiState.value = _uiState.value.copy(
                        isSyncing = isRunning,
                        lastSyncTime = if (lastSync > 0) lastSync else _uiState.value.lastSyncTime
                    )
                    if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                        _uiState.value = _uiState.value.copy(
                            isSyncing = false,
                            lastSyncTime = repository.getLastSyncTimestamp()
                        )
                    }
                }
            }
        }
    }

    fun selectTab(tab: AppTab) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
    }

    fun setHotspotCategory(category: String) {
        _uiState.value = _uiState.value.copy(hotspotCategory = category)
    }

    fun setNewsCategory(category: String) {
        _uiState.value = _uiState.value.copy(newsCategory = category)
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query, newsSearchQuery = query)
    }

    fun clearSearchQuery() {
        _uiState.value = _uiState.value.copy(searchQuery = "", newsSearchQuery = "")
    }

    fun setNewsSearchQuery(query: String) {
        setSearchQuery(query)
    }

    fun selectHotspot(hotspot: HotspotEntity?) {
        _uiState.value = _uiState.value.copy(selectedHotspot = hotspot)
    }

    fun selectArticle(article: NewsArticleEntity?) {
        _uiState.value = _uiState.value.copy(selectedArticle = article)
    }

    fun dismissNotice() {
        _uiState.value = _uiState.value.copy(userNotice = null)
    }

    fun openOfflineSheet() {
        _uiState.value = _uiState.value.copy(isOfflineSheetOpen = true)
    }

    fun closeOfflineSheet() {
        _uiState.value = _uiState.value.copy(isOfflineSheetOpen = false)
    }

    fun openAlertsSheet() {
        _uiState.value = _uiState.value.copy(isAlertsSheetOpen = true)
    }

    fun closeAlertsSheet() {
        _uiState.value = _uiState.value.copy(isAlertsSheetOpen = false)
    }

    fun showUserNotice(notice: String) {
        _uiState.value = _uiState.value.copy(userNotice = notice)
    }

    fun setWeatherAlertsOptIn(enabled: Boolean) {
        FcmManager.setWeatherAlertsEnabled(getApplication(), enabled)
        val msg = if (enabled) {
            "Subscribed to Balasore Severe Weather & Tide Alerts"
        } else {
            "Unsubscribed from Weather Alerts"
        }
        showUserNotice(msg)
    }

    fun setBreakingNewsOptIn(enabled: Boolean) {
        FcmManager.setBreakingNewsEnabled(getApplication(), enabled)
        val msg = if (enabled) {
            "Subscribed to Balasore Breaking News & Civic Wire"
        } else {
            "Unsubscribed from Breaking News Alerts"
        }
        showUserNotice(msg)
    }

    fun simulateWeatherAlertPush() {
        FcmManager.simulateWeatherAlertPush(getApplication())
        showUserNotice("Urgent coastal storm alert received & cached")
    }

    fun simulateBreakingNewsPush() {
        FcmManager.simulateBreakingNewsPush(getApplication())
        showUserNotice("Breaking news bulletin received & cached")
    }

    fun setThemeMode(mode: ThemeMode) {
        _uiState.value = _uiState.value.copy(themeMode = mode)
        try {
            val prefs = getApplication<Application>().getSharedPreferences("balasore_app_prefs", Context.MODE_PRIVATE)
            prefs.edit().putString("theme_mode", mode.name).apply()
        } catch (_: Exception) {}
    }

    fun toggleTheme() {
        val current = _uiState.value.themeMode
        val next = when (current) {
            ThemeMode.LIGHT -> ThemeMode.DARK
            ThemeMode.DARK -> ThemeMode.LIGHT
            ThemeMode.SYSTEM -> ThemeMode.DARK
        }
        setThemeMode(next)
    }

    fun triggerManualSync() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true, isSyncing = true)
            SyncManager.triggerImmediateSync(getApplication())
            val result = repository.syncAllData(forceNetwork = true)
            _uiState.value = _uiState.value.copy(
                isRefreshing = false,
                isSyncing = false,
                lastSyncTime = result.timestamp,
                userNotice = result.message
            )
        }
    }

    fun toggleFavorite(hotspot: HotspotEntity) {
        viewModelScope.launch {
            repository.toggleFavoriteHotspot(hotspot.id, hotspot.isFavorite)
        }
    }

    fun toggleBookmark(article: NewsArticleEntity) {
        viewModelScope.launch {
            repository.toggleBookmark(article.id, article.isBookmarked)
        }
    }

    fun refreshData(silent: Boolean = false) {
        viewModelScope.launch {
            if (!silent) {
                _uiState.value = _uiState.value.copy(isRefreshing = true)
            }
            try {
                // Trigger background worker sync
                SyncManager.triggerImmediateSync(getApplication())
                val syncResult = repository.syncAllData()
                _uiState.value = _uiState.value.copy(
                    isRefreshing = false,
                    lastSyncTime = syncResult.timestamp,
                    userNotice = if (!silent) syncResult.message else null
                )
            } catch (_: Exception) {
                if (!silent) {
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        userNotice = "Offline mode: Showing cached Balasore data from Room."
                    )
                }
            } finally {
                if (!silent) {
                    _uiState.value = _uiState.value.copy(isRefreshing = false)
                }
            }
        }
    }

    // --- Interactive Map State Controls ---
    fun toggleMapMode(enabled: Boolean? = null) {
        val next = enabled ?: !_uiState.value.isMapMode
        _uiState.value = _uiState.value.copy(isMapMode = next)
    }

    fun selectMapHotspot(hotspot: HotspotEntity?) {
        _uiState.value = _uiState.value.copy(selectedMapHotspot = hotspot)
    }

    // --- Auth Sheet Controls ---
    fun openAuthSheet(mode: AuthMode = AuthMode.PROFILE) {
        _uiState.value = _uiState.value.copy(
            isAuthSheetOpen = true,
            authMode = mode,
            authError = null,
            authSuccessMessage = null
        )
    }

    fun setAuthMode(mode: AuthMode) {
        _uiState.value = _uiState.value.copy(
            authMode = mode,
            authError = null,
            authSuccessMessage = null
        )
    }

    fun closeAuthSheet() {
        _uiState.value = _uiState.value.copy(
            isAuthSheetOpen = false,
            authError = null,
            authSuccessMessage = null
        )
    }

    fun clearAuthMessages() {
        _uiState.value = _uiState.value.copy(authError = null, authSuccessMessage = null)
    }

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(authError = null, authSuccessMessage = null)
            val result = repository.login(email, pass)
            result.onSuccess { user ->
                _uiState.value = _uiState.value.copy(
                    authMode = AuthMode.PROFILE,
                    authSuccessMessage = "Welcome back, ${user.fullName}!"
                )
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(authError = err.message ?: "Login failed")
            }
        }
    }

    fun signUp(
        name: String,
        email: String,
        pass: String,
        phone: String,
        locality: String,
        securityAnswer: String
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(authError = null, authSuccessMessage = null)
            val result = repository.signUp(name, email, pass, phone, locality, securityAnswer)
            result.onSuccess { user ->
                _uiState.value = _uiState.value.copy(
                    authMode = AuthMode.PROFILE,
                    authSuccessMessage = "Account created! Welcome, ${user.fullName}."
                )
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(authError = err.message ?: "Sign up failed")
            }
        }
    }

    fun resetPassword(email: String, securityAnswer: String, newPass: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(authError = null, authSuccessMessage = null)
            val result = repository.resetPassword(email, securityAnswer, newPass)
            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    authMode = AuthMode.LOGIN,
                    authSuccessMessage = "Password reset successfully! Please log in with your new password."
                )
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(authError = err.message ?: "Password reset failed")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _uiState.value = _uiState.value.copy(
                authMode = AuthMode.LOGIN,
                authSuccessMessage = "You have logged out."
            )
        }
    }

    fun updateProfile(fullName: String, phone: String, locality: String, bio: String) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            val updated = user.copy(
                fullName = fullName.ifBlank { user.fullName },
                phoneNumber = phone,
                locality = locality.ifBlank { user.locality },
                bio = bio
            )
            repository.updateUserProfile(updated)
            _uiState.value = _uiState.value.copy(authSuccessMessage = "Profile details updated successfully!")
        }
    }

    fun updateAvatar(avatarUri: String?) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            repository.updateUserAvatar(user.id, avatarUri)
            _uiState.value = _uiState.value.copy(authSuccessMessage = "Profile picture updated!")
        }
    }

    // --- Feedback & Reviews ---
    fun getReviewsForTarget(targetType: String, targetId: String): Flow<List<ReviewEntity>> {
        return repository.getReviewsForTarget(targetType, targetId)
    }

    fun getAverageRating(targetType: String, targetId: String): Flow<Double?> {
        return repository.getAverageRating(targetType, targetId)
    }

    fun getReviewCount(targetType: String, targetId: String): Flow<Int> {
        return repository.getReviewCount(targetType, targetId)
    }

    fun submitReview(
        targetType: String,
        targetId: String,
        targetTitle: String,
        rating: Int,
        comment: String,
        guestName: String? = null,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            val result = repository.submitReview(
                targetType = targetType,
                targetId = targetId,
                targetTitle = targetTitle,
                rating = rating,
                comment = comment,
                authorName = guestName
            )
            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    userNotice = "Thank you! Your rating and feedback were submitted."
                )
                onSuccess()
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(
                    userNotice = err.message ?: "Failed to submit review"
                )
            }
        }
    }
}
