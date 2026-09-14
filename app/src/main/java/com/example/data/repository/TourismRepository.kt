package com.example.data.repository

import com.example.data.local.CacheMetadataDao
import com.example.data.local.CacheSyncMetadataEntity
import com.example.data.local.HotspotDao
import com.example.data.local.HotspotEntity
import com.example.data.local.TourismDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

/**
 * Repository interface defining tourism data operations.
 */
interface ITourismRepository {
    val allHotspots: Flow<List<HotspotEntity>>
    fun getUnifiedTourismFlow(forceRefresh: Boolean = false): Flow<Resource<List<HotspotEntity>>>
    fun getHotspotsByCategory(category: String): Flow<List<HotspotEntity>>
    fun getFavoriteHotspots(): Flow<List<HotspotEntity>>
    fun searchHotspots(query: String): Flow<List<HotspotEntity>>
    fun getHotspotById(id: String): Flow<HotspotEntity?>
    suspend fun isCacheOlderThan24Hours(): Boolean
    suspend fun toggleFavorite(hotspotId: String, currentFavorite: Boolean)
    suspend fun insertHotspot(hotspot: HotspotEntity)
    suspend fun updateHotspot(hotspot: HotspotEntity): Int
    suspend fun deleteHotspot(hotspot: HotspotEntity): Int
    suspend fun deleteHotspotById(id: String): Int
}

/**
 * Repository implementing offline-first caching for Balasore tourism attractions.
 * Abstracts the Room [TourismDao] / [HotspotDao] and ensures uninterrupted offline travel access.
 */
class TourismRepository(
    private val tourismDao: TourismDao,
    private val cacheMetadataDao: CacheMetadataDao? = null
) : ITourismRepository {

    override val allHotspots: Flow<List<HotspotEntity>> =
        tourismDao.getAllHotspots().distinctUntilChanged()

    companion object {
        const val CACHE_EXPIRATION_MS = 24 * 60 * 60 * 1000L // 24 Hours
    }

    override suspend fun isCacheOlderThan24Hours(): Boolean = withContext(Dispatchers.IO) {
        val latestTimestamp = tourismDao.getLatestTimestamp() ?: 0L
        val metadataTime = cacheMetadataDao?.getMetadataSync("TOURISM")?.lastSyncedAt ?: 0L
        val effectiveTime = maxOf(latestTimestamp, metadataTime)
        if (effectiveTime <= 0L) return@withContext true
        (System.currentTimeMillis() - effectiveTime) > CACHE_EXPIRATION_MS
    }

    /**
     * Unified Flow combining local Room database cache with offline verification.
     * Emits [Resource.Loading], seeds or verifies local Room cache, and emits [Resource.Success].
     */
    override fun getUnifiedTourismFlow(forceRefresh: Boolean): Flow<Resource<List<HotspotEntity>>> = flow {
        emit(Resource.Loading())

        val count = tourismDao.getCount()
        val isStale = isCacheOlderThan24Hours()
        if (count == 0) {
            val defaultSpots = DefaultData.getInitialHotspots()
            tourismDao.insertHotspots(defaultSpots)

            cacheMetadataDao?.insertOrUpdate(
                CacheSyncMetadataEntity(
                    cacheKey = "TOURISM",
                    lastSyncedAt = System.currentTimeMillis(),
                    itemCount = defaultSpots.size,
                    status = "FRESH",
                    cacheLabel = "${defaultSpots.size} Tourism Destinations in Room",
                    details = "Persisted offline travel guide & navigation coords."
                )
            )
            emit(Resource.Success(data = defaultSpots, isOfflineCached = true))
        } else if (forceRefresh || isStale) {
            // Preserve user-selected favorite spots persisted in Room
            val favoriteIds = tourismDao.getFavoriteHotspotsSync().map { it.id }.toSet()
            val mergedSpots = DefaultData.getInitialHotspots().map { spot ->
                if (spot.id in favoriteIds) spot.copy(isFavorite = true) else spot
            }
            tourismDao.insertHotspots(mergedSpots)

            cacheMetadataDao?.insertOrUpdate(
                CacheSyncMetadataEntity(
                    cacheKey = "TOURISM",
                    lastSyncedAt = System.currentTimeMillis(),
                    itemCount = mergedSpots.size,
                    status = "FRESH",
                    cacheLabel = "${mergedSpots.size} Tourism Destinations in Room",
                    details = "Persisted offline travel guide & navigation coords."
                )
            )
            emit(Resource.Success(data = mergedSpots, isOfflineCached = true))
        } else {
            cacheMetadataDao?.insertOrUpdate(
                CacheSyncMetadataEntity(
                    cacheKey = "TOURISM",
                    lastSyncedAt = System.currentTimeMillis(),
                    itemCount = count,
                    status = "OFFLINE",
                    cacheLabel = "$count Tourism Destinations",
                    details = "Serving verified offline Room destination cache."
                )
            )
            emit(Resource.Success(data = emptyList(), isOfflineCached = true))
        }
    }.flowOn(Dispatchers.IO)

    override fun getHotspotsByCategory(category: String): Flow<List<HotspotEntity>> =
        if (category == "All") tourismDao.getAllHotspots() else tourismDao.getHotspotsByCategory(category)

    override fun getFavoriteHotspots(): Flow<List<HotspotEntity>> =
        tourismDao.getFavoriteHotspots()

    override fun searchHotspots(query: String): Flow<List<HotspotEntity>> =
        if (query.isBlank()) tourismDao.getAllHotspots() else tourismDao.searchHotspots(query.trim())

    override fun getHotspotById(id: String): Flow<HotspotEntity?> =
        tourismDao.getHotspotById(id)

    override suspend fun toggleFavorite(hotspotId: String, currentFavorite: Boolean) = withContext(Dispatchers.IO) {
        tourismDao.updateFavorite(hotspotId, !currentFavorite)
    }

    override suspend fun insertHotspot(hotspot: HotspotEntity) = withContext(Dispatchers.IO) {
        tourismDao.insertHotspot(hotspot)
    }

    override suspend fun updateHotspot(hotspot: HotspotEntity): Int = withContext(Dispatchers.IO) {
        tourismDao.updateHotspot(hotspot)
    }

    override suspend fun deleteHotspot(hotspot: HotspotEntity): Int = withContext(Dispatchers.IO) {
        tourismDao.deleteHotspot(hotspot)
    }

    override suspend fun deleteHotspotById(id: String): Int = withContext(Dispatchers.IO) {
        tourismDao.deleteHotspotById(id)
    }
}
