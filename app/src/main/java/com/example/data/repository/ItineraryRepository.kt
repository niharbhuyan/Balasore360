package com.example.data.repository

import com.example.data.local.ItineraryDao
import com.example.data.local.ItineraryItemEntity
import com.example.data.model.Hotspot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ItineraryRepository(
    private val itineraryDao: ItineraryDao
) {
    val allItineraryItems: Flow<List<ItineraryItemEntity>> = itineraryDao.getAllItineraryItems()

    suspend fun ensureDefaultItinerarySeeded() = withContext(Dispatchers.IO) {
        val count = itineraryDao.getItineraryCount()
        if (count == 0) {
            val defaults = listOf(
                ItineraryItemEntity(
                    hotspotId = "khirachora_temple",
                    hotspotName = "Khirachora Gopinatha Temple",
                    odiaName = "କ୍ଷୀରଚୋରା ଗୋପୀନାଥ ମନ୍ଦିର",
                    category = "Temple",
                    dayNumber = 1,
                    timeSlot = "08:30 AM",
                    notes = "Taste divine Amrita Keli prasad & admire 12th-century Kalinga architecture.",
                    latitude = 21.5284,
                    longitude = 86.8647,
                    weatherCondition = "Sunny Morning",
                    weatherIconUrl = "https://cdn.weatherapi.com/weather/64x64/day/113.png",
                    tempC = 27.5
                ),
                ItineraryItemEntity(
                    hotspotId = "chandipur_beach",
                    hotspotName = "Chandipur Beach & Vanishing Sea",
                    odiaName = "ଚାନ୍ଦିପୁର ବେଳାଭୂମି",
                    category = "Beach",
                    dayNumber = 1,
                    timeSlot = "02:30 PM",
                    notes = "Walk 4 km onto the exposed seabed at low tide & spot living fossil horseshoe crabs.",
                    latitude = 21.4682,
                    longitude = 87.0163,
                    weatherCondition = "Coastal Breeze",
                    weatherIconUrl = "https://cdn.weatherapi.com/weather/64x64/day/116.png",
                    tempC = 29.8
                ),
                ItineraryItemEntity(
                    hotspotId = "emami_jagannath_temple",
                    hotspotName = "Emami Jagannath Temple",
                    odiaName = "ଏମାମି ଜଗନ୍ନାଥ ମନ୍ଦିର",
                    category = "Temple",
                    dayNumber = 1,
                    timeSlot = "05:45 PM",
                    notes = "Witness evening sand art & sunset aarti along the illuminated temple pond.",
                    latitude = 21.5034,
                    longitude = 86.9150,
                    weatherCondition = "Clear Evening",
                    weatherIconUrl = "https://cdn.weatherapi.com/weather/64x64/day/113.png",
                    tempC = 28.0
                )
            )
            itineraryDao.insertItineraryItems(defaults)
        }
    }

    suspend fun addToItinerary(
        hotspot: Hotspot,
        dayNumber: Int,
        timeSlot: String,
        notes: String = ""
    ): Long = withContext(Dispatchers.IO) {
        val (condition, iconUrl, temp) = determineWeatherForHotspot(hotspot)
        val coords = getHotspotCoordinates(hotspot)
        val item = ItineraryItemEntity(
            hotspotId = hotspot.id,
            hotspotName = hotspot.name,
            odiaName = hotspot.odiaName,
            category = hotspot.category,
            dayNumber = dayNumber,
            timeSlot = timeSlot,
            notes = notes.ifBlank { hotspot.description.take(80) },
            latitude = coords.first,
            longitude = coords.second,
            weatherCondition = condition,
            weatherIconUrl = iconUrl,
            tempC = temp
        )
        itineraryDao.insertItineraryItem(item)
    }

    private fun getHotspotCoordinates(hotspot: Hotspot): Pair<Double, Double> {
        return when {
            hotspot.id.contains("chandipur", ignoreCase = true) || hotspot.name.contains("Chandipur", ignoreCase = true) -> Pair(21.4674, 87.0242)
            hotspot.id.contains("remuna", ignoreCase = true) || hotspot.name.contains("Gopinatha", ignoreCase = true) -> Pair(21.5273, 86.8722)
            hotspot.id.contains("talasari", ignoreCase = true) || hotspot.name.contains("Talasari", ignoreCase = true) -> Pair(21.5972, 87.4578)
            hotspot.id.contains("emami", ignoreCase = true) || hotspot.name.contains("Emami", ignoreCase = true) -> Pair(21.4878, 86.9189)
            hotspot.id.contains("kuldiha", ignoreCase = true) || hotspot.name.contains("Kuldiha", ignoreCase = true) -> Pair(21.4167, 86.6667)
            hotspot.id.contains("panchalingeswar", ignoreCase = true) || hotspot.name.contains("Panchalingeswar", ignoreCase = true) -> Pair(21.4285, 86.7214)
            else -> Pair(21.4934, 86.9325)
        }
    }

    suspend fun removeItem(id: Long): Int = withContext(Dispatchers.IO) {
        itineraryDao.deleteItineraryItemById(id)
    }

    suspend fun removeByHotspotId(hotspotId: String): Int = withContext(Dispatchers.IO) {
        itineraryDao.deleteByHotspotId(hotspotId)
    }

    suspend fun clearAll(): Int = withContext(Dispatchers.IO) {
        itineraryDao.clearAllItinerary()
    }

    private fun determineWeatherForHotspot(hotspot: Hotspot): Triple<String, String, Double> {
        return when (hotspot.category.lowercase()) {
            "beach" -> Triple(
                "Coastal Breeze",
                "https://cdn.weatherapi.com/weather/64x64/day/116.png",
                29.5
            )
            "wildlife" -> Triple(
                "Misty Forest Canopy",
                "https://cdn.weatherapi.com/weather/64x64/day/143.png",
                26.8
            )
            "port" -> Triple(
                "Oceanic Wind",
                "https://cdn.weatherapi.com/weather/64x64/day/119.png",
                30.2
            )
            else -> Triple(
                "Sunny & Pleasant",
                "https://cdn.weatherapi.com/weather/64x64/day/113.png",
                28.4
            )
        }
    }
}
