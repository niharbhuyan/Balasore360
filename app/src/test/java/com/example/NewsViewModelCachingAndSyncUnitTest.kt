package com.example

import com.example.data.local.CacheMetadataDao
import com.example.data.local.NewsArticleEntity
import com.example.data.local.NewsDao
import com.example.data.remote.BalasoreApiService
import com.example.data.repository.INewsRepository
import com.example.data.repository.NewsRepository
import com.example.data.repository.Resource
import com.example.ui.viewmodel.BalasoreViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
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
 * Unit Test Suite using JUnit and Mockito verifying:
 * 1. Offline Caching components in BalasoreViewModel & NewsRepository
 * 2. Data Synchronization workflows (Hourly Refresh, News Auto-Update, Pull-to-Refresh)
 * 3. Reactive observation of Room database entities and offline bookmark persistence.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelCachingAndSyncUnitTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testViewModelOfflineNewsCachingFromRoom() = runTest(testDispatcher) {
        // Arrange: Mock the news repository providing cached Room articles
        val mockRepo = mock<INewsRepository>()
        val cachedEntities = listOf(
            NewsArticleEntity(
                id = 101L,
                title = "BLS Station Skywalk Finalized",
                summary = "Balasore skywalk links platforms 1 to 4 with escalators.",
                content = "Full article content covering railway infrastructure developments in Balasore district.",
                category = "Infrastructure",
                source = "Balasore Express",
                publishedAt = "15 mins ago",
                timestamp = System.currentTimeMillis(),
                isBookmarked = false
            ),
            NewsArticleEntity(
                id = 102L,
                title = "Chandipur Coastal Alert System Upgraded",
                summary = "DRDO coastal defense sensors operational.",
                content = "Radar tracking telemetry at Chandipur beach has achieved complete coverage.",
                category = "Defense Alerts",
                source = "Defense Updates",
                publishedAt = "1 hour ago",
                timestamp = System.currentTimeMillis(),
                isBookmarked = true
            )
        )

        whenever(mockRepo.allNews).thenReturn(flowOf(cachedEntities))
        whenever(mockRepo.getBookmarkedNews()).thenReturn(flowOf(listOf(cachedEntities[1])))

        // Act: Initialize ViewModel with the mocked repository
        val viewModel = BalasoreViewModel(injectedNewsRepo = mockRepo)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert: ViewModel correctly consumed and transformed offline cached articles
        val state = viewModel.uiState.value
        assertEquals("Should contain 2 cached articles", 2, state.newsArticles.size)
        assertEquals("First article ID should match", "101", state.newsArticles[0].id)
        assertEquals("Second article ID should match", "102", state.newsArticles[1].id)
        assertTrue("Second article should be marked bookmarked", state.bookmarkedIds.contains("102"))
        assertNotNull("Filtered articles list should be non-empty", state.filteredNewsArticles)
    }

    @Test
    fun testViewModelOfflineBookmarkCacheSynchronization() = runTest(testDispatcher) {
        // Arrange
        val mockRepo = mock<INewsRepository>()
        val bookmarkedArticles = listOf(
            NewsArticleEntity(
                id = 201L,
                title = "Remuna Khirachora Mahotsav Announcement",
                summary = "Dates for annual festival revealed.",
                content = "Devotees will gather in Remuna.",
                category = "Culture",
                source = "Odisha Sambad",
                publishedAt = "2 hours ago",
                isBookmarked = true
            )
        )

        whenever(mockRepo.allNews).thenReturn(flowOf(bookmarkedArticles))
        whenever(mockRepo.getBookmarkedNews()).thenReturn(flowOf(bookmarkedArticles))

        // Act
        val viewModel = BalasoreViewModel(injectedNewsRepo = mockRepo)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert: bookmarkedIds should be synchronized with the offline Room database
        assertTrue(
            "State should reflect bookmarked ID 201 from offline Room storage",
            viewModel.uiState.value.bookmarkedIds.contains("201")
        )
    }

    @Test
    fun testViewModelToggleBookmarkDelegatesToRepository() = runTest(testDispatcher) {
        // Arrange
        val mockRepo = mock<INewsRepository>()
        whenever(mockRepo.allNews).thenReturn(flowOf(emptyList()))
        whenever(mockRepo.getBookmarkedNews()).thenReturn(flowOf(emptyList()))

        val viewModel = BalasoreViewModel(injectedNewsRepo = mockRepo)
        testDispatcher.scheduler.advanceUntilIdle()

        // Act: Toggle bookmark for article ID "305"
        viewModel.toggleBookmark("305")
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert: Repository toggleBookmark must be called with parsed Long ID
        verify(mockRepo).toggleBookmark(eq(305L), eq(false))
    }

    @Test
    fun testDataSynchronization_NewsAutoRefreshTriggersRepositoryRefresh() = runTest(testDispatcher) {
        // Arrange
        val mockRepo = mock<INewsRepository>()
        whenever(mockRepo.allNews).thenReturn(flowOf(emptyList()))
        whenever(mockRepo.getBookmarkedNews()).thenReturn(flowOf(emptyList()))
        whenever(mockRepo.refreshNews()).thenReturn(Result.success(3))

        val viewModel = BalasoreViewModel(injectedNewsRepo = mockRepo)
        testDispatcher.scheduler.advanceUntilIdle()

        val initialCycles = viewModel.uiState.value.newsAutoUpdateCycleCount

        // Act: Trigger auto-refresh synchronization
        viewModel.triggerNewsAutoUpdate()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert: Repository refresh was invoked and cycle count incremented
        verify(mockRepo, times(1)).refreshNews()
        assertEquals(
            "News auto-update cycle count should advance by 1",
            initialCycles + 1,
            viewModel.uiState.value.newsAutoUpdateCycleCount
        )
    }

    @Test
    fun testDataSynchronization_HourlyAutoRefreshTriggersRepositoryRefresh() = runTest(testDispatcher) {
        // Arrange
        val mockRepo = mock<INewsRepository>()
        whenever(mockRepo.allNews).thenReturn(flowOf(emptyList()))
        whenever(mockRepo.getBookmarkedNews()).thenReturn(flowOf(emptyList()))
        whenever(mockRepo.refreshNews()).thenReturn(Result.success(5))

        val viewModel = BalasoreViewModel(injectedNewsRepo = mockRepo)
        testDispatcher.scheduler.advanceUntilIdle()

        val initialHourlyCycles = viewModel.uiState.value.autoRefreshCycleCount

        // Act: Trigger hourly data synchronization
        viewModel.triggerHourlyAutoRefresh()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert: Repository refresh executed, countdown reset, cycle count advanced
        verify(mockRepo, times(1)).refreshNews()
        assertEquals(
            "Hourly cycle count should increment",
            initialHourlyCycles + 1,
            viewModel.uiState.value.autoRefreshCycleCount
        )
        assertEquals(
            "Next hourly refresh countdown should reset to 60 minutes",
            60,
            viewModel.uiState.value.nextHourlyRefreshMinutesRemaining
        )
    }

    @Test
    fun testDataSynchronization_PullToRefreshCallsRepository() = runTest(testDispatcher) {
        // Arrange
        val mockRepo = mock<INewsRepository>()
        whenever(mockRepo.allNews).thenReturn(flowOf(emptyList()))
        whenever(mockRepo.getBookmarkedNews()).thenReturn(flowOf(emptyList()))
        whenever(mockRepo.refreshNews()).thenReturn(Result.success(2))

        val viewModel = BalasoreViewModel(injectedNewsRepo = mockRepo)
        testDispatcher.scheduler.advanceUntilIdle()

        // Act: Manual pull-to-refresh
        viewModel.refreshAll()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert: Data synchronization across news repository was invoked
        verify(mockRepo).refreshNews()
        assertFalse("isRefreshing should return to false after sync", viewModel.uiState.value.isRefreshing)
    }

    @Test
    fun testDataSynchronization_AutoUpdateSettingsToggles() {
        // Arrange
        val viewModel = BalasoreViewModel()

        // Act & Assert 1: Toggle news auto-update engine
        viewModel.setNewsAutoUpdateEnabled(false)
        assertFalse(viewModel.uiState.value.isNewsAutoUpdateEnabled)

        viewModel.setNewsAutoUpdateEnabled(true)
        assertTrue(viewModel.uiState.value.isNewsAutoUpdateEnabled)

        // Act & Assert 2: Change auto-update interval
        viewModel.setNewsAutoUpdateInterval(30)
        assertEquals(30, viewModel.uiState.value.newsAutoUpdateIntervalSeconds)

        viewModel.setNewsAutoUpdateInterval(15)
        assertEquals(15, viewModel.uiState.value.newsAutoUpdateIntervalSeconds)
    }

    @Test
    fun testNewsRepository_isCacheOlderThan24Hours_WhenStale() = runTest(testDispatcher) {
        // Arrange: Mock Room DAOs with timestamp older than 24 hours
        val mockNewsDao = mock<NewsDao>()
        val mockMetadataDao = mock<CacheMetadataDao>()
        val mockApiService = mock<BalasoreApiService>()

        val staleTimestamp = System.currentTimeMillis() - (48 * 60 * 60 * 1000L) // 48 hrs ago
        whenever(mockNewsDao.getLatestTimestamp()).thenReturn(staleTimestamp)
        whenever(mockMetadataDao.getMetadataSync("NEWS")).thenReturn(null)
        whenever(mockNewsDao.getAllNews()).thenReturn(flowOf(emptyList()))
        whenever(mockNewsDao.getBreakingNews()).thenReturn(flowOf(emptyList()))

        val repository = NewsRepository(mockNewsDao, mockMetadataDao, mockApiService)

        // Act
        val isStale = repository.isCacheOlderThan24Hours()

        // Assert: Stale cache should return true
        assertTrue("Cache older than 24 hours should be identified as stale", isStale)
    }

    @Test
    fun testNewsRepository_isCacheOlderThan24Hours_WhenFresh() = runTest(testDispatcher) {
        // Arrange: Mock Room DAOs with timestamp 1 hour ago
        val mockNewsDao = mock<NewsDao>()
        val mockMetadataDao = mock<CacheMetadataDao>()
        val mockApiService = mock<BalasoreApiService>()

        val freshTimestamp = System.currentTimeMillis() - (60 * 60 * 1000L) // 1 hr ago
        whenever(mockNewsDao.getLatestTimestamp()).thenReturn(freshTimestamp)
        whenever(mockMetadataDao.getMetadataSync("NEWS")).thenReturn(null)
        whenever(mockNewsDao.getAllNews()).thenReturn(flowOf(emptyList()))
        whenever(mockNewsDao.getBreakingNews()).thenReturn(flowOf(emptyList()))

        val repository = NewsRepository(mockNewsDao, mockMetadataDao, mockApiService)

        // Act
        val isStale = repository.isCacheOlderThan24Hours()

        // Assert: Fresh cache should return false
        assertFalse("Cache updated 1 hour ago should not be considered stale", isStale)
    }

    @Test
    fun testNewsRepository_offlineFirstUnifiedFlow_UsesCacheWhenFresh() = runTest(testDispatcher) {
        // Arrange: Fresh cache with local articles
        val mockNewsDao = mock<NewsDao>()
        val mockMetadataDao = mock<CacheMetadataDao>()
        val mockApiService = mock<BalasoreApiService>()

        val freshTimestamp = System.currentTimeMillis() - 1000L
        whenever(mockNewsDao.getCount()).thenReturn(5)
        whenever(mockNewsDao.getLatestTimestamp()).thenReturn(freshTimestamp)
        whenever(mockMetadataDao.getMetadataSync("NEWS")).thenReturn(null)
        whenever(mockNewsDao.getAllNews()).thenReturn(flowOf(emptyList()))
        whenever(mockNewsDao.getBreakingNews()).thenReturn(flowOf(emptyList()))

        val repository = NewsRepository(mockNewsDao, mockMetadataDao, mockApiService)

        // Act: Request unified flow without forceRefresh
        val emissions = repository.getUnifiedNewsFlow(forceRefresh = false).toList()

        // Assert: Emits Loading without triggering unnecessary network call
        assertTrue("Emissions should contain at least initial loading state", emissions.isNotEmpty())
        assertTrue("First emission should be Loading", emissions[0] is Resource.Loading)
        verify(mockApiService, never()).getBalasoreNews()
    }
}
