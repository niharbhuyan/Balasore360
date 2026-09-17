package com.example.data.repository

import com.example.data.local.TravelJournalDao
import com.example.data.local.TravelJournalEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TravelJournalRepository(
    private val travelJournalDao: TravelJournalDao
) {
    val allJournals: Flow<List<TravelJournalEntity>> = travelJournalDao.getAllJournals()

    suspend fun ensureDefaultJournalSeeded() = withContext(Dispatchers.IO) {
        val count = travelJournalDao.getJournalCount()
        if (count == 0) {
            val defaults = listOf(
                TravelJournalEntity(
                    title = "Walking onto the Vanishing Seabed",
                    locationName = "Chandipur Beach",
                    category = "Beach",
                    rating = 5,
                    notes = "Incredible experience walking almost 3 kilometers out into the dry sea bed at low tide! Discovered horseshoe crabs gliding in shallow pools and watched local fishermen harvesting mud crabs.",
                    colorTag = 0xFF0284C7,
                    visitedDate = System.currentTimeMillis() - 86400000L
                ),
                TravelJournalEntity(
                    title = "Sweet Blessing of Amrita Keli",
                    locationName = "Khirachora Gopinath Temple, Remuna",
                    category = "Temple",
                    rating = 5,
                    notes = "Tried the legendary condensed milk prasad in traditional earthen pots. The cardamom flavor and thick golden texture are unforgettable. The temple priest shared stories of Madhavendra Puri.",
                    colorTag = 0xFFD97706,
                    visitedDate = System.currentTimeMillis() - 172800000L
                ),
                TravelJournalEntity(
                    title = "Elephant Herd at Kuldiha Watchtower",
                    locationName = "Kuldiha Wildlife Sanctuary",
                    category = "Wildlife",
                    rating = 5,
                    notes = "Spotted a mother elephant and calf near Risa waterhole around 4:30 PM. Also saw Malabar giant squirrels leaping between Sal trees. Nilagiri hills in the backdrop were stunning.",
                    colorTag = 0xFF059669,
                    visitedDate = System.currentTimeMillis() - 259200000L
                )
            )
            travelJournalDao.insertJournals(defaults)
        }
    }

    suspend fun addJournal(
        title: String,
        locationName: String,
        category: String,
        rating: Int,
        notes: String,
        colorTag: Long
    ) = withContext(Dispatchers.IO) {
        val journal = TravelJournalEntity(
            title = title,
            locationName = locationName,
            category = category,
            rating = rating,
            notes = notes,
            colorTag = colorTag,
            visitedDate = System.currentTimeMillis()
        )
        travelJournalDao.insertJournal(journal)
    }

    suspend fun deleteJournalById(id: Long) = withContext(Dispatchers.IO) {
        travelJournalDao.deleteJournalById(id)
    }
}
