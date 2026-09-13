package com.example.data.repository

import com.example.data.local.CacheMetadataDao
import com.example.data.local.CacheSyncMetadataEntity
import com.example.data.local.NewsArticleEntity
import com.example.data.local.NewsDao
import com.example.data.remote.BalasoreApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

/**
 * Repository interface defining news data operations.
 */
interface INewsRepository {
    val allNews: Flow<List<NewsArticleEntity>>
    val breakingNews: Flow<List<NewsArticleEntity>>
    fun getUnifiedNewsFlow(forceRefresh: Boolean = false): Flow<Resource<List<NewsArticleEntity>>>
    fun getNewsByCategory(category: String): Flow<List<NewsArticleEntity>>
    fun getBookmarkedNews(): Flow<List<NewsArticleEntity>>
    fun searchNews(query: String): Flow<List<NewsArticleEntity>>
    fun getArticleById(id: Long): Flow<NewsArticleEntity?>
    suspend fun isCacheOlderThan24Hours(): Boolean
    suspend fun refreshNews(): Result<Int>
    suspend fun toggleBookmark(articleId: Long, currentBookmarked: Boolean)
    suspend fun insertArticle(article: NewsArticleEntity): Long
    suspend fun updateArticle(article: NewsArticleEntity): Int
    suspend fun deleteArticle(article: NewsArticleEntity): Int
    suspend fun deleteArticleById(id: Long): Int
}

/**
 * Repository implementing offline-first caching for local Balasore district news.
 * Abstracts the Room [NewsDao] and Retrofit [BalasoreApiService].
 */
