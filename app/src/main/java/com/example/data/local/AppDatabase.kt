package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        NewsArticleEntity::class,
        WeatherCacheEntity::class,
        DailyForecastEntity::class,
        HotspotEntity::class,
        CacheSyncMetadataEntity::class,
        UserEntity::class,
        ReviewEntity::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Abstract getter method for News DAO to enable CRUD operations for local news and district bulletins.
     */
    abstract fun newsDao(): NewsDao

    /**
     * Abstract getter method for Tourism DAO to enable CRUD operations for tourism destinations, heritage spots, and beaches.
     */
    abstract fun tourismDao(): TourismDao

    /**
     * Hotspot DAO getter implemented to delegate to tourismDao for backward compatibility.
     */
    fun hotspotDao(): HotspotDao = tourismDao()

    /**
     * Abstract getter method for Weather & marine forecast DAO.
     */
    abstract fun weatherDao(): WeatherDao

    /**
     * Abstract getter method for cache synchronization metadata.
     */
    abstract fun cacheMetadataDao(): CacheMetadataDao

    /**
     * Abstract getter method for user credentials and profiles.
     */
    abstract fun userDao(): UserDao

    /**
     * Abstract getter method for local community ratings and reviews.
     */
    abstract fun reviewDao(): ReviewDao

    companion object {
        private const val DATABASE_NAME = "balasore_app_db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).fallbackToDestructiveMigration(dropAllTables = true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
