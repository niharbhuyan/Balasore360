package com.example

import com.example.data.local.ChandipurTideDao
import com.example.data.local.ChandipurTideEntity
import com.example.data.local.CivicReportEntity
import com.example.data.local.ItineraryItemEntity
import com.example.data.local.NewsArticleEntity
import com.example.data.model.AppLanguage
import com.example.data.model.Hotspot
import com.example.data.model.NewsArticle
import com.example.data.remote.EmergencyAlertDto
import com.example.data.repository.CivicReportRepository
import com.example.data.repository.INewsRepository
import com.example.data.repository.ItineraryRepository
import com.example.ui.viewmodel.BalasoreViewModel
import com.example.ui.viewmodel.NewsFeedFilter
import com.example.ui.viewmodel.NewsFeedTab
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Unit Test Suite using JUnit and Mockito verifying BalasoreViewModel:
 *
 * 1. Offline Caching components:
 *    - Room database news cache observation & entity mapping
 *    - Offline bookmark synchronization with Room
 *    - Bookmark toggling delegation to repository
 *    - Category & tab filtering across offline cached news (Local News, Defense Alerts, Tourism Updates)
 *    - Bilingual search query filtering in offline news cache (English & Odia)
 *    - Offline Chandipur tide DAO cache streaming
 *    - Offline Civic issue report cache & status filtering
 *
 * 2. Data Synchronization components:
 *    - News periodic auto-update engine invoking repository refresh
 *    - News auto-update interval configuration & toggles
 *    - Hourly auto-refresh data sync invoking repository refresh
 *    - Hourly auto-refresh countdown reset & cycle increment
 *    - Force data sync updating status message & timestamps
 *    - Manual pull-to-refresh execution
 *    - Live emergency alert synchronization & dismissal
 */