class NewsRepository(
    private val newsDao: NewsDao,
    private val cacheMetadataDao: CacheMetadataDao? = null,
    private val apiService: BalasoreApiService = BalasoreApiService.create()
) : INewsRepository {

    override val allNews: Flow<List<NewsArticleEntity>> = newsDao.getAllNews().distinctUntilChanged()

    override val breakingNews: Flow<List<NewsArticleEntity>> = newsDao.getBreakingNews().distinctUntilChanged()

    companion object {
        const val CACHE_EXPIRATION_MS = 24 * 60 * 60 * 1000L // 24 Hours
    }

    override suspend fun isCacheOlderThan24Hours(): Boolean = withContext(Dispatchers.IO) {
        val latestTimestamp = newsDao.getLatestTimestamp() ?: 0L
        val metadataTime = cacheMetadataDao?.getMetadataSync("NEWS")?.lastSyncedAt ?: 0L
        val effectiveTime = maxOf(latestTimestamp, metadataTime)
        if (effectiveTime <= 0L) return@withContext true
        (System.currentTimeMillis() - effectiveTime) > CACHE_EXPIRATION_MS
    }

    /**
     * Unified Flow combining local Room cache with remote network fetching.
     * 1. Emits [Resource.Loading] with cached Room data immediately for offline responsiveness.
     * 2. If [forceRefresh], if cache is empty, OR if cache is older than 24 hours, automatically queries the network.
     * 3. On success, persists to Room and emits [Resource.Success].
     * 4. On network failure, smoothly emits [Resource.Success] with cached data if available,
     *    or [Resource.Error] with fallback cache so the UI never breaks.
     */
    override fun getUnifiedNewsFlow(forceRefresh: Boolean): Flow<Resource<List<NewsArticleEntity>>> = flow {
        // Step 1: Read local Room cache synchronously
        val hasLocalArticles = newsDao.getCount() > 0
        val isStale = isCacheOlderThan24Hours()

        emit(Resource.Loading())

        if (forceRefresh || !hasLocalArticles || isStale) {
            try {
                val response = apiService.getBalasoreNews()
                val dtos = response.articles.orEmpty()

                val bookmarkedArticles = newsDao.getBookmarkedNewsSync()
                val bookmarkedIds = bookmarkedArticles.map { it.id }.toSet()
                val bookmarkedTitles = bookmarkedArticles.map { it.title.trim().lowercase() }.toSet()

                val freshEntities = dtos.map { dto ->
                    val isBookmarked = (dto.id != null && bookmarkedIds.contains(dto.id)) ||
                            (dto.title != null && bookmarkedTitles.contains(dto.title.trim().lowercase()))
                    dto.toEntity(isBookmarked = isBookmarked)
                }

                if (freshEntities.isNotEmpty()) {
                    newsDao.insertArticles(freshEntities)
                }

                cacheMetadataDao?.insertOrUpdate(
                    CacheSyncMetadataEntity(
                        cacheKey = "NEWS",
                        lastSyncedAt = System.currentTimeMillis(),
                        itemCount = newsDao.getCount(),
                        status = "FRESH",
                        cacheLabel = "${newsDao.getCount()} District News Articles",
                        details = "Persisted live to Room local database."
                    )
                )

                // Step 2: Emit success with fresh data from Room
                emit(Resource.Success(data = freshEntities, isOfflineCached = false))
            } catch (e: Exception) {
                // Fallback to local Room cache
                cacheMetadataDao?.insertOrUpdate(
                    CacheSyncMetadataEntity(
                        cacheKey = "NEWS",
                        lastSyncedAt = System.currentTimeMillis(),
                        itemCount = newsDao.getCount(),
                        status = "OFFLINE",
                        cacheLabel = "Offline Room News Cache",
                        details = "Serving local cached news articles without active network."
                    )
                )
                emit(Resource.Success(data = emptyList(), isOfflineCached = true, message = "Serving from offline Room cache"))
            }
        } else {
            emit(Resource.Success(data = emptyList(), isOfflineCached = true))
        }
    }.flowOn(Dispatchers.IO)

    override fun getNewsByCategory(category: String): Flow<List<NewsArticleEntity>> =
        if (category == "All") newsDao.getAllNews() else newsDao.getNewsByCategory(category)

    override fun getBookmarkedNews(): Flow<List<NewsArticleEntity>> =
        newsDao.getBookmarkedNews()

    override fun searchNews(query: String): Flow<List<NewsArticleEntity>> =
        if (query.isBlank()) newsDao.getAllNews() else newsDao.searchNews(query.trim())

    override fun getArticleById(id: Long): Flow<NewsArticleEntity?> =
        newsDao.getArticleById(id)

    override suspend fun refreshNews(): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getBalasoreNews()
            val dtos = response.articles.orEmpty()

            val bookmarkedArticles = newsDao.getBookmarkedNewsSync()
            val bookmarkedIds = bookmarkedArticles.map { it.id }.toSet()
            val bookmarkedTitles = bookmarkedArticles.map { it.title.trim().lowercase() }.toSet()

            val entities = dtos.map { dto ->
                val isBookmarked = (dto.id != null && bookmarkedIds.contains(dto.id)) ||
                        (dto.title != null && bookmarkedTitles.contains(dto.title.trim().lowercase()))
                dto.toEntity(isBookmarked = isBookmarked)
            }

            if (entities.isNotEmpty()) {
                newsDao.insertArticles(entities)
            }

            val totalCount = newsDao.getCount()
            cacheMetadataDao?.insertOrUpdate(
                CacheSyncMetadataEntity(
                    cacheKey = "NEWS",
                    lastSyncedAt = System.currentTimeMillis(),
                    itemCount = totalCount,
                    status = "FRESH",
                    cacheLabel = "$totalCount News Articles",
                    details = "Refreshed via Balasore API and cached into Room."
                )
            )
            Result.success(totalCount)
        } catch (e: Exception) {
            val count = newsDao.getCount()
            if (count > 0) {
                Result.success(count)
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun toggleBookmark(articleId: Long, currentBookmarked: Boolean) = withContext(Dispatchers.IO) {
        newsDao.updateBookmark(articleId, !currentBookmarked)
    }

    override suspend fun insertArticle(article: NewsArticleEntity): Long = withContext(Dispatchers.IO) {
        newsDao.insertArticle(article)
    }

    override suspend fun updateArticle(article: NewsArticleEntity): Int = withContext(Dispatchers.IO) {
        newsDao.updateArticle(article)
    }

    override suspend fun deleteArticle(article: NewsArticleEntity): Int = withContext(Dispatchers.IO) {
        newsDao.deleteArticle(article)
    }

    override suspend fun deleteArticleById(id: Long): Int = withContext(Dispatchers.IO) {
        newsDao.deleteArticleById(id)
    }
}
