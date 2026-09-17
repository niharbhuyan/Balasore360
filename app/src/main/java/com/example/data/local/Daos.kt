package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    // --- CREATE ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: NewsArticleEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<NewsArticleEntity>)

    // --- READ ---
    @Query("SELECT * FROM news_articles ORDER BY isBreaking DESC, timestamp DESC")
    fun getAllNews(): Flow<List<NewsArticleEntity>>

    @Query("SELECT * FROM news_articles WHERE category = :category ORDER BY timestamp DESC")
    fun getNewsByCategory(category: String): Flow<List<NewsArticleEntity>>

    @Query("SELECT * FROM news_articles WHERE isBookmarked = 1 ORDER BY timestamp DESC")
    fun getBookmarkedNews(): Flow<List<NewsArticleEntity>>

    @Query("SELECT * FROM news_articles WHERE isBookmarked = 1")
    suspend fun getBookmarkedNewsSync(): List<NewsArticleEntity>

    @Query("SELECT * FROM news_articles WHERE isBreaking = 1 ORDER BY timestamp DESC LIMIT 3")
    fun getBreakingNews(): Flow<List<NewsArticleEntity>>

    @Query("SELECT * FROM news_articles WHERE id = :id LIMIT 1")
    fun getArticleById(id: Long): Flow<NewsArticleEntity?>

    @Query("SELECT * FROM news_articles WHERE id = :id LIMIT 1")
    suspend fun getArticleByIdSync(id: Long): NewsArticleEntity?

    @Query("SELECT * FROM news_articles WHERE title LIKE '%' || :query || '%' OR summary LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchNews(query: String): Flow<List<NewsArticleEntity>>

    @Query("SELECT * FROM news_articles")
    suspend fun getAllArticlesSync(): List<NewsArticleEntity>

    @Query("SELECT COUNT(*) FROM news_articles")
    suspend fun getCount(): Int

    @Query("SELECT COUNT(*) FROM news_articles")
    fun getNewsCountFlow(): Flow<Int>

    @Query("SELECT MAX(timestamp) FROM news_articles")
    suspend fun getLatestTimestamp(): Long?

    @Query("SELECT MIN(timestamp) FROM news_articles")
    suspend fun getOldestTimestamp(): Long?

    // --- UPDATE ---
    @Update
    suspend fun updateArticle(article: NewsArticleEntity): Int

    @Update
    suspend fun updateArticles(articles: List<NewsArticleEntity>): Int

    @Query("UPDATE news_articles SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun updateBookmark(id: Long, isBookmarked: Boolean)

    // --- DELETE ---
    @Delete
    suspend fun deleteArticle(article: NewsArticleEntity): Int

    @Query("DELETE FROM news_articles WHERE id = :id")
    suspend fun deleteArticleById(id: Long): Int

    @Query("DELETE FROM news_articles WHERE isBookmarked = 0")
    suspend fun clearNonBookmarked()

    @Query("DELETE FROM news_articles")
    suspend fun clearAll()
}

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather_cache WHERE id = 1 LIMIT 1")
    fun getWeatherCache(): Flow<WeatherCacheEntity?>

    @Query("SELECT * FROM weather_cache WHERE id = 1 LIMIT 1")
    suspend fun getWeatherCacheSync(): WeatherCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateWeather(weather: WeatherCacheEntity)

    @Query("SELECT MAX(lastUpdated) FROM weather_cache")
    suspend fun getLatestTimestamp(): Long?

    @Query("DELETE FROM weather_cache")
    suspend fun clearWeatherCache()

    @Query("SELECT * FROM daily_forecasts ORDER BY date ASC")
    fun getDailyForecasts(): Flow<List<DailyForecastEntity>>

    @Query("SELECT * FROM daily_forecasts ORDER BY date ASC")
    suspend fun getDailyForecastsSync(): List<DailyForecastEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyForecasts(forecasts: List<DailyForecastEntity>)

    @Query("DELETE FROM daily_forecasts")
    suspend fun clearDailyForecasts()
}

@Dao
interface HotspotDao {
    // --- CREATE ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHotspot(hotspot: HotspotEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHotspots(hotspots: List<HotspotEntity>)

