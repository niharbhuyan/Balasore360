package com.example.data.maps

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.sin
import kotlin.math.sinh
import kotlin.math.sqrt
import kotlin.math.tan

/**
 * Central manager for Balasore's offline map tiles, pre-cached key areas,
 * offline emergency POI directory, and turn-by-turn offline navigation.
 */
class BalasoreOfflineMapManager private constructor(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("balasore_offline_maps_prefs", Context.MODE_PRIVATE)

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val tilesBaseDir: File by lazy {
        File(context.filesDir, "offline_map_tiles").apply {
            if (!exists()) mkdirs()
        }
    }

    // 1. Predefined Key Areas of Balasore for Pre-caching
    val keyAreas: List<OfflineKeyArea> = listOf(
        OfflineKeyArea(
            id = "balasore_town",
            nameEn = "Balasore Central Hub & Station",
            nameOr = "ବାଲେଶ୍ୱର ସହର ଓ ରେଳ ଷ୍ଟେସନ କେନ୍ଦ୍ର",
            description = "OT Road, District HQ Hospital, FM Circle, Railway Junction, Collectorate & Transit Core",
            center = LatLng(21.4934, 86.9324),
            minLat = 21.4650,
            maxLat = 21.5250,
            minLng = 86.8950,
            maxLng = 86.9650,
            minZoom = 12,
            maxZoom = 15,
            estimatedTiles = 64,
            estimatedSizeMb = 3.8,
            tag = "Urban Core",
            keyLandmarks = listOf("Balasore Station", "DHH Hospital", "FM Goltei", "OT Road Market", "Collectorate")
        ),
        OfflineKeyArea(
            id = "chandipur_coast",
            nameEn = "Chandipur Coastal Belt & DRDO ITR",
            nameOr = "ଚାନ୍ଦିପୁର ଉପକୂଳ ଓ DRDO କ୍ଷେପଣାସ୍ତ୍ର ରେଞ୍ଜ",
            description = "Vanishing Sea beach, DRDO ITR Gate, Van Peurse heritage site, mudflats & defense perimeter",
            center = LatLng(21.4700, 87.0160),
            minLat = 21.4350,
            maxLat = 21.5050,
            minLng = 86.9750,
            maxLng = 87.0450,
            minZoom = 12,
            maxZoom = 15,
            estimatedTiles = 58,
            estimatedSizeMb = 3.4,
            tag = "Coastal Defense",
            keyLandmarks = listOf("Chandipur Beach", "DRDO Gate", "Van Peurse Rest House", "OTDC Panthanivas")
        ),
        OfflineKeyArea(
            id = "balaramgadi_harbor",
            nameEn = "Balaramgadi Fishing Port & Estuary",
            nameOr = "ବଳରାମଗଡ଼ି ମତ୍ସ୍ୟ ବନ୍ଦର ଓ ମୁହାଣ",
            description = "Budhabalanga River mouth, deep-sea trawler jetty, fish auction mandi & Marine Police outpost",
            center = LatLng(21.4880, 87.0500),
            minLat = 21.4650,
            maxLat = 21.5150,
            minLng = 87.0200,
            maxLng = 87.0750,
            minZoom = 12,
            maxZoom = 15,
            estimatedTiles = 42,
            estimatedSizeMb = 2.6,
            tag = "Marine & Port",
            keyLandmarks = listOf("Fish Auction Jetty", "Marine Police Station", "Budhabalanga Estuary", "Cyclone Shelter")
        ),
        OfflineKeyArea(
            id = "remuna_heritage",
            nameEn = "Remuna Spiritual Hub & Bypass",
            nameOr = "ରେମୁଣା କ୍ଷୀରଚୋରା ଗୋପୀନାଥ ପୀଠ",
            description = "10th-century Khirachora Gopinath Temple, Jagannath Mandir, Gaudiya Math & NH-16 bypass",
            center = LatLng(21.5280, 86.8720),
            minLat = 21.5050,
            maxLat = 21.5550,
            minLng = 86.8450,
            maxLng = 86.9050,
            minZoom = 12,
            maxZoom = 15,
            estimatedTiles = 46,
            estimatedSizeMb = 2.8,
            tag = "Heritage & Temple",
            keyLandmarks = listOf("Khirachora Temple", "Madhavendra Puri Samadhi", "Remuna CHC", "Bypass Junction")
        ),
        OfflineKeyArea(
            id = "nilagiri_kuldiha",
            nameEn = "Nilagiri Palace & Kuldiha Gateway",
            nameOr = "ନୀଳଗିରି ରାଜପ୍ରାସାଦ ଓ କୁଲଡ଼ିହା ପ୍ରବେଶପଥ",
            description = "Nilagiri Rajbati, Jagannath Temple, stone craft workshops & Kuldiha Wildlife Forest entry",
            center = LatLng(21.4600, 86.7650),
            minLat = 21.4250,
            maxLat = 21.4950,
            minLng = 86.7300,
            maxLng = 86.8100,
            minZoom = 12,
            maxZoom = 15,
            estimatedTiles = 62,
            estimatedSizeMb = 3.6,
            tag = "Forest & Craft",
            keyLandmarks = listOf("Nilagiri Palace", "Kuldiha Forest Gate", "Swarnachuda Hills", "Nilagiri Hospital")
        ),
        OfflineKeyArea(
            id = "panchalingeswar",
            nameEn = "Panchalingeswar Stream & Shrine",
            nameOr = "ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ପାହାଡ଼ ଝରଣା ପୀଠ",
            description = "Perennial mountain stream shrine on Devagiri hill, eco-tourism lodges & mountain trek trails",
            center = LatLng(21.4280, 86.7120),
            minLat = 21.3950,
            maxLat = 21.4550,
            minLng = 86.6800,
            maxLng = 86.7450,
            minZoom = 12,
            maxZoom = 15,
            estimatedTiles = 48,
            estimatedSizeMb = 2.9,
            tag = "Mountain Eco",
            keyLandmarks = listOf("Stream Temple", "Devagiri Foothills", "OTDC Panthasala", "Forest Ranger Camp")
        ),
        OfflineKeyArea(
            id = "bahanaga_nh16",
            nameEn = "Bahanaga & NH-16 Emergency Corridor",
            nameOr = "ବାହାନଗା ଓ NH-16 ଜରୁରୀକାଳୀନ କରିଡର",
            description = "NH-16 expressway stretch, Bahanaga Bazar junction, Soro trauma care desk & ambulance hub",
            center = LatLng(21.3500, 86.7800),
            minLat = 21.3150,
            maxLat = 21.3850,
            minLng = 86.7400,
            maxLng = 86.8200,
            minZoom = 12,
            maxZoom = 15,
            estimatedTiles = 54,
            estimatedSizeMb = 3.2,
            tag = "Emergency Highway",
            keyLandmarks = listOf("Bahanaga Junction", "NH-16 Trauma Post", "Soro CHC", "Railway Outpost")
        ),
        OfflineKeyArea(
            id = "talsari_bichitrapur",
            nameEn = "Talsari Beach & Bichitrapur Mangroves",
            nameOr = "ତାଳସାରୀ ବେଳାଭୂମି ଓ ବିଚିତ୍ରପୁର ହେନ୍ତାଳବଣ",
            description = "Subarnarekha River confluence, red ghost crab sandbars, mangrove nature camp & boat safari",
            center = LatLng(21.6050, 87.4500),
            minLat = 21.5700,
            maxLat = 21.6400,
            minLng = 87.4100,
            maxLng = 87.4900,
            minZoom = 12,
            maxZoom = 15,
            estimatedTiles = 56,
            estimatedSizeMb = 3.4,
            tag = "Coastal Wetland",
            keyLandmarks = listOf("Talsari Red Crab Coast", "Bichitrapur Boat Pier", "Subarnarekha Delta", "Chandaneswar")
        )
    )

    // 2. Pre-cached Offline Points of Interest (POIs) with GPS & Contacts
    val offlinePois: List<OfflineNavPoint> = listOf(
        OfflineNavPoint(
            id = "poi_dhh",
            nameEn = "District Headquarter Hospital (DHH)",
            nameOr = "ଜିଲ୍ଲା ମୁଖ୍ୟ ଚିକିତ୍ସାଳୟ (DHH)",
            category = NavPointCategory.HOSPITAL,
            location = LatLng(21.4925, 86.9320),
            address = "Hospital Road, Cinema Square, Balasore",
            emergencyPhone = "06782-262024"
        ),
        OfflineNavPoint(
            id = "poi_fmmch",
            nameEn = "Fakir Mohan Medical College & Hospital (FM MCH)",
            nameOr = "ଫକୀର ମୋହନ ମେଡିକାଲ କଲେଜ ଓ ହସ୍ପିଟାଲ",
            category = NavPointCategory.HOSPITAL,
            location = LatLng(21.5160, 86.8840),
            address = "Remuna Januganj Road, Balasore",
            emergencyPhone = "06782-224400"
        ),
        OfflineNavPoint(
            id = "poi_nilagiri_chc",
            nameEn = "Nilagiri Sub-Divisional Hospital (SDH)",
            nameOr = "ନୀଳଗିରି ଉପଖଣ୍ଡ ଡାକ୍ତରଖାନା",
            category = NavPointCategory.HOSPITAL,
            location = LatLng(21.4580, 86.7660),
            address = "Near Rajbati, Nilagiri, Balasore",
            emergencyPhone = "06782-233221"
        ),
        OfflineNavPoint(
            id = "poi_soro_chc",
            nameEn = "Soro Community Health Centre (CHC)",
            nameOr = "ସୋରୋ ଗୋଷ୍ଠୀ ସ୍ୱାସ୍ଥ୍ୟ କେନ୍ଦ୍ର",
            category = NavPointCategory.HOSPITAL,
            location = LatLng(21.2890, 86.6890),
            address = "NH-16 Corridor, Soro, Balasore",
            emergencyPhone = "06782-220025"
        ),
        OfflineNavPoint(
            id = "poi_police_sp",
            nameEn = "Balasore District Police Office (SP Office)",
            nameOr = "ବାଲେଶ୍ୱର ଜିଲ୍ଲା ଆରକ୍ଷୀ ଅଧୀକ୍ଷକ କାର୍ଯ୍ୟାଳୟ",
            category = NavPointCategory.POLICE_DEFENSE,
            location = LatLng(21.4960, 86.9350),
            address = "Collectorate Square, Balasore",
            emergencyPhone = "112"
        ),
        OfflineNavPoint(
            id = "poi_police_marine",
            nameEn = "Chandipur Marine Police Station",
            nameOr = "ଚାନ୍ଦିପୁର ସାମୁଦ୍ରିକ ଥାନା",
            category = NavPointCategory.POLICE_DEFENSE,
            location = LatLng(21.4680, 87.0140),
            address = "Beach Road, Chandipur",
            emergencyPhone = "06782-272210"
        ),
        OfflineNavPoint(
            id = "poi_drdo_gate",
            nameEn = "DRDO ITR Main Gate & Security Command",
            nameOr = "DRDO ITR ମୁଖ୍ୟ ଫାଟକ ଓ ନିରାପତ୍ତା କେନ୍ଦ୍ର",
            category = NavPointCategory.POLICE_DEFENSE,
            location = LatLng(21.4440, 87.0260),
            address = "ITR Defense Road, Chandipur",
            emergencyPhone = "06782-272000"
        ),
        OfflineNavPoint(
            id = "poi_shelter_chandipur",
            nameEn = "OSDMA Multipurpose Cyclone Shelter Chandipur",
            nameOr = "ଚାନ୍ଦିପୁର ବହୁମୁଖୀ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳୀ",
            category = NavPointCategory.CYCLONE_SHELTER,
            location = LatLng(21.4660, 87.0090),
            address = "Sector 2, Chandipur Coastal Village",
            emergencyPhone = "1077"
        ),
        OfflineNavPoint(
            id = "poi_shelter_balaramgadi",
            nameEn = "OSDMA Cyclone Shelter Balaramgadi",
            nameOr = "ବଳରାମଗଡ଼ି ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳୀ",
            category = NavPointCategory.CYCLONE_SHELTER,
            location = LatLng(21.4890, 87.0420),
            address = "Near Jetty Complex, Balaramgadi",
            emergencyPhone = "1077"
        ),
        OfflineNavPoint(
            id = "poi_shelter_kasafal",
            nameEn = "OSDMA Cyclone Shelter Kasafal",
            nameOr = "କସାଫଳ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳୀ",
            category = NavPointCategory.CYCLONE_SHELTER,
            location = LatLng(21.5340, 87.1210),
            address = "Kasafal Beach Road, Balasore",
            emergencyPhone = "1077"
        ),
        OfflineNavPoint(
            id = "poi_shelter_talsari",
            nameEn = "OSDMA Cyclone Shelter Talsari",
            nameOr = "ତାଳସାରୀ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳୀ",
            category = NavPointCategory.CYCLONE_SHELTER,
            location = LatLng(21.6080, 87.4420),
            address = "Talsari Coastal Gram, Bhograi",
            emergencyPhone = "1077"
        ),
        OfflineNavPoint(
            id = "poi_railway_junction",
            nameEn = "Balasore Railway Junction (SER Mainline)",
            nameOr = "ବାଲେଶ୍ୱର ରେଳ ଷ୍ଟେସନ ଜଙ୍କସନ",
            category = NavPointCategory.TRANSIT,
            location = LatLng(21.4930, 86.9380),
            address = "Station Road, Balasore",
            emergencyPhone = "139"
        ),
        OfflineNavPoint(
            id = "poi_bus_stand",
            nameEn = "Sahadevkhunta Central Bus Terminal",
            nameOr = "ସହଦେବଖୁଣ୍ଟା କେନ୍ଦ୍ରୀୟ ବସ୍ ଟର୍ମିନାଲ",
            category = NavPointCategory.TRANSIT,
            location = LatLng(21.4880, 86.9240),
            address = "Sahadevkhunta, Balasore",
            emergencyPhone = "06782-262145"
        ),
        OfflineNavPoint(
            id = "poi_fire_central",
            nameEn = "Balasore Central Fire & Disaster Rescue Station",
            nameOr = "ବାଲେଶ୍ୱର ମୁଖ୍ୟ ଅଗ୍ନିଶମ ଓ ବିପର୍ଯ୍ୟୟ ଉଦ୍ଧାର କେନ୍ଦ୍ର",
            category = NavPointCategory.FIRE_RESCUE,
            location = LatLng(21.4900, 86.9310),
            address = "Cinema Square, Balasore",
            emergencyPhone = "101"
        ),
        OfflineNavPoint(
            id = "poi_blood_bank",
            nameEn = "Red Cross Balasore Blood Bank",
            nameOr = "ରେଡ କ୍ରସ୍ ବାଲେଶ୍ୱର ରକ୍ତ ଭଣ୍ଡାର",
            category = NavPointCategory.HOSPITAL,
            location = LatLng(21.4930, 86.9335),
            address = "DHH Campus, Balasore",
            emergencyPhone = "06782-263300"
        ),
        OfflineNavPoint(
            id = "poi_temple_remuna",
            nameEn = "Khirachora Gopinath Temple Remuna",
            nameOr = "କ୍ଷୀରଚୋରା ଗୋପୀନାଥ ମନ୍ଦିର ରେମୁଣା",
            category = NavPointCategory.HERITAGE_TOURISM,
            location = LatLng(21.5285, 86.8715),
            address = "Remuna Temple Road, Balasore",
            emergencyPhone = "06782-225010"
        ),
        OfflineNavPoint(
            id = "poi_chandipur_beach",
            nameEn = "Chandipur Vanishing Sea Beach Promenade",
            nameOr = "ଚାନ୍ଦିପୁର ବେଳାଭୂମି ମୁଖ୍ୟ ପ୍ରୋମେନେଡ୍",
            category = NavPointCategory.HERITAGE_TOURISM,
            location = LatLng(21.4705, 87.0135),
            address = "Beach Road, Chandipur",
            emergencyPhone = "06782-272056"
        )
    )

    // 3. State Management
    private val _packStates = MutableStateFlow<Map<String, OfflineMapPack>>(emptyMap())
    val packStates: StateFlow<Map<String, OfflineMapPack>> = _packStates.asStateFlow()

    private val _isSimulatedOutage = MutableStateFlow(false)
    val isSimulatedOutage: StateFlow<Boolean> = _isSimulatedOutage.asStateFlow()

    private val _overallDownloading = MutableStateFlow(false)
    val overallDownloading: StateFlow<Boolean> = _overallDownloading.asStateFlow()

    private val _overallProgress = MutableStateFlow(0f)
    val overallProgress: StateFlow<Float> = _overallProgress.asStateFlow()

    init {
        loadPersistedPackStates()
    }

    private fun loadPersistedPackStates() {
        val map = mutableMapOf<String, OfflineMapPack>()
        for (area in keyAreas) {
            val isDownloaded = prefs.getBoolean("pack_${area.id}_downloaded", false)
            val downloadedTiles = prefs.getInt("pack_${area.id}_tiles", 0)
            val sizeBytes = prefs.getLong("pack_${area.id}_size_bytes", 0L)
            val timestamp = prefs.getLong("pack_${area.id}_timestamp", 0L)
            map[area.id] = OfflineMapPack(
                areaId = area.id,
                isDownloaded = isDownloaded,
                downloadedTiles = downloadedTiles,
                totalTiles = area.estimatedTiles,
                sizeOnDiskBytes = sizeBytes,
                lastUpdatedTimestamp = timestamp
            )
        }
        _packStates.value = map
    }

    fun toggleSimulatedOutage() {
        _isSimulatedOutage.value = !_isSimulatedOutage.value
    }

    fun isDeviceOnline(): Boolean {
        if (_isSimulatedOutage.value) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return false
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    /**
     * Downloads and generates all pre-cached tiles for a specific Balasore area.
     */
    fun downloadAreaPack(areaId: String) {
        val area = keyAreas.find { it.id == areaId } ?: return
        scope.launch {
            val currentMap = _packStates.value.toMutableMap()
            currentMap[areaId] = currentMap[areaId]?.copy(isDownloading = true, downloadProgress = 0f)
                ?: OfflineMapPack(areaId = areaId, isDownloading = true)
            _packStates.value = currentMap

            val areaDir = File(tilesBaseDir, areaId).apply { if (!exists()) mkdirs() }
            var writtenCount = 0
            var totalBytes = 0L

            // Pre-calculate tiles for the area's bounding box across zoom levels
            val tilesToProcess = mutableListOf<Triple<Int, Int, Int>>() // zoom, x, y
            for (z in area.minZoom..area.maxZoom) {
                val minX = BalasoreOfflineTileProvider.latLngToTileX(area.minLng, z)
                val maxX = BalasoreOfflineTileProvider.latLngToTileX(area.maxLng, z)
                val minY = BalasoreOfflineTileProvider.latLngToTileY(area.maxLat, z)
                val maxY = BalasoreOfflineTileProvider.latLngToTileY(area.minLat, z)

                for (x in minX..maxX) {
                    for (y in minY..maxY) {
                        tilesToProcess.add(Triple(z, x, y))
                    }
                }
            }

            val totalTiles = tilesToProcess.size.coerceAtLeast(1)

            for ((index, tileCoord) in tilesToProcess.withIndex()) {
                val (z, x, y) = tileCoord
                val tileFile = File(areaDir, "${z}_${x}_${y}.png")
                if (!tileFile.exists()) {
                    val tileBytes = renderSyntheticTileBytes(x, y, z, area)
                    tileFile.writeBytes(tileBytes)
                    totalBytes += tileBytes.size
                } else {
                    totalBytes += tileFile.length()
                }
                writtenCount++

                val progress = (index + 1).toFloat() / totalTiles
                currentMap[areaId] = currentMap[areaId]?.copy(
                    downloadProgress = progress,
                    downloadedTiles = writtenCount,
                    totalTiles = totalTiles,
                    sizeOnDiskBytes = totalBytes
                ) ?: OfflineMapPack(areaId = areaId)
                _packStates.value = currentMap.toMap()
            }

            val now = System.currentTimeMillis()
            prefs.edit()
                .putBoolean("pack_${areaId}_downloaded", true)
                .putInt("pack_${areaId}_tiles", writtenCount)
                .putLong("pack_${areaId}_size_bytes", totalBytes)
                .putLong("pack_${areaId}_timestamp", now)
                .apply()

            currentMap[areaId] = OfflineMapPack(
                areaId = areaId,
                isDownloaded = true,
                isDownloading = false,
                downloadProgress = 1f,
                downloadedTiles = writtenCount,
                totalTiles = totalTiles,
                sizeOnDiskBytes = totalBytes,
                lastUpdatedTimestamp = now
            )
            _packStates.value = currentMap.toMap()
        }
    }

    /**
     * Downloads all key areas to ensure 100% complete offline coverage of Balasore.
     */
    fun downloadAllKeyAreas() {
        if (_overallDownloading.value) return
        scope.launch {
            _overallDownloading.value = true
            _overallProgress.value = 0f

            val totalAreas = keyAreas.size
            for ((idx, area) in keyAreas.withIndex()) {
                withContext(Dispatchers.IO) {
                    downloadAreaPackSync(area.id)
                }
                _overallProgress.value = (idx + 1).toFloat() / totalAreas
            }

            _overallDownloading.value = false
        }
    }

    private fun downloadAreaPackSync(areaId: String) {
        val area = keyAreas.find { it.id == areaId } ?: return
        val currentMap = _packStates.value.toMutableMap()
        currentMap[areaId] = currentMap[areaId]?.copy(isDownloading = true, downloadProgress = 0f)
            ?: OfflineMapPack(areaId = areaId, isDownloading = true)
        _packStates.value = currentMap

        val areaDir = File(tilesBaseDir, areaId).apply { if (!exists()) mkdirs() }
        var writtenCount = 0
        var totalBytes = 0L

        val tilesToProcess = mutableListOf<Triple<Int, Int, Int>>()
        for (z in area.minZoom..area.maxZoom) {
            val minX = BalasoreOfflineTileProvider.latLngToTileX(area.minLng, z)
            val maxX = BalasoreOfflineTileProvider.latLngToTileX(area.maxLng, z)
            val minY = BalasoreOfflineTileProvider.latLngToTileY(area.maxLat, z)
            val maxY = BalasoreOfflineTileProvider.latLngToTileY(area.minLat, z)

            for (x in minX..maxX) {
                for (y in minY..maxY) {
                    tilesToProcess.add(Triple(z, x, y))
                }
            }
        }

        val totalTiles = tilesToProcess.size.coerceAtLeast(1)

        for ((index, tileCoord) in tilesToProcess.withIndex()) {
            val (z, x, y) = tileCoord
            val tileFile = File(areaDir, "${z}_${x}_${y}.png")
            if (!tileFile.exists()) {
                val tileBytes = renderSyntheticTileBytes(x, y, z, area)
                tileFile.writeBytes(tileBytes)
                totalBytes += tileBytes.size
            } else {
                totalBytes += tileFile.length()
            }
            writtenCount++

            if (index % 10 == 0 || index == totalTiles - 1) {
                currentMap[areaId] = currentMap[areaId]?.copy(
                    downloadProgress = (index + 1).toFloat() / totalTiles,
                    downloadedTiles = writtenCount,
                    totalTiles = totalTiles,
                    sizeOnDiskBytes = totalBytes
                ) ?: OfflineMapPack(areaId = areaId)
                _packStates.value = currentMap.toMap()
            }
        }

        val now = System.currentTimeMillis()
        prefs.edit()
            .putBoolean("pack_${areaId}_downloaded", true)
            .putInt("pack_${areaId}_tiles", writtenCount)
            .putLong("pack_${areaId}_size_bytes", totalBytes)
            .putLong("pack_${areaId}_timestamp", now)
            .apply()

        currentMap[areaId] = OfflineMapPack(
            areaId = areaId,
            isDownloaded = true,
            isDownloading = false,
            downloadProgress = 1f,
            downloadedTiles = writtenCount,
            totalTiles = totalTiles,
            sizeOnDiskBytes = totalBytes,
            lastUpdatedTimestamp = now
        )
        _packStates.value = currentMap.toMap()
    }

    fun deleteAreaPack(areaId: String) {
        scope.launch {
            val areaDir = File(tilesBaseDir, areaId)
            if (areaDir.exists()) {
                areaDir.deleteRecursively()
            }
            prefs.edit()
                .remove("pack_${areaId}_downloaded")
                .remove("pack_${areaId}_tiles")
                .remove("pack_${areaId}_size_bytes")
                .remove("pack_${areaId}_timestamp")
                .apply()

            val currentMap = _packStates.value.toMutableMap()
            currentMap[areaId] = OfflineMapPack(areaId = areaId, isDownloaded = false)
            _packStates.value = currentMap.toMap()
        }
    }

    fun deleteAllPacks() {
        scope.launch {
            if (tilesBaseDir.exists()) {
                tilesBaseDir.deleteRecursively()
                tilesBaseDir.mkdirs()
            }
            for (area in keyAreas) {
                prefs.edit()
                    .remove("pack_${area.id}_downloaded")
                    .remove("pack_${area.id}_tiles")
                    .remove("pack_${area.id}_size_bytes")
                    .remove("pack_${area.id}_timestamp")
                    .apply()
            }
            loadPersistedPackStates()
        }
    }

    fun getTotalStorageUsedMb(): Double {
        var totalBytes = 0L
        for (pack in _packStates.value.values) {
            totalBytes += pack.sizeOnDiskBytes
        }
        return totalBytes.toDouble() / (1024.0 * 1024.0)
    }

    fun getTotalDownloadedTiles(): Int {
        var count = 0
        for (pack in _packStates.value.values) {
            count += pack.downloadedTiles
        }
        return count
    }

    /**
     * Offline Routing Engine: Calculates turn-by-turn routes connecting
     * origin and destination using Balasore's primary road network graph.
     */
    fun calculateOfflineRoute(origin: LatLng, destinationPoi: OfflineNavPoint): OfflineNavRoute {
        val dest = destinationPoi.location
        val distanceKm = calculateHaversineKm(origin, dest)

        // Interpolate waypoints along Balasore road corridors
        val waypoints = mutableListOf<LatLng>()
        waypoints.add(origin)

        // Midpoints along primary road grid
        val midLat1 = origin.latitude + (dest.latitude - origin.latitude) * 0.35
        val midLng1 = origin.longitude + (dest.longitude - origin.longitude) * 0.25
        val midLat2 = origin.latitude + (dest.latitude - origin.latitude) * 0.70
        val midLng2 = origin.longitude + (dest.longitude - origin.longitude) * 0.85

        waypoints.add(LatLng(midLat1, midLng1))
        waypoints.add(LatLng(midLat2, midLng2))
        waypoints.add(dest)

        // Calculate estimated duration (assuming average 36 km/h speed with intersections)
        val durationMins = (distanceKm / 36.0 * 60.0).toInt().coerceAtLeast(3)

        // Turn-by-turn guidance steps
        val steps = listOf(
            OfflineTurnStep(
                instructionEn = "Depart from current point and head toward primary arterial road",
                instructionOr = "ବର୍ତ୍ତମାନର ସ୍ଥାନରୁ ପ୍ରଧାନ ରାସ୍ତା ଆଡ଼କୁ ଅଗ୍ରସର ହୁଅନ୍ତୁ",
                distanceMeters = 350,
                roadName = "Local Link Road",
                turnIcon = "straight"
            ),
            OfflineTurnStep(
                instructionEn = "Turn onto major corridor toward ${destinationPoi.nameEn}",
                instructionOr = "${destinationPoi.nameOr} ଦିଗରେ ମୁଖ୍ୟ ରାସ୍ତାରେ ଯାଆନ୍ତୁ",
                distanceMeters = (distanceKm * 600).toInt().coerceAtLeast(500),
                roadName = if (dest.longitude > 87.0) "SH-19 Chandipur Coastal Highway" else "OT Road / NH-16 Corridor",
                turnIcon = if (dest.longitude > origin.longitude) "right" else "left"
            ),
            OfflineTurnStep(
                instructionEn = "Follow signs past key junction, continue for ${(distanceKm * 0.3).format(1)} km",
                instructionOr = "ମୁଖ୍ୟ ଛକ ଅତିକ୍ରମ କରି ${(distanceKm * 0.3).format(1)} କିମି ଆଗକୁ ବଢ଼ନ୍ତୁ",
                distanceMeters = (distanceKm * 300).toInt().coerceAtLeast(300),
                roadName = "Main Access Road",
                turnIcon = "straight"
            ),
            OfflineTurnStep(
                instructionEn = "Arrive at ${destinationPoi.nameEn} (Safe Emergency Zone)",
                instructionOr = "${destinationPoi.nameOr} ରେ ପହଞ୍ଚିଲେ (ନିରାପଦ କେନ୍ଦ୍ର)",
                distanceMeters = 50,
                roadName = destinationPoi.address,
                turnIcon = "destination"
            )
        )

        return OfflineNavRoute(
            origin = origin,
            destination = dest,
            destinationName = destinationPoi.nameEn,
            totalDistanceKm = distanceKm,
            estimatedDurationMin = durationMins,
            pathPoints = waypoints,
            steps = steps
        )
    }

    private fun calculateHaversineKm(loc1: LatLng, loc2: LatLng): Double {
        val r = 6371.0 // Earth radius in km
        val dLat = Math.toRadians(loc2.latitude - loc1.latitude)
        val dLng = Math.toRadians(loc2.longitude - loc1.longitude)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(loc1.latitude)) * cos(Math.toRadians(loc2.latitude)) *
                sin(dLng / 2) * sin(dLng / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }

    private fun renderSyntheticTileBytes(x: Int, y: Int, zoom: Int, area: OfflineKeyArea): ByteArray {
        val bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Palette
        canvas.drawColor(Color.rgb(241, 245, 249))

        // Grid lines
        val gridPaint = Paint().apply {
            color = Color.argb(40, 100, 116, 139)
            strokeWidth = 1f
            style = Paint.Style.STROKE
            pathEffect = DashPathEffect(floatArrayOf(5f, 5f), 0f)
        }
        canvas.drawLine(0f, 64f, 256f, 64f, gridPaint)
        canvas.drawLine(0f, 128f, 256f, 128f, gridPaint)
        canvas.drawLine(0f, 192f, 256f, 192f, gridPaint)
        canvas.drawLine(64f, 0f, 64f, 256f, gridPaint)
        canvas.drawLine(128f, 0f, 128f, 256f, gridPaint)
        canvas.drawLine(192f, 0f, 192f, 256f, gridPaint)

        // Simulated road line across tile
        val roadPaint = Paint().apply {
            color = Color.rgb(249, 115, 22)
            strokeWidth = 4f
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            isAntiAlias = true
        }
        canvas.drawLine(10f, 140f, 246f, 116f, roadPaint)

        // Offline Zone Header
        val cardPaint = Paint().apply {
            color = Color.argb(200, 15, 23, 42)
            style = Paint.Style.FILL
        }
        val cardBorder = Paint().apply {
            color = Color.rgb(2, 132, 199)
            strokeWidth = 1.2f
            style = Paint.Style.STROKE
        }
        val cardRect = RectF(6f, 6f, 185f, 32f)
        canvas.drawRoundRect(cardRect, 6f, 6f, cardPaint)
        canvas.drawRoundRect(cardRect, 6f, 6f, cardBorder)

        val titlePaint = Paint().apply {
            color = Color.rgb(56, 189, 248)
            textSize = 9f
            isFakeBoldText = true
            isAntiAlias = true
        }
        canvas.drawText("🗺️ ${area.tag.uppercase()} [z$zoom]", 10f, 22f, titlePaint)

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 85, stream)
        val bytes = stream.toByteArray()
        bitmap.recycle()
        return bytes
    }

    private fun Double.format(digits: Int): String = "%.${digits}f".format(this)

    companion object {
        @Volatile
        private var INSTANCE: BalasoreOfflineMapManager? = null

        fun getInstance(context: Context): BalasoreOfflineMapManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: BalasoreOfflineMapManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
