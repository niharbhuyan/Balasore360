package com.example.data.maps

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.RectF
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.sinh
import kotlin.math.tan

/**
 * Category of Landmark for offline tile pre-fetching.
 */
enum class LandmarkType {
    CYCLONE_SHELTER,
    TOURIST_SPOT,
    EMERGENCY_HUB,
    HERITAGE_TEMPLE,
    COASTAL_BEACH
}

/**
 * Definition of a key Balasore landmark targeted for map tile pre-fetching.
 */
data class CacheTargetLandmark(
    val id: String,
    val nameEn: String,
    val nameOr: String,
    val type: LandmarkType,
    val center: LatLng,
    val radiusKm: Double = 1.5,
    val minZoom: Int = 12,
    val maxZoom: Int = 16,
    val description: String,
    val emergencyPhone: String? = null,
    val capacity: Int? = null,
    val elevationMeters: Double? = null,
    val priority: Int = 1
)

/**
 * Real-time caching status for a landmark.
 */
data class LandmarkCacheStatus(
    val landmarkId: String,
    val isCached: Boolean = false,
    val isPrefetching: Boolean = false,
    val cachedTilesCount: Int = 0,
    val totalTilesTarget: Int = 0,
    val sizeBytes: Long = 0L,
    val progressPercent: Float = 0f,
    val lastCachedTimestamp: Long = 0L,
    val isOnlineSource: Boolean = false
) {
    val sizeFormatted: String
        get() {
            if (sizeBytes <= 0) return "0 KB"
            val kb = sizeBytes / 1024.0
            return if (kb < 1024) {
                "%.1f KB".format(kb)
            } else {
                "%.2f MB".format(kb / 1024.0)
            }
        }
}

/**
 * Summary metrics of the tile cache across all landmarks.
 */
data class MapCacheSummary(
    val totalCachedTiles: Int,
    val totalSizeBytes: Long,
    val totalSizeFormatted: String,
    val cachedLandmarksCount: Int,
    val totalLandmarksCount: Int,
    val sheltersCachedCount: Int,
    val touristSpotsCachedCount: Int,
    val isCurrentlyCaching: Boolean,
    val overallProgress: Float,
    val currentTaskName: String?
)

/**
 * Service to pre-fetch and cache map tiles for Balasore's key landmarks
 * (cyclone shelters, tourist spots, emergency hubs) so the Google Maps
 * component can display them without an active internet connection.
 *
 * Supports both online pre-fetching from map tile CDNs and synthetic
 * high-contrast disaster navigation tiles when offline.
 */