    // --- READ ---
    @Query("SELECT * FROM tourism_hotspots ORDER BY distanceKmFromBls ASC")
    fun getAllHotspots(): Flow<List<HotspotEntity>>

    @Query("SELECT * FROM tourism_hotspots ORDER BY distanceKmFromBls ASC")
    suspend fun getAllHotspotsSync(): List<HotspotEntity>

    @Query("SELECT * FROM tourism_hotspots WHERE category = :category ORDER BY distanceKmFromBls ASC")
    fun getHotspotsByCategory(category: String): Flow<List<HotspotEntity>>

    @Query("SELECT * FROM tourism_hotspots WHERE isFavorite = 1 ORDER BY distanceKmFromBls ASC")
    fun getFavoriteHotspots(): Flow<List<HotspotEntity>>

    @Query("SELECT * FROM tourism_hotspots WHERE isFavorite = 1 ORDER BY distanceKmFromBls ASC")
    suspend fun getFavoriteHotspotsSync(): List<HotspotEntity>

    @Query("SELECT * FROM tourism_hotspots WHERE id = :id LIMIT 1")
    fun getHotspotById(id: String): Flow<HotspotEntity?>

    @Query("SELECT * FROM tourism_hotspots WHERE id = :id LIMIT 1")
    suspend fun getHotspotByIdSync(id: String): HotspotEntity?

    @Query("SELECT * FROM tourism_hotspots WHERE name LIKE '%' || :query || '%' OR odiaName LIKE '%' || :query || '%' OR hindiName LIKE '%' || :query || '%' OR shortDescription LIKE '%' || :query || '%' OR highlights LIKE '%' || :query || '%' ORDER BY distanceKmFromBls ASC")
    fun searchHotspots(query: String): Flow<List<HotspotEntity>>

    @Query("SELECT COUNT(*) FROM tourism_hotspots")
    suspend fun getCount(): Int

    @Query("SELECT COUNT(*) FROM tourism_hotspots")
    fun getHotspotsCountFlow(): Flow<Int>

    @Query("SELECT MAX(timestamp) FROM tourism_hotspots")
    suspend fun getLatestTimestamp(): Long?

    @Query("SELECT MIN(timestamp) FROM tourism_hotspots")
    suspend fun getOldestTimestamp(): Long?

    // --- UPDATE ---
    @Update
    suspend fun updateHotspot(hotspot: HotspotEntity): Int

    @Update
    suspend fun updateHotspots(hotspots: List<HotspotEntity>): Int

    @Query("UPDATE tourism_hotspots SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: String, isFavorite: Boolean)

    // --- DELETE ---
    @Delete
    suspend fun deleteHotspot(hotspot: HotspotEntity): Int

    @Query("DELETE FROM tourism_hotspots WHERE id = :id")
    suspend fun deleteHotspotById(id: String): Int

    @Query("DELETE FROM tourism_hotspots")
    suspend fun clearAllHotspots()
}

/**
 * Typealias for semantic clarity: TourismDao refers directly to HotspotDao.
 */
typealias TourismDao = HotspotDao

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUserById(id: String): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserByIdSync(id: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users ORDER BY createdAt DESC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET avatarUri = :avatarUri WHERE id = :userId")
    suspend fun updateAvatar(userId: String, avatarUri: String?)

    @Query("UPDATE users SET passwordHash = :newPassword WHERE email = :email")
    suspend fun updatePassword(email: String, newPassword: String): Int

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getCount(): Int
}

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews WHERE targetType = :targetType AND targetId = :targetId ORDER BY timestamp DESC")
    fun getReviewsForTarget(targetType: String, targetId: String): Flow<List<ReviewEntity>>

    @Query("SELECT * FROM reviews WHERE targetType = :targetType ORDER BY timestamp DESC")
    fun getReviewsByType(targetType: String): Flow<List<ReviewEntity>>

    @Query("SELECT * FROM reviews ORDER BY timestamp DESC")
    fun getAllReviews(): Flow<List<ReviewEntity>>