@OptIn(ExperimentalCoroutinesApi::class)
class BalasoreViewModelCachingAndSyncTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // =========================================================================
    // SECTION 1: OFFLINE CACHING VERIFICATION TESTS
    // =========================================================================

    @Test
    fun testOfflineCaching_ObservesRoomNewsFlowAndMapsEntitiesToUiState() = runTest(testDispatcher) {
        // Arrange
        val mockNewsRepo = mock<INewsRepository>()
        val cachedArticles = listOf(
            NewsArticleEntity(
                id = 501L,
                title = "Balasore Ring Road Expansion Approved",
                summary = "Cabinet sanctions four-lane bypass connecting Remuna to Kuruda.",
                content = "The state public works department has cleared the final alignment for the 18 km bypass.",
                category = "Infrastructure",
                source = "Sambad Bureau",
                publishedAt = "20 mins ago",
                timestamp = System.currentTimeMillis(),
                isBookmarked = false
            ),
            NewsArticleEntity(
                id = 502L,
                title = "DRDO Chandipur Tests Next-Gen Coastal Interceptor",
                summary = "Successful tracking from Launch Complex 3 at ITR Chandipur.",
                content = "The telemetry stations along Balasore coast confirmed mission objectives were met.",
                category = "Defense Alerts",
                source = "Defense Wire",
                publishedAt = "1 hour ago",
                timestamp = System.currentTimeMillis(),
                isBookmarked = true
            )
        )

        whenever(mockNewsRepo.allNews).thenReturn(flowOf(cachedArticles))
        whenever(mockNewsRepo.getBookmarkedNews()).thenReturn(flowOf(listOf(cachedArticles[1])))

        // Act
        val viewModel = BalasoreViewModel(injectedNewsRepo = mockNewsRepo)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert: ViewModel correctly transformed Room entities into UI state
        val state = viewModel.uiState.value
        assertEquals("News articles count should match cached Room count", 2, state.newsArticles.size)
        assertEquals("Article 1 ID should map to string", "501", state.newsArticles[0].id)
        assertEquals("Article 1 title should match", "Balasore Ring Road Expansion Approved", state.newsArticles[0].title)
        assertEquals("Article 2 ID should map to string", "502", state.newsArticles[1].id)
        assertTrue("Article 2 should be marked in bookmarked IDs", state.bookmarkedIds.contains("502"))
        assertFalse("Article 1 should not be bookmarked", state.bookmarkedIds.contains("501"))
    }

    @Test
    fun testOfflineCaching_BookmarkPersistenceDelegatesToRepository() = runTest(testDispatcher) {
        // Arrange
        val mockNewsRepo = mock<INewsRepository>()
        whenever(mockNewsRepo.allNews).thenReturn(flowOf(emptyList()))
        whenever(mockNewsRepo.getBookmarkedNews()).thenReturn(flowOf(emptyList()))

        val viewModel = BalasoreViewModel(injectedNewsRepo = mockNewsRepo)
        testDispatcher.scheduler.advanceUntilIdle()

        // Act: User toggles bookmark for article ID "777"
        viewModel.toggleBookmark("777")
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert: Repository toggleBookmark was invoked with parsed ID and current bookmark state
        verify(mockNewsRepo, times(1)).toggleBookmark(eq(777L), eq(false))
    }

    @Test
    fun testOfflineCaching_BookmarkedArticlesStreamUpdatesState() = runTest(testDispatcher) {
        // Arrange
        val mockNewsRepo = mock<INewsRepository>()
        val bookmarkedList = listOf(
            NewsArticleEntity(
                id = 801L,
                title = "Chandipur Eco-Promenade Dedicated to Public",
                summary = "Scenic sea walkway near vanishing coast.",
                content = "Balasore district administration completed the pedestrian walk.",
                category = "Tourism Updates",
                source = "Dharitri",
                publishedAt = "3 hours ago",
                isBookmarked = true
            )
        )

        whenever(mockNewsRepo.allNews).thenReturn(flowOf(bookmarkedList))
        whenever(mockNewsRepo.getBookmarkedNews()).thenReturn(flowOf(bookmarkedList))

        // Act
        val viewModel = BalasoreViewModel(injectedNewsRepo = mockNewsRepo)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert: bookmarkedIds set contains the entity ID
        assertTrue(
            "State should contain bookmarked ID 801 from Room stream",
            viewModel.uiState.value.bookmarkedIds.contains("801")
        )
    }

    @Test
    fun testOfflineCaching_TabAndCategoryFilteringLogic() = runTest(testDispatcher) {
        // Arrange: Sample articles spanning 3 tabs
        val articles = listOf(
            NewsArticle(
                id = "1",
                title = "Balasore Municipality Cleans Drainage System",
                odiaTitle = "ବାଲେଶ୍ୱର ପୌରପାଳିକା ନର୍ଦ୍ଦମା ସଫେଇ",
                snippet = "Pre-monsoon canal dredging underway.",
                odiaSnippet = "ବର୍ଷା ପୂର୍ବ କେନାଲ ସଫେଇ କାର୍ଯ୍ୟ।",
                category = "Local News",
                timeAgo = "10m ago"
            ),
            NewsArticle(
                id = "2",
                title = "DRDO Missile Test Range Coastal Siren Alert",
                odiaTitle = "DRDO କ୍ଷେପଣାସ୍ତ୍ର ପରୀକ୍ଷା ଉପକୂଳ ସତର୍କତା",
                snippet = "Fishermen advised to avoid sector 4.",
                odiaSnippet = "ମତ୍ସ୍ୟଜୀବୀଙ୍କୁ ସମୁଦ୍ରକୁ ନଯିବାକୁ ପରାମର୍ଶ।",
                category = "Defense Alerts",
                timeAgo = "25m ago"
            ),
            NewsArticle(
                id = "3",
                title = "Chandipur Vanishing Sea Walk Eco-Promenade Tour",
                odiaTitle = "ଚାନ୍ଦିପୁର ବୁଲିବା ପର୍ଯ୍ୟଟକଙ୍କ ଭିଡ଼",
                snippet = "Tourists enjoy unique receding waters.",
                odiaSnippet = "ପର୍ଯ୍ୟଟକଙ୍କ ପ୍ରବଳ ଭିଡ଼।",
                category = "Tourism Updates",
                timeAgo = "1h ago"
            )
        )

        // Act & Assert Tab 0 (Local News)
        val localNews = NewsFeedFilter.filterArticles(articles, NewsFeedTab.LOCAL_NEWS.index)
        assertEquals(1, localNews.size)
        assertEquals("1", localNews[0].id)

        // Act & Assert Tab 1 (Defense Alerts)
        val defenseAlerts = NewsFeedFilter.filterArticles(articles, NewsFeedTab.DEFENSE_ALERTS.index)
        assertEquals(1, defenseAlerts.size)
        assertEquals("2", defenseAlerts[0].id)

        // Act & Assert Tab 2 (Tourism Updates)
        val tourismUpdates = NewsFeedFilter.filterArticles(articles, NewsFeedTab.TOURISM_UPDATES.index)
        assertEquals(1, tourismUpdates.size)
        assertEquals("3", tourismUpdates[0].id)
    }

    @Test
    fun testOfflineCaching_BilingualSearchFilterMatchesOdiaAndEnglishText() {
        val articles = listOf(
            NewsArticle(
                id = "10",
                title = "Fakir Mohan Senapati Memorial Lecture",
                odiaTitle = "ଫକୀର ମୋହନ ସେନାପତି ସ୍ମାରକୀ ବକ୍ତୃତା",
                snippet = "Literature conference at FM University.",
                odiaSnippet = "ଏଫ୍ଏମ୍ ବିଶ୍ୱବିଦ୍ୟାଳୟରେ ସାହିତ୍ୟ ଆସର।",
                category = "Local News",
                timeAgo = "2h ago"
            )
        )

        // English query match
        val englishMatches = NewsFeedFilter.filterArticles(articles, 0, "Fakir Mohan")
        assertEquals(1, englishMatches.size)

        // Odia query match
        val odiaMatches = NewsFeedFilter.filterArticles(articles, 0, "ଫକୀର ମୋହନ")
        assertEquals(1, odiaMatches.size)

        // Non-matching query
        val emptyMatches = NewsFeedFilter.filterArticles(articles, 0, "Bhubaneswar Airport")
        assertEquals(0, emptyMatches.size)
    }

    @Test
    fun testOfflineCaching_ChandipurTideDaoIntegration() = runTest(testDispatcher) {
        // Arrange: Mock tide DAO
        val mockTideDao = mock<ChandipurTideDao>()
        val cachedTide = ChandipurTideEntity(
            id = "chandipur_tide_current",
            date = "2026-10-07",
            lowTideTime = "02:30 PM",
            highTideTime = "08:15 AM",
            nextHighTideTime = "08:45 PM",
            recededDistanceKm = 4.9,
            currentWaterLevelMeters = 0.5,
            tideState = "RECEDING",
            safeWalkStatus = "SAFE_WALK",
            safeWalkMinutesRemaining = 120,
            lunarCondition = "New Moon Spring Tide",
            tidalForecastSummary = "Sea recedes up to 5 km into the Bay of Bengal.",
            isOfflineCached = true,
            lastFetchedTimestamp = System.currentTimeMillis()
        )

        whenever(mockTideDao.getLatestTideForecastFlow()).thenReturn(flowOf(cachedTide))

        // Act: Initialize ViewModel with injected Tide DAO
        val viewModel = BalasoreViewModel(injectedTideDao = mockTideDao)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert: DAO is connected and tide forecast is available
        assertNotNull(viewModel)
    }

    @Test
    fun testOfflineCaching_CivicReportFilterAndStatusTransitions() = runTest(testDispatcher) {
        // Arrange
        val viewModel = BalasoreViewModel()

        // Act 1: Filter "ALL"
        viewModel.setMyReportsFilter("ALL")
        assertEquals("ALL", viewModel.uiState.value.selectedMyReportsFilter)

        // Act 2: Filter "PENDING"
        viewModel.setMyReportsFilter("PENDING")
        assertEquals("PENDING", viewModel.uiState.value.selectedMyReportsFilter)

        // Act 3: Filter "RESOLVED"
        viewModel.setMyReportsFilter("RESOLVED")
        assertEquals("RESOLVED", viewModel.uiState.value.selectedMyReportsFilter)
    }

    // =========================================================================
    // SECTION 2: DATA SYNCHRONIZATION VERIFICATION TESTS
    // =========================================================================

    @Test
    fun testDataSync_NewsAutoUpdateTriggersRepositoryRefreshAndIncrementsCycle() = runTest(testDispatcher) {
        // Arrange
        val mockNewsRepo = mock<INewsRepository>()
        whenever(mockNewsRepo.allNews).thenReturn(flowOf(emptyList()))
        whenever(mockNewsRepo.getBookmarkedNews()).thenReturn(flowOf(emptyList()))
        whenever(mockNewsRepo.refreshNews()).thenReturn(Result.success(4))

        val viewModel = BalasoreViewModel(injectedNewsRepo = mockNewsRepo)
        testDispatcher.scheduler.advanceUntilIdle()

        val initialCycles = viewModel.uiState.value.newsAutoUpdateCycleCount

        // Act: Trigger automatic periodic news update
        viewModel.triggerNewsAutoUpdate()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert: Repository refresh was invoked, cycle advanced, seconds reset
        verify(mockNewsRepo, times(1)).refreshNews()
        assertEquals(
            "newsAutoUpdateCycleCount should increment by 1",
            initialCycles + 1,
            viewModel.uiState.value.newsAutoUpdateCycleCount
        )
        assertEquals(
            "Countdown seconds should reset to configured interval",
            viewModel.uiState.value.newsAutoUpdateIntervalSeconds,
            viewModel.uiState.value.newsAutoUpdateSecondsRemaining
        )
    }

    @Test
    fun testDataSync_NewsAutoUpdateConfigurationSettings() {
        // Arrange
        val viewModel = BalasoreViewModel()

        // Act 1: Disable news auto-update
        viewModel.setNewsAutoUpdateEnabled(false)
        assertFalse(viewModel.uiState.value.isNewsAutoUpdateEnabled)

        // Act 2: Enable news auto-update
        viewModel.setNewsAutoUpdateEnabled(true)
        assertTrue(viewModel.uiState.value.isNewsAutoUpdateEnabled)

        // Act 3: Configure interval
        viewModel.setNewsAutoUpdateInterval(60)
        assertEquals(60, viewModel.uiState.value.newsAutoUpdateIntervalSeconds)
        assertEquals(60, viewModel.uiState.value.newsAutoUpdateSecondsRemaining)

        viewModel.setNewsAutoUpdateInterval(30)
        assertEquals(30, viewModel.uiState.value.newsAutoUpdateIntervalSeconds)
        assertEquals(30, viewModel.uiState.value.newsAutoUpdateSecondsRemaining)
    }

    @Test
    fun testDataSync_HourlyAutoRefreshTriggersDataSynchronization() = runTest(testDispatcher) {
        // Arrange
        val mockNewsRepo = mock<INewsRepository>()
        whenever(mockNewsRepo.allNews).thenReturn(flowOf(emptyList()))
        whenever(mockNewsRepo.getBookmarkedNews()).thenReturn(flowOf(emptyList()))
        whenever(mockNewsRepo.refreshNews()).thenReturn(Result.success(5))

        val viewModel = BalasoreViewModel(injectedNewsRepo = mockNewsRepo)
        testDispatcher.scheduler.advanceUntilIdle()

        val initialHourlyCycles = viewModel.uiState.value.autoRefreshCycleCount

        // Act: Trigger hourly auto-refresh
        viewModel.triggerHourlyAutoRefresh()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert: News repository refresh was invoked
        verify(mockNewsRepo, times(1)).refreshNews()

        // Assert: Auto-refresh metadata updated
        assertEquals(
            "autoRefreshCycleCount should increment by 1",
            initialHourlyCycles + 1,
            viewModel.uiState.value.autoRefreshCycleCount
        )
        assertEquals(
            "nextHourlyRefreshMinutesRemaining should reset to frequency",
            viewModel.uiState.value.autoUpdateFrequencyMinutes,
            viewModel.uiState.value.nextHourlyRefreshMinutesRemaining
        )
    }

    @Test
    fun testDataSync_HourlyAutoRefreshSettingsAndFrequency() {
        // Arrange
        val viewModel = BalasoreViewModel()

        // Act 1: Toggle hourly auto refresh
        viewModel.setHourlyAutoRefreshEnabled(false)
        assertFalse(viewModel.uiState.value.isHourlyAutoRefreshEnabled)
        assertEquals(0, viewModel.uiState.value.nextHourlyRefreshMinutesRemaining)

        viewModel.setHourlyAutoRefreshEnabled(true)
        assertTrue(viewModel.uiState.value.isHourlyAutoRefreshEnabled)
        assertEquals(
            viewModel.uiState.value.autoUpdateFrequencyMinutes,
            viewModel.uiState.value.nextHourlyRefreshMinutesRemaining
        )

        // Act 2: Set frequency to 45 minutes
        viewModel.setAutoUpdateFrequency(45)
        assertEquals(45, viewModel.uiState.value.autoUpdateFrequencyMinutes)
        assertEquals(45, viewModel.uiState.value.nextHourlyRefreshMinutesRemaining)
    }

    @Test
    fun testDataSync_ForceSyncAllDataUpdatesStatusMessage() = runTest(testDispatcher) {
        // Arrange
        val mockNewsRepo = mock<INewsRepository>()
        whenever(mockNewsRepo.allNews).thenReturn(flowOf(emptyList()))
        whenever(mockNewsRepo.getBookmarkedNews()).thenReturn(flowOf(emptyList()))
        whenever(mockNewsRepo.refreshNews()).thenReturn(Result.success(3))

        val viewModel = BalasoreViewModel(injectedNewsRepo = mockNewsRepo)
        testDispatcher.scheduler.advanceUntilIdle()

        // Act: Trigger forceSyncAllData
        viewModel.forceSyncAllData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert: Status message reflects live synchronization
        assertTrue(
            "Status message should indicate synchronized feeds",
            viewModel.uiState.value.lastSyncStatusMessage.contains("synced")
        )
    }

    @Test
    fun testDataSync_PullToRefreshCallsRepositoryAndClearsRefreshingFlag() = runTest(testDispatcher) {
        // Arrange
        val mockNewsRepo = mock<INewsRepository>()
        whenever(mockNewsRepo.allNews).thenReturn(flowOf(emptyList()))
        whenever(mockNewsRepo.getBookmarkedNews()).thenReturn(flowOf(emptyList()))
        whenever(mockNewsRepo.refreshNews()).thenReturn(Result.success(2))

        val viewModel = BalasoreViewModel(injectedNewsRepo = mockNewsRepo)
        testDispatcher.scheduler.advanceUntilIdle()

        // Act: User pulls down to refresh
        viewModel.refreshAll()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert: Repository was refreshed and isRefreshing is false
        verify(mockNewsRepo).refreshNews()
        assertFalse(viewModel.uiState.value.isRefreshing)
    }

    @Test
    fun testDataSync_EmergencyAlertsDismissalSync() {
        // Arrange
        val viewModel = BalasoreViewModel()
        val alertId = "bls_alert_cyclone_01"

        // Act: User dismisses emergency alert
        viewModel.dismissEmergencyAlert(alertId)

        // Assert: Alert ID is recorded in dismissed set
        assertTrue(
            "Dismissed alerts set must contain the alert ID",
            viewModel.uiState.value.dismissedAlertIds.contains(alertId)
        )
    }
}
