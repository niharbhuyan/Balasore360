package com.example

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.ContactPhone
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.fcm.BalasoreNotificationHelper
import com.example.data.fcm.FcmManager
import com.example.ui.components.AppHeader
import com.example.ui.components.MainScrollableTabRow
import com.example.ui.screens.AuthBottomSheet
import com.example.ui.screens.EssentialsScreen
import com.example.ui.screens.LocalAlertsBottomSheet
import com.example.ui.screens.NewsScreen
import com.example.ui.screens.OfflineStatusBottomSheet
import com.example.ui.screens.TourismScreen
import com.example.ui.screens.WeatherScreen
import com.example.ui.theme.BentoBluePill
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoCanvas
import com.example.ui.theme.BentoCardWhite
import com.example.ui.theme.BentoPrimaryBlue
import com.example.ui.theme.BentoSlate400
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.BalasoreViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: BalasoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        BalasoreNotificationHelper.createNotificationChannels(this)
        FcmManager.initialize(this.applicationContext)
        handleNotificationIntent(intent)
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            MyApplicationTheme(themeMode = uiState.themeMode) {
                BalasoreApp(viewModel = viewModel)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleNotificationIntent(intent)
    }

    private fun handleNotificationIntent(intent: Intent?) {
        if (intent == null) return
        val targetTab = intent.getStringExtra(BalasoreNotificationHelper.EXTRA_TARGET_TAB)
            ?: intent.getStringExtra("target_tab")
            ?: intent.getStringExtra("type")

        when {
            targetTab?.contains("WEATHER", ignoreCase = true) == true -> {
                viewModel.selectTab(AppTab.WEATHER)
                if (intent.hasExtra("alert_title")) {
                    viewModel.openAlertsSheet()
                }
            }
            targetTab?.contains("NEWS", ignoreCase = true) == true ||
            targetTab?.contains("BREAKING", ignoreCase = true) == true -> {
                viewModel.selectTab(AppTab.NEWS)
                val articleId = intent.getStringExtra(BalasoreNotificationHelper.EXTRA_ARTICLE_ID)
                    ?: intent.getStringExtra("article_id")
                if (!articleId.isNullOrBlank()) {
                    val idLong = articleId.toLongOrNull()
                    if (idLong != null) {
                        viewModel.selectArticleById(idLong)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalasoreApp(viewModel: BalasoreViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val weather by viewModel.weatherState.collectAsStateWithLifecycle()
    val dailyForecasts by viewModel.dailyForecasts.collectAsStateWithLifecycle()
    val filteredDailyForecasts by viewModel.filteredDailyForecasts.collectAsStateWithLifecycle()
    val hotspots by viewModel.filteredHotspots.collectAsStateWithLifecycle()
    val news by viewModel.filteredNews.collectAsStateWithLifecycle()
    val weatherMatchesCount by viewModel.weatherMatchesCount.collectAsStateWithLifecycle()
    val weatherAlertsEnabled by viewModel.weatherAlertsEnabled.collectAsStateWithLifecycle()
    val breakingNewsEnabled by viewModel.breakingNewsEnabled.collectAsStateWithLifecycle()
    val fcmToken by viewModel.fcmToken.collectAsStateWithLifecycle()
    val timeSensitiveAlerts by viewModel.timeSensitiveAlerts.collectAsStateWithLifecycle()

    val breakingNewsList = remember(news) {
        news.filter {
            it.category.equals("Emergency", ignoreCase = true) ||
                    it.title.contains("Breaking", ignoreCase = true) ||
                    it.title.contains("Cyclone", ignoreCase = true)
        }
    }
    val hasActiveWeatherAlert = weather != null && weather?.alertLevel != "SAFE" && weather?.alertLevel != "NORMAL"
    val hasActiveAlerts = hasActiveWeatherAlert || breakingNewsList.isNotEmpty()

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.showUserNotice("Notifications enabled for Balasore alerts")
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    LaunchedEffect(uiState.userNotice) {
        val notice = uiState.userNotice
        if (notice != null) {
            snackbarHostState.showSnackbar(notice)
            viewModel.dismissNotice()
        }
    }

    Scaffold(
        containerColor = BentoCanvas,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                AppHeader(
                    weather = weather,
                    isRefreshing = uiState.isRefreshing,
                    onRefresh = { viewModel.refreshData() },
                    currentUser = currentUser,
                    onProfileClick = { viewModel.openAuthSheet() },
                    isOnline = uiState.isOnline,
                    isSyncing = uiState.isSyncing,
                    lastSyncTime = uiState.lastSyncTime,
                    onOfflineStatusClick = { viewModel.openOfflineSheet() },
                    searchQuery = uiState.searchQuery,
                    onSearchQueryChange = { viewModel.setSearchQuery(it) },
                    selectedTab = uiState.selectedTab,
                    hotspotsCount = hotspots.size,
                    newsCount = news.size,
                    weatherMatchesCount = weatherMatchesCount,
                    onSelectTab = { viewModel.selectTab(it) },
                    onToggleTheme = { viewModel.toggleTheme() },
                    onAlertsClick = { viewModel.openAlertsSheet() },
                    hasActiveAlerts = hasActiveAlerts
                )

                // Horizontal scrollable tab row component to toggle between News, Weather, and Tourism feeds
                MainScrollableTabRow(
                    selectedTab = uiState.selectedTab,
                    onSelectTab = { viewModel.selectTab(it) },
                    hotspotsCount = hotspots.size,
                    newsCount = news.size,
                    weatherTemp = weather?.temperature?.toInt()?.let { "$it°C" }
                )
            }
        },
        bottomBar = {
            Surface(
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = BentoCardWhite,
                shadowElevation = 8.dp,
                border = BorderStroke(1.dp, BentoBorder),
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("bottom_navigation_bar")
            ) {
                NavigationBar(
                    containerColor = BentoCardWhite,
                    tonalElevation = 0.dp,
                    modifier = Modifier.height(72.dp)
                ) {
                    AppTab.values().forEach { tab ->
                        val isSelected = uiState.selectedTab == tab
                        val (iconFilled, iconOutlined) = when (tab) {
                            AppTab.HOTSPOTS -> Pair(Icons.Default.Explore, Icons.Outlined.Explore)
                            AppTab.NEWS -> Pair(Icons.AutoMirrored.Filled.Article, Icons.AutoMirrored.Outlined.Article)
                            AppTab.WEATHER -> Pair(Icons.Default.Cloud, Icons.Outlined.Cloud)
                            AppTab.ESSENTIALS -> Pair(Icons.Default.ContactPhone, Icons.Outlined.ContactPhone)
                        }

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.selectTab(tab) },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) iconFilled else iconOutlined,
                                    contentDescription = tab.title
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title.uppercase(),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                        letterSpacing = 0.6.sp
                                    )
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BentoPrimaryBlue,
                                selectedTextColor = BentoPrimaryBlue,
                                indicatorColor = BentoBluePill,
                                unselectedIconColor = BentoSlate400,
                                unselectedTextColor = BentoSlate400
                            ),
                            modifier = Modifier.testTag("nav_item_${tab.name.lowercase()}")
                        )
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.selectedTab) {
                AppTab.HOTSPOTS -> {
                    PullToRefreshBox(
                        isRefreshing = uiState.isRefreshing,
                        onRefresh = { viewModel.refreshData() },
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("main_pull_to_refresh")
                    ) {
                        TourismScreen(
                            hotspots = hotspots,
                            weather = weather,
                            selectedCategory = uiState.hotspotCategory,
                            selectedHotspot = uiState.selectedHotspot,
                            isMapMode = uiState.isMapMode,
                            currentUser = currentUser,
                            onToggleMapMode = { viewModel.toggleMapMode() },
                            onOpenAuth = { viewModel.openAuthSheet() },
                            getReviewsForHotspot = { id -> viewModel.getReviewsForTarget("HOTSPOT", id) },
                            onSubmitHotspotReview = { id, name, rating, comment, guestName ->
                                viewModel.submitReview("HOTSPOT", id, name, rating, comment, guestName)
                            },
                            onCategorySelect = { viewModel.setHotspotCategory(it) },
                            onHotspotSelect = { viewModel.selectHotspot(it) },
                            onToggleFavorite = { viewModel.toggleFavorite(it) },
                            searchQuery = uiState.searchQuery,
                            onSearchQueryChange = { viewModel.setSearchQuery(it) }
                        )
                    }
                }
                AppTab.NEWS -> {
                    NewsScreen(
                        articles = news,
                        selectedCategory = uiState.newsCategory,
                        searchQuery = uiState.searchQuery,
                        selectedArticle = uiState.selectedArticle,
                        currentUser = currentUser,
                        isRefreshing = uiState.isRefreshing,
                        onRefresh = { viewModel.refreshNewsFeed() },
                        onOpenAuth = { viewModel.openAuthSheet() },
                        getReviewsForArticle = { id -> viewModel.getReviewsForTarget("NEWS", id) },
                        onSubmitArticleReview = { id, title, rating, comment, guestName ->
                            viewModel.submitReview("NEWS", id, title, rating, comment, guestName)
                        },
                        onCategorySelect = { viewModel.setNewsCategory(it) },
                        onSearchQueryChange = { viewModel.setSearchQuery(it) },
                        onArticleSelect = { viewModel.selectArticle(it) },
                        onToggleBookmark = { viewModel.toggleBookmark(it) }
                    )
                }
                AppTab.WEATHER -> {
                    WeatherScreen(
                        weather = weather,
                        dailyForecasts = filteredDailyForecasts,
                        timeSensitiveAlerts = timeSensitiveAlerts,
                        currentUser = currentUser,
                        isRefreshing = uiState.isRefreshing,
                        onRefresh = { viewModel.refreshWeatherFeed() },
                        onOpenAuth = { viewModel.openAuthSheet() },
                        getReviewsForWeather = { id -> viewModel.getReviewsForTarget("WEATHER", id) },
                        onSubmitWeatherReview = { id, title, rating, comment, guestName ->
                            viewModel.submitReview("WEATHER", id, title, rating, comment, guestName)
                        },
                        searchQuery = uiState.searchQuery,
                        onClearSearch = { viewModel.clearSearchQuery() },
                        onSearchQueryChange = { viewModel.setSearchQuery(it) }
                    )
                }
                AppTab.ESSENTIALS -> {
                    EssentialsScreen(
                        weatherAlertsEnabled = weatherAlertsEnabled,
                        breakingNewsEnabled = breakingNewsEnabled,
                        onToggleWeatherAlerts = { viewModel.setWeatherAlertsOptIn(it) },
                        onToggleBreakingNews = { viewModel.setBreakingNewsOptIn(it) },
                        onOpenAlertCenter = { viewModel.openAlertsSheet() },
                        onSimulateWeatherAlert = { viewModel.simulateWeatherAlertPush() },
                        onSimulateBreakingNews = { viewModel.simulateBreakingNewsPush() }
                    )
                }
            }
        }
    }

    // Full User Authentication, Profile Management & Avatar Upload Bottom Sheet
    AuthBottomSheet(
        isOpen = uiState.isAuthSheetOpen,
        authMode = uiState.authMode,
        currentUser = currentUser,
        errorMessage = uiState.authError,
        successMessage = uiState.authSuccessMessage,
        onClose = { viewModel.closeAuthSheet() },
        onSetMode = { viewModel.setAuthMode(it) },
        onLogin = { email, pass -> viewModel.login(email, pass) },
        onSignUp = { name, email, pass, phone, locality, answer ->
            viewModel.signUp(name, email, pass, phone, locality, answer)
        },
        onResetPassword = { email, answer, newPass ->
            viewModel.resetPassword(email, answer, newPass)
        },
        onUpdateProfile = { name, phone, locality, bio ->
            viewModel.updateProfile(name, phone, locality, bio)
        },
        onUpdateAvatar = { avatarUri ->
            viewModel.updateAvatar(avatarUri)
        },
        onLogout = { viewModel.logout() }
    )

    // Offline & WorkManager Background Cache Sync Sheet
    OfflineStatusBottomSheet(
        isOpen = uiState.isOfflineSheetOpen,
        isOnline = uiState.isOnline,
        isSyncing = uiState.isSyncing,
        lastSyncTime = uiState.lastSyncTime,
        newsCount = news.size,
        hotspotsCount = hotspots.size,
        hasWeatherCache = weather != null,
        forecastCount = dailyForecasts.size,
        onClose = { viewModel.closeOfflineSheet() },
        onSyncNow = { viewModel.triggerManualSync() }
    )

    // Real-time Push & FCM Alert Center Bottom Sheet
    LocalAlertsBottomSheet(
        isOpen = uiState.isAlertsSheetOpen,
        weather = weather,
        breakingNews = breakingNewsList,
        weatherAlertsEnabled = weatherAlertsEnabled,
        breakingNewsEnabled = breakingNewsEnabled,
        fcmToken = fcmToken,
        onClose = { viewModel.closeAlertsSheet() },
        onToggleWeatherAlerts = { viewModel.setWeatherAlertsOptIn(it) },
        onToggleBreakingNews = { viewModel.setBreakingNewsOptIn(it) },
        onSimulateWeatherAlert = { viewModel.simulateWeatherAlertPush() },
        onSimulateBreakingNews = { viewModel.simulateBreakingNewsPush() },
        onViewWeatherDetails = { viewModel.selectTab(AppTab.WEATHER) },
        onReadArticle = { article ->
            viewModel.selectTab(AppTab.NEWS)
            viewModel.selectArticle(article)
        }
    )
}