    @Query("SELECT AVG(rating) FROM reviews WHERE targetType = :targetType AND targetId = :targetId")
    fun getAverageRating(targetType: String, targetId: String): Flow<Double?>

    @Query("SELECT COUNT(*) FROM reviews WHERE targetType = :targetType AND targetId = :targetId")
    fun getReviewCount(targetType: String, targetId: String): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviews(reviews: List<ReviewEntity>)

    @Query("SELECT COUNT(*) FROM reviews")
    suspend fun getCount(): Int
}

@Dao
interface CacheMetadataDao {
    @Query("SELECT * FROM cache_sync_metadata ORDER BY lastSyncedAt DESC")
    fun getAllMetadata(): Flow<List<CacheSyncMetadataEntity>>

    @Query("SELECT * FROM cache_sync_metadata WHERE cacheKey = :cacheKey LIMIT 1")
    fun getMetadata(cacheKey: String): Flow<CacheSyncMetadataEntity?>

    @Query("SELECT * FROM cache_sync_metadata WHERE cacheKey = :cacheKey LIMIT 1")
    suspend fun getMetadataSync(cacheKey: String): CacheSyncMetadataEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(metadata: CacheSyncMetadataEntity)

    @Query("UPDATE cache_sync_metadata SET lastSyncedAt = :timestamp, itemCount = :itemCount, status = :status, cacheLabel = :label, details = :details WHERE cacheKey = :cacheKey")
    suspend fun updateSyncInfo(cacheKey: String, timestamp: Long, itemCount: Int, status: String, label: String, details: String): Int

    @Query("SELECT COUNT(*) FROM cache_sync_metadata")
    suspend fun getCount(): Int

    @Query("DELETE FROM cache_sync_metadata")
    suspend fun clearAll()
}

@Dao
interface ItineraryDao {
    @Query("SELECT * FROM travel_itinerary ORDER BY dayNumber ASC, id ASC")
    fun getAllItineraryItems(): Flow<List<ItineraryItemEntity>>

    @Query("SELECT * FROM travel_itinerary ORDER BY dayNumber ASC, id ASC")
    suspend fun getAllItineraryItemsSync(): List<ItineraryItemEntity>

    @Query("SELECT * FROM travel_itinerary WHERE id = :id LIMIT 1")
    suspend fun getItineraryItemById(id: Long): ItineraryItemEntity?

    @Query("SELECT COUNT(*) FROM travel_itinerary")
    fun getItineraryCountFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM travel_itinerary")
    suspend fun getItineraryCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItineraryItem(item: ItineraryItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItineraryItems(items: List<ItineraryItemEntity>)

    @Update
    suspend fun updateItineraryItem(item: ItineraryItemEntity): Int

    @Delete
    suspend fun deleteItineraryItem(item: ItineraryItemEntity): Int

    @Query("DELETE FROM travel_itinerary WHERE id = :id")
    suspend fun deleteItineraryItemById(id: Long): Int

    @Query("DELETE FROM travel_itinerary WHERE hotspotId = :hotspotId")
    suspend fun deleteByHotspotId(hotspotId: String): Int

    @Query("DELETE FROM travel_itinerary")
    suspend fun clearAllItinerary(): Int
}

@Dao
interface TravelJournalDao {
    @Query("SELECT * FROM travel_journals ORDER BY visitedDate DESC")
    fun getAllJournals(): Flow<List<TravelJournalEntity>>

    @Query("SELECT * FROM travel_journals ORDER BY visitedDate DESC")
    suspend fun getAllJournalsSync(): List<TravelJournalEntity>

    @Query("SELECT COUNT(*) FROM travel_journals")
    suspend fun getJournalCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournal(journal: TravelJournalEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournals(journals: List<TravelJournalEntity>)

    @Update
    suspend fun updateJournal(journal: TravelJournalEntity): Int

    @Delete
    suspend fun deleteJournal(journal: TravelJournalEntity): Int

    @Query("DELETE FROM travel_journals WHERE id = :id")
    suspend fun deleteJournalById(id: Long): Int

    @Query("DELETE FROM travel_journals")
    suspend fun clearAllJournals(): Int
}

