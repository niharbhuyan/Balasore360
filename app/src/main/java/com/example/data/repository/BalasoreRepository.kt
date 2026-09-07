package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.local.AppDatabase
import com.example.data.local.CacheSyncMetadataEntity
import com.example.data.local.DailyForecastEntity
import com.example.data.local.HotspotEntity
import com.example.data.local.NewsArticleEntity
import com.example.data.local.ReviewEntity
import com.example.data.local.UserEntity
import com.example.data.local.WeatherCacheEntity
import com.example.data.remote.WeatherApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class SyncResult(
    val isSuccess: Boolean,
    val weatherUpdated: Boolean,
    val newsArticlesCount: Int,
    val hotspotsCount: Int,
    val timestamp: Long,
    val isOfflineServed: Boolean = false,
    val message: String = "Cache synchronized"
)

class BalasoreRepository(
    private val database: AppDatabase,
    private val context: Context? = null,
    private val weatherApi: WeatherApiService = WeatherApiService.create()
) {
    private val prefs: SharedPreferences? =
        context?.getSharedPreferences("balasore_user_prefs", Context.MODE_PRIVATE)

    private val _currentUserId = MutableStateFlow<String?>(
        prefs?.getString("logged_in_user_id", "niharbhuyan@gmail.com") ?: "niharbhuyan@gmail.com"
    )
    val currentUserId: Flow<String?> = _currentUserId.asStateFlow()

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val currentUser: Flow<UserEntity?> = _currentUserId.flatMapLatest { id ->
        if (id == null) flowOf(null) else database.userDao().getUserById(id)
    }

    val allNews: Flow<List<NewsArticleEntity>> = database.newsDao().getAllNews()
    val breakingNews: Flow<List<NewsArticleEntity>> = database.newsDao().getBreakingNews()
    val allHotspots: Flow<List<HotspotEntity>> = database.hotspotDao().getAllHotspots()
    val weatherCache: Flow<WeatherCacheEntity?> = database.weatherDao().getWeatherCache()
    val dailyForecasts: Flow<List<DailyForecastEntity>> = database.weatherDao().getDailyForecasts()
    val cacheMetadataList: Flow<List<CacheSyncMetadataEntity>> = database.cacheMetadataDao().getAllMetadata()
    val allReviews: Flow<List<ReviewEntity>> = database.reviewDao().getAllReviews()

    suspend fun initializeIfNeeded() = withContext(Dispatchers.IO) {
        if (database.hotspotDao().getCount() == 0) {
            database.hotspotDao().insertHotspots(DefaultData.getInitialHotspots())
        }
        if (database.newsDao().getCount() == 0) {
            database.newsDao().insertArticles(DefaultData.getInitialNews())
        }
        if (database.userDao().getCount() == 0) {
            for (user in DefaultData.getInitialUsers()) {
                database.userDao().insertUser(user)
            }
        }
        if (database.reviewDao().getCount() == 0) {
            database.reviewDao().insertReviews(DefaultData.getInitialReviews())
        }
        // If weather is empty, insert initial default
        if (database.weatherDao().getWeatherCacheSync() == null) {
            database.weatherDao().insertOrUpdateWeather(
                WeatherCacheEntity(
                    id = 1,
                    temperature = 29.5,
                    apparentTemperature = 32.0,
                    weatherCode = 1,
                    weatherDescription = "Mainly Clear Coastal Sky",
                    windSpeed = 14.5,
                    windGusts = 22.0,
                    humidity = 72,
                    alertLevel = "NORMAL",
                    alertTitle = "Normal Marine & Coastal Conditions",
                    alertMessage = "Calm to moderate sea breeze along Chandipur and Talasari. Ideal for sightseeing and low-tide beach walking.",
                    tideState = calculateTideState().first,
                    tideDescription = calculateTideState().second,
                    sunrise = "05:32 AM",
                    sunset = "06:14 PM",
                    uvIndex = 6.8,
                    maxTemp = 32.0,
                    minTemp = 24.5,
                    lastUpdated = System.currentTimeMillis()
                )
            )
        }
        // If daily forecast is empty in Room, initialize 7-day forecast
        if (database.weatherDao().getDailyForecastsSync().isEmpty()) {
            database.weatherDao().insertDailyForecasts(createInitialDailyForecasts())
        }
        // If cache metadata is empty, seed Room metadata table
        if (database.cacheMetadataDao().getCount() == 0) {
            val now = System.currentTimeMillis()
            val newsCount = database.newsDao().getCount()
            val hotspotCount = database.hotspotDao().getCount()
            database.cacheMetadataDao().insertOrUpdate(
                CacheSyncMetadataEntity(
                    cacheKey = "WEATHER",
                    lastSyncedAt = now,
                    itemCount = 8,
                    status = "FRESH",
                    cacheLabel = "Current + 7-Day Room Forecast",
                    details = "Persisted locally in Room SQLite database."
                )
            )
            database.cacheMetadataDao().insertOrUpdate(
                CacheSyncMetadataEntity(
                    cacheKey = "NEWS",
                    lastSyncedAt = now,
                    itemCount = newsCount,
                    status = "FRESH",
                    cacheLabel = "$newsCount News Articles Cached",
                    details = "Full articles, bookmarks, and summaries saved in Room."
                )
            )
            database.cacheMetadataDao().insertOrUpdate(
                CacheSyncMetadataEntity(
                    cacheKey = "TOURISM",
                    lastSyncedAt = now,
                    itemCount = hotspotCount,
                    status = "FRESH",
                    cacheLabel = "$hotspotCount Tourism Hotspots Cached",
                    details = "Complete beach, temple, and heritage directory cached in Room."
                )
            )
        }
    }

    // --- Authentication & Profile Methods ---
    suspend fun login(email: String, password: String): Result<UserEntity> = withContext(Dispatchers.IO) {
        val trimmedEmail = email.trim().lowercase()
        val user = database.userDao().getUserByEmail(trimmedEmail)
        if (user == null) {
            Result.failure(Exception("No account found with $email. Please sign up."))
        } else if (user.passwordHash != password) {
            Result.failure(Exception("Incorrect password. Please check and try again."))
        } else {
            _currentUserId.value = user.id
            prefs?.edit()?.putString("logged_in_user_id", user.id)?.apply()
            Result.success(user)
        }
    }

    suspend fun signUp(
        fullName: String,
        email: String,
        password: String,
        phoneNumber: String,
        locality: String,
        securityAnswer: String
    ): Result<UserEntity> = withContext(Dispatchers.IO) {
        val trimmedEmail = email.trim().lowercase()
        if (trimmedEmail.isBlank() || !trimmedEmail.contains("@")) {
            return@withContext Result.failure(Exception("Please enter a valid email address"))
        }
        if (password.length < 4) {
            return@withContext Result.failure(Exception("Password must be at least 4 characters"))
        }
        val existing = database.userDao().getUserByEmail(trimmedEmail)
        if (existing != null) {
            return@withContext Result.failure(Exception("An account with this email already exists. Please log in."))
        }

        val newUser = UserEntity(
            id = trimmedEmail,
            fullName = fullName.ifBlank { "Balasore Explorer" },
            email = trimmedEmail,
            passwordHash = password,
            phoneNumber = phoneNumber,
            locality = locality.ifBlank { "Balasore Town" },
            bio = "Active resident & traveler in Balasore district",
            avatarUri = null,
            securityAnswer = securityAnswer.trim().ifBlank { "Balasore" }
        )
        database.userDao().insertUser(newUser)
        _currentUserId.value = newUser.id
        prefs?.edit()?.putString("logged_in_user_id", newUser.id)?.apply()
        Result.success(newUser)
    }

    suspend fun resetPassword(
        email: String,
        securityAnswer: String,
        newPassword: String
    ): Result<Boolean> = withContext(Dispatchers.IO) {
        val trimmedEmail = email.trim().lowercase()
        val user = database.userDao().getUserByEmail(trimmedEmail)
            ?: return@withContext Result.failure(Exception("No account found for $email"))

        if (!user.securityAnswer.equals(securityAnswer.trim(), ignoreCase = true)) {
            return@withContext Result.failure(Exception("Incorrect answer to security question."))
        }
        if (newPassword.length < 4) {
            return@withContext Result.failure(Exception("Password must be at least 4 characters."))
        }

        database.userDao().updatePassword(trimmedEmail, newPassword)
        Result.success(true)
    }

    suspend fun logout() = withContext(Dispatchers.IO) {
        _currentUserId.value = null
        prefs?.edit()?.remove("logged_in_user_id")?.apply()
    }

    suspend fun updateUserProfile(user: UserEntity): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            database.userDao().updateUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserAvatar(userId: String, avatarUri: String?): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            database.userDao().updateAvatar(userId, avatarUri)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- Feedback and Reviews Methods ---
    fun getReviewsForTarget(targetType: String, targetId: String): Flow<List<ReviewEntity>> {
        return database.reviewDao().getReviewsForTarget(targetType, targetId)
    }

    fun getAverageRating(targetType: String, targetId: String): Flow<Double?> {
        return database.reviewDao().getAverageRating(targetType, targetId)
    }

    fun getReviewCount(targetType: String, targetId: String): Flow<Int> {
        return database.reviewDao().getReviewCount(targetType, targetId)
    }

    suspend fun submitReview(
        targetType: String,
        targetId: String,
        targetTitle: String,
        rating: Int,
        comment: String,
        authorName: String? = null,
        authorLocality: String? = null,
        authorAvatarUri: String? = null
    ): Result<ReviewEntity> = withContext(Dispatchers.IO) {
        if (comment.isBlank()) {
            return@withContext Result.failure(Exception("Please write a comment or feedback."))
        }
        val currentLoggedIn = _currentUserId.value?.let { database.userDao().getUserByIdSync(it) }

        val review = ReviewEntity(
            targetType = targetType,
            targetId = targetId,
            targetTitle = targetTitle,
            userId = currentLoggedIn?.id ?: "guest_${System.currentTimeMillis()}",
            userName = currentLoggedIn?.fullName ?: authorName?.ifBlank { "Balasore Visitor" } ?: "Balasore Visitor",
            userLocality = currentLoggedIn?.locality ?: authorLocality?.ifBlank { "Balasore" } ?: "Balasore",
            userAvatarUri = currentLoggedIn?.avatarUri ?: authorAvatarUri,
            rating = rating.coerceIn(1, 5),
            comment = comment.trim(),
            timestamp = System.currentTimeMillis()
        )
        val id = database.reviewDao().insertReview(review)
        Result.success(review.copy(id = id))
    }

    suspend fun refreshWeatherAndAlerts(): Result<WeatherCacheEntity> = withContext(Dispatchers.IO) {
        try {
            val response = weatherApi.getBalasoreForecast()
            val current = response.current
            val daily = response.daily

            val temp = current?.temperature ?: 30.0
            val feelsLike = current?.apparentTemperature ?: temp
            val code = current?.weatherCode ?: 0
            val wind = current?.windSpeed ?: 15.0
            val gusts = current?.windGusts ?: (wind * 1.3)
            val humidity = current?.relativeHumidity ?: 70

            val desc = parseWeatherCode(code)
            val (alertLevel, alertTitle, alertMsg) = evaluateCoastalAlert(code, wind, gusts, temp)
            val (tideState, tideDesc) = calculateTideState()

            val maxT = daily?.temperatureMax?.firstOrNull() ?: (temp + 2.0)
            val minT = daily?.temperatureMin?.firstOrNull() ?: (temp - 4.0)
            val uv = daily?.uvIndexMax?.firstOrNull() ?: 7.0

            val sunriseStr = daily?.sunrise?.firstOrNull()?.let { formatTimeIso(it) } ?: "05:35 AM"
            val sunsetStr = daily?.sunset?.firstOrNull()?.let { formatTimeIso(it) } ?: "06:12 PM"

            val entity = WeatherCacheEntity(
                id = 1,
                temperature = temp,
                apparentTemperature = feelsLike,
                weatherCode = code,
                weatherDescription = desc,
                windSpeed = wind,
                windGusts = gusts,
                humidity = humidity,
                alertLevel = alertLevel,
                alertTitle = alertTitle,
                alertMessage = alertMsg,
                tideState = tideState,
                tideDescription = tideDesc,
                sunrise = sunriseStr,
                sunset = sunsetStr,
                uvIndex = uv,
                maxTemp = maxT,
                minTemp = minT,
                lastUpdated = System.currentTimeMillis()
            )

            database.weatherDao().insertOrUpdateWeather(entity)

            // Cache 7-Day daily forecast into Room
            val dailyEntities = mutableListOf<DailyForecastEntity>()
            val dailyTimes = daily?.time
            if (!dailyTimes.isNullOrEmpty()) {
                val inDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dayOfWeekFormat = SimpleDateFormat("EEE", Locale.getDefault())
                for (i in dailyTimes.indices) {
                    val dateStr = dailyTimes[i]
                    val dayName = try {
                        val d = inDateFormat.parse(dateStr)
                        if (d != null) dayOfWeekFormat.format(d) else "Day $i"
                    } catch (_: Exception) { "Day $i" }

                    val dayCode = daily.weatherCode?.getOrNull(i) ?: code
                    val dayMax = daily.temperatureMax?.getOrNull(i) ?: maxT
                    val dayMin = daily.temperatureMin?.getOrNull(i) ?: minT
                    val daySunrise = daily.sunrise?.getOrNull(i)?.let { formatTimeIso(it) } ?: sunriseStr
                    val daySunset = daily.sunset?.getOrNull(i)?.let { formatTimeIso(it) } ?: sunsetStr
                    val dayUv = daily.uvIndexMax?.getOrNull(i) ?: uv

                    dailyEntities.add(
                        DailyForecastEntity(
                            date = dateStr,
                            dayOfWeek = dayName,
                            weatherCode = dayCode,
                            weatherDescription = parseWeatherCode(dayCode),
                            maxTemp = dayMax,
                            minTemp = dayMin,
                            sunrise = daySunrise,
                            sunset = daySunset,
                            uvIndex = dayUv,
                            cachedAt = System.currentTimeMillis()
                        )
                    )
                }
            }
            if (dailyEntities.isNotEmpty()) {
                database.weatherDao().clearDailyForecasts()
                database.weatherDao().insertDailyForecasts(dailyEntities)
            }

            database.cacheMetadataDao().insertOrUpdate(
                CacheSyncMetadataEntity(
                    cacheKey = "WEATHER",
                    lastSyncedAt = System.currentTimeMillis(),
                    itemCount = 1 + dailyEntities.size,
                    status = "FRESH",
                    cacheLabel = "Current + ${dailyEntities.size}-Day Room Forecast",
                    details = "Updated live from meteorological satellite feed."
                )
            )

            Result.success(entity)
        } catch (e: Exception) {
            // In case of network failure, fallback to existing or fallback weather entity
            val cached = database.weatherDao().getWeatherCacheSync()
            if (cached != null) {
                database.cacheMetadataDao().insertOrUpdate(
                    CacheSyncMetadataEntity(
                        cacheKey = "WEATHER",
                        lastSyncedAt = cached.lastUpdated,
                        itemCount = 1 + database.weatherDao().getDailyForecastsSync().size,
                        status = "OFFLINE",
                        cacheLabel = "Offline Room Cache Active",
                        details = "Serving local cached marine & inland forecast."
                    )
                )
                Result.success(cached)
            } else {
                Result.failure(e)
            }
        }
    }

    suspend fun toggleBookmark(articleId: Long, currentBookmarked: Boolean) = withContext(Dispatchers.IO) {
        database.newsDao().updateBookmark(articleId, !currentBookmarked)
    }

    suspend fun toggleFavoriteHotspot(hotspotId: String, currentFav: Boolean) = withContext(Dispatchers.IO) {
        database.hotspotDao().updateFavorite(hotspotId, !currentFav)
    }

    suspend fun refreshDailyNews(): Result<Int> = withContext(Dispatchers.IO) {
        try {
            // Auto update simulation / latest district wire sync
            val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
            val timeString = sdf.format(Date())

            val freshArticles = listOf(
                NewsArticleEntity(
                    title = "Balasore Municipal Corporation Rolls Out Eco-Electric City Shuttles",
                    summary = "New fleet connecting Balasore Railway Station, Station Square, and Remuna Gopinath Temple for commuters.",
                    content = "To facilitate clean transportation for residents and pilgrims, the Balasore Municipality has launched an eco-friendly electric feeder service. The buses run at 15-minute intervals connecting major transit hubs.",
                    category = "Civic & Transport",
                    source = "Balasore Municipal Corporation",
                    publishedAt = "Updated $timeString",
                    isBreaking = false,
                    isBookmarked = false
                ),
                NewsArticleEntity(
                    title = "Bay of Bengal High-Tide Cautionary Siren Installed at Chandipur Promenade",
                    summary = "Automated sensor-based alert sounds 30 minutes before sea returns to safeguard tourists walking the receding sea bed.",
                    content = "In an effort to maximize safety on Chandipur Beach, district coastal security has installed solar sirens synchronized with real-time oceanographic tide gauges. Visitors will hear warning chimes 30 minutes prior to high tide onset.",
                    category = "Coastal & Tourism",
                    source = "Coastal Police & Tourism Desk",
                    publishedAt = "Updated $timeString",
                    isBreaking = true,
                    isBookmarked = false
                ),
                NewsArticleEntity(
                    title = "District Council Passes Resolution on Industrial Park Expansion Near Kuruda",
                    summary = "Political consensus reached on expanding infrastructure and agro-processing clusters in Balasore district.",
                    content = "In the latest district council meeting, elected representatives unanimously passed a resolution approving industrial park modernization near Kuruda. The project aims to attract food processing units and boost rural employment.",
                    category = "Politics",
                    source = "District Press Bureau",
                    publishedAt = "Updated $timeString",
                    isBreaking = false,
                    isBookmarked = false
                ),
                NewsArticleEntity(
                    title = "Annual Balasore Heritage Walk & Cultural Conclave Commences This Weekend",
                    summary = "Heritage enthusiasts, artists, and students gather to explore 10th-century temples and maritime relics.",
                    content = "The annual Balasore Heritage Walk series kicks off this weekend from Fakir Mohan College square. Guided walking tours of historical monuments and evening folk art exhibitions are scheduled across the town.",
                    category = "Events",
                    source = "Balasore Cultural Foundation",
                    publishedAt = "Updated $timeString",
                    isBreaking = false,
                    isBookmarked = false
                )
            )

            database.newsDao().insertArticles(freshArticles)
            val count = database.newsDao().getCount()

            database.cacheMetadataDao().insertOrUpdate(
                CacheSyncMetadataEntity(
                    cacheKey = "NEWS",
                    lastSyncedAt = System.currentTimeMillis(),
                    itemCount = count,
                    status = "FRESH",
                    cacheLabel = "$count Articles Cached in Room",
                    details = "Persisted locally for offline reading & bookmarks."
                )
            )

            Result.success(freshArticles.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Executes comprehensive synchronization of Weather, News, and Tourism data.
     * Caches all data into local Room Database so the app remains fully functional offline.
     */
    suspend fun syncAllData(forceNetwork: Boolean = false): SyncResult = withContext(Dispatchers.IO) {
        initializeIfNeeded()
        var weatherSuccess = false
        var isOffline = false

        // 1. Sync Weather to Room Cache
        val weatherRes = refreshWeatherAndAlerts()
        if (weatherRes.isSuccess) {
            weatherSuccess = true
        } else {
            isOffline = true
        }

        // 2. Sync News to Room Cache
        if (!isOffline) {
            try {
                refreshDailyNews()
            } catch (_: Exception) {
                // Keep existing cached news in Room
            }
        }

        // 3. Tourism Hotspots Cache verification & guarantee
        if (database.hotspotDao().getCount() == 0) {
            database.hotspotDao().insertHotspots(DefaultData.getInitialHotspots())
        }

        val currentNewsCount = database.newsDao().getCount()
        val currentHotspotsCount = database.hotspotDao().getCount()
        val now = System.currentTimeMillis()

        prefs?.edit()?.putLong("last_sync_timestamp", now)?.apply()
        prefs?.edit()?.putString("last_sync_status", if (isOffline) "OFFLINE_CACHE" else "SYNCED")?.apply()

        database.cacheMetadataDao().insertOrUpdate(
            CacheSyncMetadataEntity(
                cacheKey = "TOURISM",
                lastSyncedAt = now,
                itemCount = currentHotspotsCount,
                status = if (isOffline) "OFFLINE" else "FRESH",
                cacheLabel = "$currentHotspotsCount Hotspots in Room",
                details = "Full tourism directory and navigation points available offline."
            )
        )

        SyncResult(
            isSuccess = true,
            weatherUpdated = weatherSuccess,
            newsArticlesCount = currentNewsCount,
            hotspotsCount = currentHotspotsCount,
            timestamp = now,
            isOfflineServed = isOffline,
            message = if (isOffline) {
                "Offline mode active: Displaying $currentNewsCount news & $currentHotspotsCount hotspots cached in Room."
            } else {
                "Background sync complete: Weather, $currentNewsCount news articles, & $currentHotspotsCount tourism hotspots cached in Room."
            }
        )
    }

    fun searchNewsOffline(query: String): Flow<List<NewsArticleEntity>> =
        if (query.isBlank()) database.newsDao().getAllNews() else database.newsDao().searchNews(query.trim())

    fun searchHotspotsOffline(query: String): Flow<List<HotspotEntity>> =
        if (query.isBlank()) database.hotspotDao().getAllHotspots() else database.hotspotDao().searchHotspots(query.trim())

    fun getArticleById(id: Long): Flow<NewsArticleEntity?> = database.newsDao().getArticleById(id)

    fun getHotspotById(id: String): Flow<HotspotEntity?> = database.hotspotDao().getHotspotById(id)

    fun getCacheMetadata(key: String): Flow<CacheSyncMetadataEntity?> = database.cacheMetadataDao().getMetadata(key)

    private fun createInitialDailyForecasts(): List<DailyForecastEntity> {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
        val list = mutableListOf<DailyForecastEntity>()

        val sampleTemps = listOf(
            Pair(32.0, 25.0) to Pair(1, "Mainly Clear Coastal Sky"),
            Pair(33.5, 26.0) to Pair(2, "Partly Cloudy Sea Breeze"),
            Pair(31.0, 24.5) to Pair(61, "Light Coastal Passing Shower"),
            Pair(30.5, 24.0) to Pair(80, "Scattered Marine Rain Showers"),
            Pair(32.5, 25.5) to Pair(0, "Clear Sunny Day"),
            Pair(34.0, 26.5) to Pair(1, "Mainly Clear"),
            Pair(33.0, 25.0) to Pair(2, "Partly Cloudy")
        )

        for (i in 0..6) {
            val dateStr = dateFormat.format(calendar.time)
            val dayName = if (i == 0) "Today" else dayFormat.format(calendar.time)
            val (tempPair, weatherPair) = sampleTemps[i % sampleTemps.size]

            list.add(
                DailyForecastEntity(
                    date = dateStr,
                    dayOfWeek = dayName,
                    weatherCode = weatherPair.first,
                    weatherDescription = weatherPair.second,
                    maxTemp = tempPair.first,
                    minTemp = tempPair.second,
                    sunrise = "05:33 AM",
                    sunset = "06:13 PM",
                    uvIndex = (7.2 - (i * 0.3)).coerceAtLeast(3.0),
                    cachedAt = System.currentTimeMillis()
                )
            )
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        return list
    }

    fun getLastSyncTimestamp(): Long {
        return prefs?.getLong("last_sync_timestamp", 0L) ?: 0L
    }

    fun getLastSyncStatus(): String {
        return prefs?.getString("last_sync_status", "NEVER") ?: "NEVER"
    }

    private fun parseWeatherCode(code: Int): String {
        return when (code) {
            0 -> "Clear Sky & Sunshine"
            1, 2, 3 -> "Partly Cloudy Coastal Sky"
            45, 48 -> "Foggy Morning / Coastal Mist"
            51, 53, 55 -> "Light Coastal Drizzle"
            61, 63, 65 -> "Moderate to Heavy Rain"
            71, 73, 75 -> "Overcast & Hail"
            80, 81, 82 -> "Passing Coastal Showers"
            95 -> "Thunderstorm & Lightning"
            96, 99 -> "Severe Thunderstorm with Hail"
            else -> "Fair Weather"
        }
    }

    private fun evaluateCoastalAlert(code: Int, windSpeed: Double, gusts: Double, temp: Double): Triple<String, String, String> {
        return when {
            windSpeed > 45 || gusts > 60 || code in listOf(95, 96, 99) -> Triple(
                "CYCLONE_ALERT",
                "Severe Weather & High Wind Warning",
                "High wind gusts and thunderstorm activity detected in North Bay of Bengal. Fishermen advised not to venture into deep sea. Keep clear of Chandipur shoreline."
            )
            windSpeed > 30 || gusts > 40 || code in listOf(63, 65, 82) -> Triple(
                "WARNING",
                "Coastal Wind & Heavy Shower Advisory",
                "Choppy sea conditions and gusty coastal breezes reported. Small watercraft and speedboats should exercise caution near Balaramgadi estuary."
            )
            temp > 38.0 -> Triple(
                "ADVISORY",
                "High Heat & Humidity Alert",
                "Afternoon temperature and humidity levels are elevated across Balasore district. Stay well hydrated and avoid prolonged outdoor sun exposure between 12 PM - 3 PM."
            )
            else -> Triple(
                "NORMAL",
                "Favorable Coastal Weather",
                "Normal sea breeze and clear atmospheric conditions along the Balasore shoreline. Safe for outdoor tourism, heritage visits, and Chandipur beach strolls."
            )
        }
    }

    private fun calculateTideState(): Pair<String, String> {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val totalMinutes = hour * 60 + minute

        // Semi-diurnal cycle simulation for Chandipur (~12.4 hours)
        // Typical daytime low tide around 10:30 AM and 11:00 PM
        val morningLow = 10 * 60 + 30
        val eveningHigh = 16 * 60 + 45

        return when {
            totalMinutes in (morningLow - 90)..(morningLow + 90) -> Pair(
                "LOW_TIDE",
                "Sea has receded up to 4.5 km into the Bay. Safest time for walking on the vast muddy seabed and spotting red crabs."
            )
            totalMinutes in (morningLow + 91)..(eveningHigh - 60) -> Pair(
                "INCOMING",
                "Sea waters are returning back to shore. Lifeguards advise tourists on the seabed to return towards the seawall."
            )
            totalMinutes in (eveningHigh - 59)..(eveningHigh + 120) -> Pair(
                "HIGH_TIDE",
                "High Tide: Water levels are high near the shoreline. Beautiful gentle waves lapping the promenade."
            )
            else -> Pair(
                "RECEDING",
                "Tide is receding outwards into the Bay of Bengal. Vanishing sea phenomenon beginning to unfold."
            )
        }
    }

    private fun formatTimeIso(isoString: String): String {
        return try {
            val parts = isoString.split("T")
            if (parts.size > 1) {
                val timeParts = parts[1].split(":")
                val h = timeParts[0].toIntOrNull() ?: 0
                val m = timeParts[1].toIntOrNull() ?: 0
                val ampm = if (h >= 12) "PM" else "AM"
                val h12 = if (h == 0) 12 else if (h > 12) h - 12 else h
                String.format(Locale.US, "%02d:%02d %s", h12, m, ampm)
            } else {
                isoString
            }
        } catch (_: Exception) {
            isoString
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: BalasoreRepository? = null

        fun getInstance(context: Context): BalasoreRepository {
            return INSTANCE ?: synchronized(this) {
                val db = AppDatabase.getInstance(context)
                val repo = BalasoreRepository(db, context = context.applicationContext)
                INSTANCE = repo
                repo
            }
        }
    }
}