class MapCacheManager private constructor(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("map_cache_manager_prefs", Context.MODE_PRIVATE)

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var prefetchJob: Job? = null

    // Base directory for map tiles (shared with BalasoreOfflineTileProvider)
    val tilesBaseDir: File by lazy {
        File(context.filesDir, "offline_map_tiles").apply {
            if (!exists()) mkdirs()
        }
    }

    val landmarksDir: File by lazy {
        File(tilesBaseDir, "landmarks").apply {
            if (!exists()) mkdirs()
        }
    }

    // HTTP client configured with reasonable timeouts and user-agent
    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(8, TimeUnit.SECONDS)
            .callTimeout(10, TimeUnit.SECONDS)
            .followRedirects(true)
            .build()
    }

    // 1. All Predefined Key Landmarks in Balasore
    val allLandmarks: List<CacheTargetLandmark> = listOf(
        // === Cyclone Multipurpose Shelters ===
        CacheTargetLandmark(
            id = "cs_1",
            nameEn = "Chandipur Coastal Multipurpose Shelter",
            nameOr = "ଚାନ୍ଦିପୁର ବହୁମୁଖୀ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳ",
            type = LandmarkType.CYCLONE_SHELTER,
            center = LatLng(21.4682, 87.0163),
            radiusKm = 2.0,
            description = "Near Marine Police Station, Chandipur coast. Primary safe haven during Bay of Bengal storms.",
            emergencyPhone = "06782262234",
            capacity = 1800,
            elevationMeters = 9.2,
            priority = 1
        ),
        CacheTargetLandmark(
            id = "cs_2",
            nameEn = "Bhograi Coastal Disaster Shelter",
            nameOr = "ଭୋଗରାଇ ଉପକୂଳ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳ",
            type = LandmarkType.CYCLONE_SHELTER,
            center = LatLng(21.5892, 87.4582),
            radiusKm = 2.2,
            description = "Talasari Beach Embankment, Bhograi. Protects northern coastal fishing hamlets.",
            emergencyPhone = "06781238210",
            capacity = 2400,
            elevationMeters = 9.5,
            priority = 1
        ),
        CacheTargetLandmark(
            id = "cs_3",
            nameEn = "Remuna High School Disaster Shelter",
            nameOr = "ରେମୁଣା ଉଚ୍ଚ ବିଦ୍ୟାଳୟ ଆଶ୍ରୟ କେନ୍ଦ୍ର",
            type = LandmarkType.CYCLONE_SHELTER,
            center = LatLng(21.5284, 86.8647),
            radiusKm = 1.8,
            description = "Near Khirachora Gopinath Temple, Remuna bypass.",
            emergencyPhone = "06782252115",
            capacity = 1400,
            elevationMeters = 8.0,
            priority = 1
        ),
        CacheTargetLandmark(
            id = "cs_4",
            nameEn = "Kasafal Fishermen Disaster Safe Haven",
            nameOr = "କସାଫଳ ମତ୍ସ୍ୟଜୀବୀ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳ",
            type = LandmarkType.CYCLONE_SHELTER,
            center = LatLng(21.5200, 87.1100),
            radiusKm = 2.0,
            description = "Kasafal Sea Mouth, Balasore. Elevated shelter with solar battery backup.",
            emergencyPhone = "06782260100",
            capacity = 1600,
            elevationMeters = 8.8,
            priority = 1
        ),
        CacheTargetLandmark(
            id = "cs_5",
            nameEn = "Bahanaga Relief Multipurpose Center",
            nameOr = "ବାହାନଗା ବିପର୍ଯ୍ୟୟ ରିଲିଫ କେନ୍ଦ୍ର",
            type = LandmarkType.CYCLONE_SHELTER,
            center = LatLng(21.3150, 86.8050),
            radiusKm = 1.8,
            description = "Bahanaga Bazar, NH-16 highway relief and trauma corridor.",
            emergencyPhone = "06782275420",
            capacity = 1500,
            elevationMeters = 7.5,
            priority = 1
        ),
        CacheTargetLandmark(
            id = "cs_6",
            nameEn = "Baliapal Subarnarekha Basin Shelter",
            nameOr = "ବାଲିଆପାଳ ସୁବର୍ଣ୍ଣରେଖା ଅବବାହିକା ଆଶ୍ରୟସ୍ଥଳ",
            type = LandmarkType.CYCLONE_SHELTER,
            center = LatLng(21.6420, 87.2890),
            radiusKm = 2.2,
            description = "Chaumukh River Embankment, Baliapal. Riverine flood and cyclone dual defense.",
            emergencyPhone = "06781254300",
            capacity = 2100,
            elevationMeters = 9.0,
            priority = 1
        ),
        CacheTargetLandmark(
            id = "cs_7",
            nameEn = "Soro High School Elevated Cyclone Shelter",
            nameOr = "ସୋରୋ ଉଚ୍ଚ ବିଦ୍ୟାଳୟ ବାତ୍ୟା ଆଶ୍ରୟ କେନ୍ଦ୍ର",
            type = LandmarkType.CYCLONE_SHELTER,
            center = LatLng(21.2850, 86.6920),
            radiusKm = 1.8,
            description = "Soro Town, Balasore. Southern block storm shelter.",
            emergencyPhone = "06782281200",
            capacity = 1300,
            elevationMeters = 7.8,
            priority = 1
        ),
        CacheTargetLandmark(
            id = "cs_8",
            nameEn = "Nilagiri Hill-Slope Disaster Relief Center",
            nameOr = "ନୀଳଗିରି ପାହାଡ଼ ତଳ ବିପର୍ଯ୍ୟୟ କେନ୍ଦ୍ର",
            type = LandmarkType.CYCLONE_SHELTER,
            center = LatLng(21.4550, 86.7620),
            radiusKm = 2.0,
            description = "Near Nilagiri Palace, Swarnachuda hill slopes.",
            emergencyPhone = "06782233215",
            capacity = 1200,
            elevationMeters = 18.5,
            priority = 1
        ),
        CacheTargetLandmark(
            id = "cs_9",
            nameEn = "Jaleswar Subarnarekha Multi-purpose Shelter",
            nameOr = "ଜଳେଶ୍ୱର ସୁବର୍ଣ୍ଣରେଖା ବହୁମୁଖୀ ଆଶ୍ରୟସ୍ଥଳ",
            type = LandmarkType.CYCLONE_SHELTER,
            center = LatLng(21.8020, 87.2150),
            radiusKm = 2.0,
            description = "Station Road, Jaleswar. Flood and storm surge refuge on northern border.",
            emergencyPhone = "06781222400",
            capacity = 1700,
            elevationMeters = 8.2,
            priority = 1
        ),
        CacheTargetLandmark(
            id = "cs_10",
            nameEn = "Simulia Canal Embankment Relief Shelter",
            nameOr = "ସିମୁଳିଆ କେନାଲ ବନ୍ଧ ରିଲିଫ ଆଶ୍ରୟସ୍ଥଳ",
            type = LandmarkType.CYCLONE_SHELTER,
            center = LatLng(21.1550, 86.5820),
            radiusKm = 1.8,
            description = "Simulia Block Headquarters canal road.",
            emergencyPhone = "06782294100",
            capacity = 1100,
            elevationMeters = 7.2,
            priority = 1
        ),
        CacheTargetLandmark(
            id = "cs_11",
            nameEn = "Basta Subarnarekha Basin Flood Relief Center",
            nameOr = "ବସ୍ତା ସୁବର୍ଣ୍ଣରେଖା ବନ୍ୟା ଓ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳ",
            type = LandmarkType.CYCLONE_SHELTER,
            center = LatLng(21.6850, 87.0550),
            radiusKm = 2.0,
            description = "Rajghat Embankment, Basta.",
            emergencyPhone = "06781251200",
            capacity = 1650,
            elevationMeters = 8.4,
            priority = 1
        ),
        CacheTargetLandmark(
            id = "cs_12",
            nameEn = "Balaramgadi Port Estuary Evacuation Safe Haven",
            nameOr = "ବଳରାମଗଡ଼ି ମୁହାଣ ଉଦ୍ଧାର ଓ ବାତ୍ୟା କେନ୍ଦ୍ର",
            type = LandmarkType.CYCLONE_SHELTER,
            center = LatLng(21.4880, 87.0480),
            radiusKm = 2.0,
            description = "Near Trawler Fish Landing Jetty, Budhabalanga river mouth.",
            emergencyPhone = "06782272210",
            capacity = 1750,
            elevationMeters = 8.6,
            priority = 1
        ),
        CacheTargetLandmark(
            id = "cs_13",
            nameEn = "Singla Coastal Multipurpose Evacuation Center",
            nameOr = "ସିଙ୍ଗଲା ଉପକୂଳ ବହୁମୁଖୀ ଆଶ୍ରୟସ୍ଥଳ",
            type = LandmarkType.CYCLONE_SHELTER,
            center = LatLng(21.6500, 87.2780),
            radiusKm = 2.0,
            description = "Singla Gram Panchayat, Baliapal block.",
            emergencyPhone = "06781258100",
            capacity = 1550,
            elevationMeters = 8.9,
            priority = 1
        ),
        CacheTargetLandmark(
            id = "cs_14",
            nameEn = "Khantapada NH-16 Highway Disaster Shelter",
            nameOr = "ଖନ୍ତାପଡ଼ା ରାଜପଥ ବିପର୍ଯ୍ୟୟ ଆଶ୍ରୟସ୍ଥଳ",
            type = LandmarkType.CYCLONE_SHELTER,
            center = LatLng(21.4120, 86.8450),
            radiusKm = 1.8,
            description = "Near Khantapada Bypass, NH-16.",
            emergencyPhone = "06782277100",
            capacity = 1400,
            elevationMeters = 7.9,
            priority = 1
        ),

        // === Tourist & Cultural Landmarks ===
        CacheTargetLandmark(
            id = "spot_chandipur",
            nameEn = "Chandipur Vanishing Sea Beach",
            nameOr = "ଚାନ୍ଦିପୁର ବେଳାଭୂମି",
            type = LandmarkType.COASTAL_BEACH,
            center = LatLng(21.4705, 87.0135),
            radiusKm = 2.5,
            description = "World-famous vanishing sea that recedes up to 5 km during low tide.",
            emergencyPhone = "06782272056",
            priority = 1
        ),
        CacheTargetLandmark(
            id = "spot_panchalingeswar",
            nameEn = "Panchalingeswar Perennial Stream Shrine",
            nameOr = "ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ପୀଠ",
            type = LandmarkType.HERITAGE_TEMPLE,
            center = LatLng(21.4285, 86.7125),
            radiusKm = 2.2,
            description = "Five Shiva Lingas permanently submerged beneath cool perennial mountain spring.",
            emergencyPhone = "06782233215",
            priority = 1
        ),
        CacheTargetLandmark(
            id = "spot_remuna",
            nameEn = "Khirachora Gopinath Temple Remuna",
            nameOr = "କ୍ଷୀରଚୋରା ଗୋପୀନାଥ ମନ୍ଦିର",
            type = LandmarkType.HERITAGE_TEMPLE,
            center = LatLng(21.5285, 86.8715),
            radiusKm = 1.8,
            description = "Historic 10th-century shrine famous for sacred Amruta Keli condensed milk bhog.",
            emergencyPhone = "06782225010",
            priority = 2
        ),
        CacheTargetLandmark(
            id = "spot_kuldiha",
            nameEn = "Kuldiha Wildlife Sanctuary & Elephant Reserve",
            nameOr = "କୁଲଡ଼ିହା ବନ୍ୟପ୍ରାଣୀ ଅଭୟାରଣ୍ୟ",
            type = LandmarkType.TOURIST_SPOT,
            center = LatLng(21.3980, 86.7450),
            radiusKm = 2.8,
            description = "Pristine Sal forests home to wild Asian elephants, leopards, and giant squirrels.",
            emergencyPhone = "06782233215",
            priority = 2
        ),
        CacheTargetLandmark(
            id = "spot_talasari",
            nameEn = "Talasari Beach & Red Ghost Crab Dunes",
            nameOr = "ତାଳସାରୀ ବେଳାଭୂମି",
            type = LandmarkType.COASTAL_BEACH,
            center = LatLng(21.6060, 87.4520),
            radiusKm = 2.5,
            description = "Casuarina-lined serene beach with colonies of vibrant red ghost crabs.",
            emergencyPhone = "06781238210",
            priority = 1
        ),
        CacheTargetLandmark(
            id = "spot_bichitrapur",
            nameEn = "Bichitrapur Mangrove Nature Safari",
            nameOr = "ବିଚିତ୍ରପୁର ହେନ୍ତାଳବଣ ସଫାରୀ",
            type = LandmarkType.TOURIST_SPOT,
            center = LatLng(21.6150, 87.4720),
            radiusKm = 2.4,
            description = "Tidal mangrove wetland ecosystem with guided boat safaris on Subarnarekha mouth.",
            emergencyPhone = "06781238210",
            priority = 2
        ),
        CacheTargetLandmark(
            id = "spot_emami_jagannath",
            nameEn = "Emami Jagannath Temple Januganj",
            nameOr = "ଏମାମି ଜଗନ୍ନାଥ ମନ୍ଦିର",
            type = LandmarkType.HERITAGE_TEMPLE,
            center = LatLng(21.5050, 86.8980),
            radiusKm = 1.6,
            description = "Magnificent modern Kalinga-style stone temple with hand-carved friezes.",
            emergencyPhone = "06782262024",
            priority = 2
        ),
        CacheTargetLandmark(
            id = "spot_raibania",
            nameEn = "Historic Raibania Medieval Fort Ruins",
            nameOr = "ପ୍ରାଚୀନ ରାଇବଣିଆ ଦୁର୍ଗ",
            type = LandmarkType.TOURIST_SPOT,
            center = LatLng(21.9120, 86.9450),
            radiusKm = 2.2,
            description = "Eastern India's largest medieval defensive fortress built by King Langula Narasingha Deva.",
            emergencyPhone = "06781222400",
            priority = 2
        ),
        CacheTargetLandmark(
            id = "spot_balaramgadi",
            nameEn = "Balaramgadi Fishing Port & Estuary",
            nameOr = "ବଳରାମଗଡ଼ି ମତ୍ସ୍ୟ ବନ୍ଦର ଓ ମୁହାଣ",
            type = LandmarkType.COASTAL_BEACH,
            center = LatLng(21.4880, 87.0480),
            radiusKm = 2.0,
            description = "Budhabalanga estuary and deep-sea trawler fleet auction port.",
            emergencyPhone = "06782272210",
            priority = 2
        ),
        CacheTargetLandmark(
            id = "spot_bhujanakhia",
            nameEn = "Hazrat Bhujanakhia Pir Dargah Asthana",
            nameOr = "ଭୁଜାଖିଆ ପୀର ଦରଗାହ",
            type = LandmarkType.HERITAGE_TEMPLE,
            center = LatLng(21.4910, 86.9280),
            radiusKm = 1.4,
            description = "Centuries-old syncretic Sufi shrine revered by people of all faiths in Balasore town.",
            emergencyPhone = "112",
            priority = 2
        ),
        CacheTargetLandmark(
            id = "spot_chandaneswar",
            nameEn = "Baba Chandaneswar Shiva Temple",
            nameOr = "ଚନ୍ଦନେଶ୍ୱର ଶିବ ମନ୍ଦିର",
            type = LandmarkType.HERITAGE_TEMPLE,
            center = LatLng(21.6210, 87.4760),
            radiusKm = 1.8,
            description = "Celebrated pilgrimage center famous for the annual Chadak Mela.",
            emergencyPhone = "06781238210",
            priority = 2
        ),
        CacheTargetLandmark(
            id = "spot_kasafal_beach",
            nameEn = "Kasafal Beach & Sea Mouth",
            nameOr = "କସାଫଳ ବେଳାଭୂମି",
            type = LandmarkType.COASTAL_BEACH,
            center = LatLng(21.5340, 87.1210),
            radiusKm = 2.2,
            description = "Untouched serene beach known for coastal fishing, dunes, and migratory birds.",
            emergencyPhone = "06782260100",
            priority = 2
        ),

        // === Essential Emergency & Transit Hubs ===
        CacheTargetLandmark(
            id = "hub_dhh",
            nameEn = "District Headquarter Hospital (DHH) Balasore",
            nameOr = "ଜିଲ୍ଲା ମୁଖ୍ୟ ଚିକିତ୍ସାଳୟ (DHH)",
            type = LandmarkType.EMERGENCY_HUB,
            center = LatLng(21.4925, 86.9320),
            radiusKm = 1.5,
            description = "Central district government trauma center, 24x7 blood bank and ambulance desk.",
            emergencyPhone = "06782262024",
            priority = 1
        ),
        CacheTargetLandmark(
            id = "hub_fmmch",
            nameEn = "Fakir Mohan Medical College & Hospital (FM MCH)",
            nameOr = "ଫକୀର ମୋହନ ମେଡିକାଲ କଲେଜ ଓ ହସ୍ପିଟାଲ",
            type = LandmarkType.EMERGENCY_HUB,
            center = LatLng(21.5160, 86.8840),
            radiusKm = 1.6,
            description = "Tertiary super-specialty healthcare and ICU emergency desk.",
            emergencyPhone = "06782224400",
            priority = 1
        ),
        CacheTargetLandmark(
            id = "hub_railway",
            nameEn = "Balasore Railway Junction (SER Mainline)",
            nameOr = "ବାଲେଶ୍ୱର ରେଳ ଷ୍ଟେସନ ଜଙ୍କସନ",
            type = LandmarkType.EMERGENCY_HUB,
            center = LatLng(21.4930, 86.9380),
            radiusKm = 1.5,
            description = "Howrah-Chennai railway mainline node with passenger shelters and transit security.",
            emergencyPhone = "139",
            priority = 1
        ),
        CacheTargetLandmark(
            id = "hub_bus",
            nameEn = "Sahadevkhunta Central Bus Terminal",
            nameOr = "ସହଦେବଖୁଣ୍ଟା କେନ୍ଦ୍ରୀୟ ବସ୍ ଟର୍ମିନାଲ",
            type = LandmarkType.EMERGENCY_HUB,
            center = LatLng(21.4880, 86.9240),
            radiusKm = 1.5,
            description = "Inter-district and coastal route bus departure hub.",
            emergencyPhone = "06782262145",
            priority = 1
        ),
        CacheTargetLandmark(
            id = "hub_fire",
            nameEn = "Balasore Central Fire & Disaster Rescue Station",
            nameOr = "ବାଲେଶ୍ୱର ମୁଖ୍ୟ ଅଗ୍ନିଶମ ଓ ବିପର୍ଯ୍ୟୟ ଉଦ୍ଧାର କେନ୍ଦ୍ର",
            type = LandmarkType.EMERGENCY_HUB,
            center = LatLng(21.4900, 86.9310),
            radiusKm = 1.5,
            description = "ODRAF and Fire Rescue Headquarters.",
            emergencyPhone = "101",
            priority = 1
        )
    )

    // 2. Reactive State Flows
    private val _cacheStatusFlow = MutableStateFlow<Map<String, LandmarkCacheStatus>>(emptyMap())
    val cacheStatusFlow: StateFlow<Map<String, LandmarkCacheStatus>> = _cacheStatusFlow.asStateFlow()

    private val _isPrefetching = MutableStateFlow(false)
    val isPrefetching: StateFlow<Boolean> = _isPrefetching.asStateFlow()

    private val _currentTaskName = MutableStateFlow<String?>(null)
    val currentTaskName: StateFlow<String?> = _currentTaskName.asStateFlow()

    private val _overallProgress = MutableStateFlow(0f)
    val overallProgress: StateFlow<Float> = _overallProgress.asStateFlow()

    init {
        loadPersistedStatus()
    }

    private fun loadPersistedStatus() {
        val map = mutableMapOf<String, LandmarkCacheStatus>()
        for (landmark in allLandmarks) {
            val isCached = prefs.getBoolean("lm_${landmark.id}_cached", false)
            val tilesCount = prefs.getInt("lm_${landmark.id}_tiles", 0)
            val sizeBytes = prefs.getLong("lm_${landmark.id}_size_bytes", 0L)
            val timestamp = prefs.getLong("lm_${landmark.id}_timestamp", 0L)
            val isOnline = prefs.getBoolean("lm_${landmark.id}_online", false)

            map[landmark.id] = LandmarkCacheStatus(
                landmarkId = landmark.id,
                isCached = isCached,
                isPrefetching = false,
                cachedTilesCount = tilesCount,
                totalTilesTarget = estimateTilesForLandmark(landmark),
                sizeBytes = sizeBytes,
                progressPercent = if (isCached) 1f else 0f,
                lastCachedTimestamp = timestamp,
                isOnlineSource = isOnline
            )
        }
        _cacheStatusFlow.value = map
    }

    fun isOnline(): Boolean {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            val net = cm?.activeNetwork ?: return false
            val caps = cm.getNetworkCapabilities(net) ?: return false
            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Calculates the estimated tile count for a landmark given its radius and zoom range.
     */
    fun estimateTilesForLandmark(landmark: CacheTargetLandmark): Int {
        var count = 0
        val deltaLat = landmark.radiusKm / 111.0
        val deltaLng = landmark.radiusKm / (111.0 * cos(Math.toRadians(landmark.center.latitude)).coerceAtLeast(0.01))

        val minLat = landmark.center.latitude - deltaLat
        val maxLat = landmark.center.latitude + deltaLat
        val minLng = landmark.center.longitude - deltaLng
        val maxLng = landmark.center.longitude + deltaLng

        for (z in landmark.minZoom..landmark.maxZoom) {
            val minX = BalasoreOfflineTileProvider.latLngToTileX(minLng, z)
            val maxX = BalasoreOfflineTileProvider.latLngToTileX(maxLng, z)
            val minY = BalasoreOfflineTileProvider.latLngToTileY(maxLat, z)
            val maxY = BalasoreOfflineTileProvider.latLngToTileY(minLat, z)

            val xSpan = (maxX - minX + 1).coerceAtLeast(1)
            val ySpan = (maxY - minY + 1).coerceAtLeast(1)
            count += (xSpan * ySpan)
        }
        return count.coerceAtLeast(1)
    }

    /**
     * Pre-fetches map tiles for an individual landmark.
     */
    fun prefetchLandmark(landmarkId: String, onProgress: (Float) -> Unit = {}) {
        val target = allLandmarks.find { it.id == landmarkId } ?: return
        scope.launch {
            prefetchLandmarkInternal(target, onProgress)
        }
    }

    /**
     * Pre-fetches tiles for all 14 cyclone shelters.
     */
    fun prefetchAllShelters(onProgress: (Float) -> Unit = {}) {
        val shelters = allLandmarks.filter { it.type == LandmarkType.CYCLONE_SHELTER }
        prefetchGroup("Cyclone Shelters", shelters, onProgress)
    }

    /**
     * Pre-fetches tiles for all tourist and coastal landmarks.
     */
    fun prefetchAllTouristSpots(onProgress: (Float) -> Unit = {}) {
        val spots = allLandmarks.filter {
            it.type == LandmarkType.TOURIST_SPOT ||
            it.type == LandmarkType.COASTAL_BEACH ||
            it.type == LandmarkType.HERITAGE_TEMPLE
        }
        prefetchGroup("Tourist Hotspots", spots, onProgress)
    }

    /**
     * Pre-fetches tiles for all landmarks in Balasore.
     */
    fun prefetchAllLandmarks(onProgress: (overall: Float, currentItem: String) -> Unit = { _, _ -> }) {
        if (_isPrefetching.value) return
        prefetchJob = scope.launch {
            _isPrefetching.value = true
            _overallProgress.value = 0f
            val total = allLandmarks.size

            try {
                for ((index, landmark) in allLandmarks.withIndex()) {
                    _currentTaskName.value = "${landmark.nameEn} (${index + 1}/$total)"
                    onProgress((index.toFloat() / total), landmark.nameEn)
                    prefetchLandmarkInternal(landmark) { itemProgress ->
                        val combined = (index + itemProgress) / total
                        _overallProgress.value = combined
                        onProgress(combined, landmark.nameEn)
                    }
                }
                _overallProgress.value = 1f
            } catch (e: CancellationException) {
                // Job was cancelled
            } finally {
                _isPrefetching.value = false
                _currentTaskName.value = null
            }
        }
    }

    private fun prefetchGroup(
        groupName: String,
        items: List<CacheTargetLandmark>,
        onProgress: (Float) -> Unit
    ) {
        if (_isPrefetching.value) return
        prefetchJob = scope.launch {
            _isPrefetching.value = true
            _overallProgress.value = 0f
            val total = items.size.coerceAtLeast(1)

            try {
                for ((index, item) in items.withIndex()) {
                    _currentTaskName.value = "$groupName: ${item.nameEn} (${index + 1}/$total)"
                    prefetchLandmarkInternal(item) { itemProgress ->
                        val combined = (index + itemProgress) / total
                        _overallProgress.value = combined
                        onProgress(combined)
                    }
                }
                _overallProgress.value = 1f
                onProgress(1f)
            } catch (e: CancellationException) {
                // User cancelled
            } finally {
                _isPrefetching.value = false
                _currentTaskName.value = null
            }
        }
    }

    /**
     * Core worker that downloads or synthesizes tiles for a single landmark.
     */
    private suspend fun prefetchLandmarkInternal(
        landmark: CacheTargetLandmark,
        onProgress: (Float) -> Unit = {}
    ) = withContext(Dispatchers.IO) {
        val currentMap = _cacheStatusFlow.value.toMutableMap()
        currentMap[landmark.id] = currentMap[landmark.id]?.copy(
            isPrefetching = true,
            progressPercent = 0f
        ) ?: LandmarkCacheStatus(landmarkId = landmark.id, isPrefetching = true)
        _cacheStatusFlow.value = currentMap

        val deltaLat = landmark.radiusKm / 111.0
        val deltaLng = landmark.radiusKm / (111.0 * cos(Math.toRadians(landmark.center.latitude)).coerceAtLeast(0.01))

        val minLat = landmark.center.latitude - deltaLat
        val maxLat = landmark.center.latitude + deltaLat
        val minLng = landmark.center.longitude - deltaLng
        val maxLng = landmark.center.longitude + deltaLng

        val tilesToProcess = mutableListOf<Triple<Int, Int, Int>>() // zoom, x, y
        for (z in landmark.minZoom..landmark.maxZoom) {
            val minX = BalasoreOfflineTileProvider.latLngToTileX(minLng, z)
            val maxX = BalasoreOfflineTileProvider.latLngToTileX(maxLng, z)
            val minY = BalasoreOfflineTileProvider.latLngToTileY(maxLat, z)
            val maxY = BalasoreOfflineTileProvider.latLngToTileY(minLat, z)

            for (x in minX..maxX) {
                for (y in minY..maxY) {
                    tilesToProcess.add(Triple(z, x, y))
                }
            }
        }

        val totalTiles = tilesToProcess.size.coerceAtLeast(1)
        var writtenCount = 0
        var totalBytes = 0L
        var fetchedOnlineCount = 0
        val onlineAvailable = isOnline()

        val landmarkSubDir = File(landmarksDir, landmark.id).apply {
            if (!exists()) mkdirs()
        }

        for ((index, tileCoord) in tilesToProcess.withIndex()) {
            val (z, x, y) = tileCoord
            val baseFileName = "${z}_${x}_${y}.png"
            val globalTileFile = File(tilesBaseDir, baseFileName)
            val landmarkTileFile = File(landmarkSubDir, baseFileName)

            if (globalTileFile.exists() && globalTileFile.length() > 0) {
                // Already cached in global pool
                totalBytes += globalTileFile.length()
                writtenCount++
            } else {
                // Pre-fetch: Try online fetch first if device is online
                var tileBytes: ByteArray? = null
                if (onlineAvailable) {
                    tileBytes = downloadTileOverHttp(x, y, z)
                    if (tileBytes != null && tileBytes.isNotEmpty()) {
                        fetchedOnlineCount++
                    }
                }

                // If offline or online fetch failed, generate high-fidelity synthetic navigation tile
                if (tileBytes == null || tileBytes.isEmpty()) {
                    tileBytes = renderLandmarkSyntheticTile(x, y, z, landmark)
                }

                try {
                    globalTileFile.writeBytes(tileBytes)
                    landmarkTileFile.writeBytes(tileBytes)
                    totalBytes += tileBytes.size
                    writtenCount++
                } catch (e: Exception) {
                    // Ignore write failure
                }
            }

            val progress = (index + 1).toFloat() / totalTiles
            onProgress(progress)

            if (index % 5 == 0 || index == totalTiles - 1) {
                currentMap[landmark.id] = LandmarkCacheStatus(
                    landmarkId = landmark.id,
                    isCached = (index == totalTiles - 1),
                    isPrefetching = (index < totalTiles - 1),
                    cachedTilesCount = writtenCount,
                    totalTilesTarget = totalTiles,
                    sizeBytes = totalBytes,
                    progressPercent = progress,
                    lastCachedTimestamp = System.currentTimeMillis(),
                    isOnlineSource = (fetchedOnlineCount > 0)
                )
                _cacheStatusFlow.value = currentMap.toMap()
            }
        }

        val now = System.currentTimeMillis()
        val isOnlineSource = fetchedOnlineCount > (totalTiles / 4)

        // Persist status
        prefs.edit()
            .putBoolean("lm_${landmark.id}_cached", true)
            .putInt("lm_${landmark.id}_tiles", writtenCount)
            .putLong("lm_${landmark.id}_size_bytes", totalBytes)
            .putLong("lm_${landmark.id}_timestamp", now)
            .putBoolean("lm_${landmark.id}_online", isOnlineSource)
            .apply()

        currentMap[landmark.id] = LandmarkCacheStatus(
            landmarkId = landmark.id,
            isCached = true,
            isPrefetching = false,
            cachedTilesCount = writtenCount,
            totalTilesTarget = totalTiles,
            sizeBytes = totalBytes,
            progressPercent = 1f,
            lastCachedTimestamp = now,
            isOnlineSource = isOnlineSource
        )
        _cacheStatusFlow.value = currentMap.toMap()
    }

    /**
     * Attempts to fetch a map tile from OpenStreetMap or CartoCDN.
     */
    private fun downloadTileOverHttp(x: Int, y: Int, zoom: Int): ByteArray? {
        val servers = listOf(
            "https://tile.openstreetmap.org/$zoom/$x/$y.png",
            "https://cartodb-basemaps-a.global.ssl.fastly.net/light_all/$zoom/$x/$y.png"
        )

        for (url in servers) {
            try {
                val request = Request.Builder()
                    .url(url)
                    .header("User-Agent", "Balasore360-OfflineMaps/1.0 (Android; Odisha Disaster Response)")
                    .build()

                val response = httpClient.newCall(request).execute()
                if (response.isSuccessful) {
                    val bytes = response.body?.bytes()
                    response.close()
                    if (bytes != null && bytes.size > 200) {
                        return bytes
                    }
                } else {
                    response.close()
                }
            } catch (_: Exception) {
                // Try fallback server or fallback to synthesis
            }
        }
        return null
    }

    /**
     * Synthesizes a high-contrast emergency navigation tile specifically tailored
     * for the landmark's proximity zone.
     */
    private fun renderLandmarkSyntheticTile(
        x: Int,
        y: Int,
        zoom: Int,
        landmark: CacheTargetLandmark
    ): ByteArray {
        val n = 1 shl zoom
        val westLng = x.toDouble() / n * 360.0 - 180.0
        val eastLng = (x + 1).toDouble() / n * 360.0 - 180.0
        val northLat = Math.toDegrees(atan(sinh(Math.PI * (1.0 - 2.0 * y / n))))
        val southLat = Math.toDegrees(atan(sinh(Math.PI * (1.0 - 2.0 * (y + 1) / n))))

        val bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val isShelter = landmark.type == LandmarkType.CYCLONE_SHELTER
        val isCoast = landmark.center.longitude > 87.0

        // Background terrain
        val bgPaint = Paint().apply {
            color = if (isShelter) Color.rgb(241, 245, 249) else Color.rgb(248, 250, 252)
        }
        canvas.drawRect(0f, 0f, 256f, 256f, bgPaint)

        // Water boundary for coastal Balasore zones
        if (isCoast && eastLng > 87.02) {
            val seaPaint = Paint().apply {
                color = Color.rgb(186, 230, 253)
                style = Paint.Style.FILL
            }
            val seaStartX = (((87.02 - westLng) / (eastLng - westLng)) * 256f).toFloat().coerceIn(0f, 256f)
            canvas.drawRect(seaStartX, 0f, 256f, 256f, seaPaint)

            val coastPaint = Paint().apply {
                color = Color.rgb(2, 132, 199)
                strokeWidth = 2f
                style = Paint.Style.STROKE
            }
            canvas.drawLine(seaStartX, 0f, seaStartX, 256f, coastPaint)
        }

        // Grid lines
        val gridPaint = Paint().apply {
            color = Color.argb(35, 100, 116, 139)
            strokeWidth = 1f
            style = Paint.Style.STROKE
            pathEffect = DashPathEffect(floatArrayOf(4f, 4f), 0f)
        }
        canvas.drawLine(0f, 64f, 256f, 64f, gridPaint)
        canvas.drawLine(0f, 128f, 256f, 128f, gridPaint)
        canvas.drawLine(0f, 192f, 256f, 192f, gridPaint)
        canvas.drawLine(64f, 0f, 64f, 256f, gridPaint)
        canvas.drawLine(128f, 0f, 128f, 256f, gridPaint)
        canvas.drawLine(192f, 0f, 192f, 256f, gridPaint)

        // Road corridor
        val roadPaint = Paint().apply {
            color = if (isShelter) Color.rgb(234, 88, 12) else Color.rgb(37, 99, 235)
            strokeWidth = 4.5f
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            isAntiAlias = true
        }
        canvas.drawLine(0f, 132f, 256f, 124f, roadPaint)

        // Approach link road
        val linkRoadPaint = Paint().apply {
            color = Color.rgb(202, 138, 4)
            strokeWidth = 3f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        canvas.drawLine(128f, 128f, 128f, 256f, linkRoadPaint)

        // Check if landmark center falls inside this tile
        val containsLandmark = landmark.center.latitude in southLat..northLat &&
                landmark.center.longitude in westLng..eastLng

        if (containsLandmark) {
            val lmX = (((landmark.center.longitude - westLng) / (eastLng - westLng)) * 256f).toFloat()
            val lmY = (((northLat - landmark.center.latitude) / (northLat - southLat)) * 256f).toFloat()

            // Safe radius aura
            val auraPaint = Paint().apply {
                color = if (isShelter) Color.argb(45, 234, 88, 12) else Color.argb(40, 2, 132, 199)
                style = Paint.Style.FILL
            }
            canvas.drawCircle(lmX, lmY, 48f, auraPaint)

            val auraStroke = Paint().apply {
                color = if (isShelter) Color.rgb(234, 88, 12) else Color.rgb(2, 132, 199)
                strokeWidth = 1.5f
                style = Paint.Style.STROKE
                pathEffect = DashPathEffect(floatArrayOf(6f, 4f), 0f)
            }
            canvas.drawCircle(lmX, lmY, 48f, auraStroke)

            // Pin center marker
            val pinPaint = Paint().apply {
                color = if (isShelter) Color.rgb(220, 38, 38) else Color.rgb(16, 185, 129)
                style = Paint.Style.FILL
                isAntiAlias = true
            }
            canvas.drawCircle(lmX, lmY, 8f, pinPaint)

            val pinInner = Paint().apply {
                color = Color.WHITE
                style = Paint.Style.FILL
                isAntiAlias = true
            }
            canvas.drawCircle(lmX, lmY, 3.5f, pinInner)
        }

        // Header Banner badge
        val headerPaint = Paint().apply {
            color = if (isShelter) Color.argb(210, 24, 24, 27) else Color.argb(205, 15, 23, 42)
            style = Paint.Style.FILL
        }
        val headerBorder = Paint().apply {
            color = if (isShelter) Color.rgb(234, 88, 12) else Color.rgb(56, 189, 248)
            strokeWidth = 1f
            style = Paint.Style.STROKE
        }
        val headerRect = RectF(6f, 6f, 190f, 30f)
        canvas.drawRoundRect(headerRect, 5f, 5f, headerPaint)
        canvas.drawRoundRect(headerRect, 5f, 5f, headerBorder)

        val headerText = Paint().apply {
            color = Color.WHITE
            textSize = 9.2f
            isFakeBoldText = true
            isAntiAlias = true
        }
        val iconPrefix = if (isShelter) "🛡️ SHELTER" else "📍 LANDMARK"
        canvas.drawText("$iconPrefix [z$zoom]", 10f, 22f, headerText)

        // Convert to PNG
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 85, stream)
        val bytes = stream.toByteArray()
        bitmap.recycle()
        return bytes
    }

    /**
     * Cancels any running pre-fetch job.
     */
    fun cancelPrefetch() {
        prefetchJob?.cancel()
        _isPrefetching.value = false
        _currentTaskName.value = null
    }

    /**
     * Removes cached tiles for a specific landmark.
     */
    fun removeLandmarkCache(landmarkId: String) {
        scope.launch {
            val subDir = File(landmarksDir, landmarkId)
            if (subDir.exists()) {
                subDir.deleteRecursively()
            }
            prefs.edit()
                .remove("lm_${landmarkId}_cached")
                .remove("lm_${landmarkId}_tiles")
                .remove("lm_${landmarkId}_size_bytes")
                .remove("lm_${landmarkId}_timestamp")
                .remove("lm_${landmarkId}_online")
                .apply()

            val currentMap = _cacheStatusFlow.value.toMutableMap()
            currentMap[landmarkId] = LandmarkCacheStatus(
                landmarkId = landmarkId,
                isCached = false,
                isPrefetching = false,
                cachedTilesCount = 0,
                totalTilesTarget = estimateTilesForLandmark(allLandmarks.firstOrNull { it.id == landmarkId } ?: return@launch)
            )
            _cacheStatusFlow.value = currentMap.toMap()
        }
    }

    /**
     * Clears all pre-fetched landmark tiles.
     */
    fun clearAllLandmarkCache() {
        cancelPrefetch()
        scope.launch {
            if (landmarksDir.exists()) {
                landmarksDir.deleteRecursively()
                landmarksDir.mkdirs()
            }
            for (lm in allLandmarks) {
                prefs.edit()
                    .remove("lm_${lm.id}_cached")
                    .remove("lm_${lm.id}_tiles")
                    .remove("lm_${lm.id}_size_bytes")
                    .remove("lm_${lm.id}_timestamp")
                    .remove("lm_${lm.id}_online")
                    .apply()
            }
            loadPersistedStatus()
        }
    }

    /**
     * Checks if a landmark is cached on disk.
     */
    fun isLandmarkCached(landmarkId: String): Boolean {
        return _cacheStatusFlow.value[landmarkId]?.isCached == true ||
                prefs.getBoolean("lm_${landmarkId}_cached", false)
    }

    /**
     * Returns tile file if present in cache.
     */
    fun getCachedTile(x: Int, y: Int, zoom: Int): File? {
        val fileName = "${zoom}_${x}_${y}.png"
        val direct = File(tilesBaseDir, fileName)
        if (direct.exists() && direct.length() > 0) return direct

        val subdirs = landmarksDir.listFiles { f -> f.isDirectory } ?: return null
        for (dir in subdirs) {
            val fileInSub = File(dir, fileName)
            if (fileInSub.exists() && fileInSub.length() > 0) return fileInSub
        }
        return null
    }

    /**
     * Computes real-time summary statistics for cache health.
     */
    fun getSummaryStatistics(): MapCacheSummary {
        val statuses = _cacheStatusFlow.value.values
        var totalTiles = 0
        var totalBytes = 0L
        var cachedCount = 0
        var sheltersCached = 0
        var touristCached = 0

        for (status in statuses) {
            if (status.isCached) {
                cachedCount++
                totalTiles += status.cachedTilesCount
                totalBytes += status.sizeBytes

                val target = allLandmarks.find { it.id == status.landmarkId }
                if (target?.type == LandmarkType.CYCLONE_SHELTER) {
                    sheltersCached++
                } else {
                    touristCached++
                }
            }
        }

        val totalSizeMb = totalBytes.toDouble() / (1024.0 * 1024.0)
        val formatted = if (totalSizeMb < 1.0) {
            "%.1f KB".format(totalBytes / 1024.0)
        } else {
            "%.2f MB".format(totalSizeMb)
        }

        return MapCacheSummary(
            totalCachedTiles = totalTiles,
            totalSizeBytes = totalBytes,
            totalSizeFormatted = formatted,
            cachedLandmarksCount = cachedCount,
            totalLandmarksCount = allLandmarks.size,
            sheltersCachedCount = sheltersCached,
            touristSpotsCachedCount = touristCached,
            isCurrentlyCaching = _isPrefetching.value,
            overallProgress = _overallProgress.value,
            currentTaskName = _currentTaskName.value
        )
    }

    companion object {
        @Volatile
        private var INSTANCE: MapCacheManager? = null

        fun getInstance(context: Context): MapCacheManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MapCacheManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
