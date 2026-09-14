package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.local.HotspotEntity
import com.example.data.repository.DefaultData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class BalasoreHotspotRoomTest {

    private lateinit var database: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testHotspotInsertionAndRetrievalWithDescriptions() {
        runBlocking {
            val initialHotspots = DefaultData.getInitialHotspots()
            assertTrue("Initial hotspots should not be empty", initialHotspots.isNotEmpty())

            // Insert hotspots into Room
            database.hotspotDao().insertHotspots(initialHotspots)

            // Retrieve all hotspots from Room
            val loaded = database.hotspotDao().getAllHotspots().first()
            assertEquals(initialHotspots.size, loaded.size)

            // Verify Chandipur Beach landmark and its descriptions
            val chandipur = loaded.find { it.id == "chandipur_beach" }
            assertNotNull("Chandipur Beach must exist in Room", chandipur)
            chandipur?.let {
                assertEquals("Chandipur Beach (Vanishing Sea)", it.name)
                assertTrue("Description must be populated", it.shortDescription.isNotEmpty())
                assertTrue("Full description must describe the vanishing sea", it.fullDescription.contains("Vanishing Sea") || it.fullDescription.contains("low tide"))
                assertEquals("Beach", it.category)
                assertFalse("Default favorite state is false", it.isFavorite)
            }
        }
    }

    @Test
    fun testToggleFavoritePersistenceInRoom() {
        runBlocking {
            val initialHotspots = DefaultData.getInitialHotspots()
            database.hotspotDao().insertHotspots(initialHotspots)

            val testSpotId = "khirachora_gopinatha"
            val initialSpot = database.hotspotDao().getHotspotByIdSync(testSpotId)
            assertNotNull(initialSpot)
            assertFalse(initialSpot!!.isFavorite)

            // Toggle favorite to TRUE in Room
            database.hotspotDao().updateFavorite(testSpotId, true)

            // Verify persistent update in Room
            val updatedSpot = database.hotspotDao().getHotspotByIdSync(testSpotId)
            assertNotNull(updatedSpot)
            assertTrue("Hotspot should be marked favorite in Room", updatedSpot!!.isFavorite)

            // Verify favorite query filter returns the newly favorited landmark
            val favorites = database.hotspotDao().getFavoriteHotspots().first()
            assertEquals(1, favorites.size)
            assertEquals(testSpotId, favorites[0].id)
            assertTrue(favorites[0].name.contains("Khirachora"))

            // Toggle favorite back to FALSE in Room
            database.hotspotDao().updateFavorite(testSpotId, false)
            val unfavoritedSpot = database.hotspotDao().getHotspotByIdSync(testSpotId)
            assertFalse("Hotspot should no longer be favorite", unfavoritedSpot!!.isFavorite)

            val emptyFavorites = database.hotspotDao().getFavoriteHotspots().first()
            assertTrue("Favorites list should be empty after untoggling", emptyFavorites.isEmpty())
        }
    }
}
