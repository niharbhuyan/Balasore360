package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.Hotspot
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark
import kotlinx.coroutines.delay
import kotlin.math.*

/**
 * Data model for a Tourist Location with Google Maps coordinates and distance markers.
 */
data class TouristLocationItem(
    val id: String,
    val nameEn: String,
    val nameOr: String,
    val categoryEn: String,
    val categoryOr: String,
    val latitude: Double,
    val longitude: Double,
    val baseDistanceKm: Double,
    val rating: Double,
    val reviewCount: Int,
    val timings: String,
    val shortDescEn: String,
    val shortDescOr: String,
    val highlightEmoji: String,
    val gradientColors: List<Color>,
    val openStatus: String = "OPEN_NOW",
    val entryFee: String = "Free Entry",
    // Transit & Navigation Information
    val nearestTransitStop: String = "Balasore City Bus Stand",
    val transitRoutes: List<String> = listOf("Mo Bus Route 101", "Sahadevkhunta Shared Auto / Toto"),
    val estimatedTransitFare: String = "Mo Bus: ₹20 • Toto/Auto: ₹50",
    val transitTravelTimeMins: Int = 30,
    val roadAccessType: String = "Paved Highway (All-Weather)",
    val parkingAvailable: Boolean = true,
    val categoryGroup: String = "HOTSPOT", // "HOTSPOT", "TEMPLE", "LANDMARK", "NATURE"
    val transitFrequency: String = "Every 15-20 mins",
    val transitTips: String = "Shared Toto available from Sahadevkhunta Bus Stand"
)

/**
 * Master dataset of Balasore Tourism Hotspots with exact Google Maps coordinates.
 * Reference center: Balasore City Center / Railway Station (21.4934° N, 86.9135° E).
 */
val BALASORE_TOURIST_LOCATIONS = listOf(
    TouristLocationItem(
        id = "chandipur_beach",
        nameEn = "Chandipur Beach (Vanishing Sea)",
        nameOr = "ଚାନ୍ଦିପୁର ବେଳାଭୂମି (ଅନ୍ତର୍ଦ୍ଧାନ ସମୁଦ୍ର)",
        categoryEn = "Beach & Coast",
        categoryOr = "ବେଳାଭୂମି ଓ ଉପକୂଳ",
        latitude = 21.4695,
        longitude = 87.0185,
        baseDistanceKm = 15.8,
        rating = 4.8,
        reviewCount = 3420,
        timings = "Open 24 Hours (Sunrise & Low Tide Best)",
        shortDescEn = "World-famous vanishing sea where tides recede up to 5 km twice daily into the Bay of Bengal.",
        shortDescOr = "ଦୈନିକ ଦୁଇଥର ସମୁଦ୍ର ୫ କିଲୋମିଟର ପଛକୁ ହଟିଯାଏ, ସମୁଦ୍ର ଶଯ୍ୟାରେ ଚାଲିବାର ଅଭୂତପୂର୍ବ ଅନୁଭୂତି।",
        highlightEmoji = "🏖️",
        gradientColors = listOf(Color(0xFF0369A1), Color(0xFF0284C7), Color(0xFF38BDF8)),
        categoryGroup = "BEACH",
        nearestTransitStop = "Chandipur Marine Police & OTDC Panthanivas",
        transitRoutes = listOf("Mo Bus Route 101 (Balasore Rly Stn - Chandipur)", "Sahadevkhunta Shared Auto / Cruiser"),
        estimatedTransitFare = "Mo Bus: ₹20 • Shared Auto: ₹40",
        transitTravelTimeMins = 30,
        transitFrequency = "Mo Bus every 20-30 mins from Station",
        transitTips = "Direct Mo Bus Route 101 terminates right in front of Chandipur Beach."
    ),
    TouristLocationItem(
        id = "khirachora_gopinath",
        nameEn = "Khirachora Gopinatha Temple",
        nameOr = "କ୍ଷୀରଚୋରା ଗୋପୀନାଥ ମନ୍ଦିର (ରେମୁଣା)",
        categoryEn = "Heritage & Spiritual",
        categoryOr = "ଐତିହ୍ୟ ଓ ଶ୍ରୀକ୍ଷେତ୍ର",
        latitude = 21.5285,
        longitude = 86.8715,
        baseDistanceKm = 9.2,
        rating = 4.9,
        reviewCount = 2890,
        timings = "05:30 AM - 12:30 PM, 04:00 PM - 08:30 PM",
        shortDescEn = "Ancient 12th-century shrine famous for the legendary Amruta Keli condensed milk bhog.",
        shortDescOr = "ମାଧବେନ୍ଦ୍ର ପୁରୀଙ୍କ ପାଇଁ କ୍ଷୀର ଚୋରି କରିଥିବା ପ୍ରଭୁ ଗୋପୀନାଥଙ୍କ ପବିତ୍ର ଦେବସ୍ଥଳୀ ଓ ଅମୃତ କେଳି ଭୋଗ।",
        highlightEmoji = "🛕",
        gradientColors = listOf(Color(0xFF78350F), Color(0xFFB45309), Color(0xFFD97706)),
        categoryGroup = "TEMPLE",
        nearestTransitStop = "Remuna Golei Auto Stand / Temple Gate",
        transitRoutes = listOf("Mo Bus Route 103 (Station - Remuna)", "Shared Toto from Remuna Golei"),
        estimatedTransitFare = "Mo Bus: ₹15 • Toto: ₹20",
        transitTravelTimeMins = 20,
        transitFrequency = "Toto shuttles available every 5 mins from Remuna Golei",
        transitTips = "Evening Amruta Keli Bhog distribution starts at 6:30 PM."
    ),
    TouristLocationItem(
        id = "emami_jagannath",
        nameEn = "Emami Jagannath Temple Januganj",
        nameOr = "ଇମାମି ଜଗନ୍ନାଥ ମନ୍ଦିର",
        categoryEn = "Spiritual Architecture",
        categoryOr = "ମନ୍ଦିର ସ୍ଥାପତ୍ୟ କଳା",
        latitude = 21.5050,
        longitude = 86.8980,
        baseDistanceKm = 4.8,
        rating = 4.8,
        reviewCount = 1950,
        timings = "06:00 AM - 12:30 PM, 04:30 PM - 09:00 PM",
        shortDescEn = "Exquisite modern Kalinga-style stone temple modeled on Puri Jagannath Dham with beautiful gardens.",
        shortDescOr = "କଳିଙ୍ଗ ସ୍ଥାପତ୍ୟ ଶୈଳୀରେ ନିର୍ମିତ ବାଲେଶ୍ୱରର ନୂତନ ଶ୍ରୀକ୍ଷେତ୍ର, ସୁନ୍ଦର ଉଦ୍ୟାନ ଓ ମନୋରମ ପରିବେଶ।",
        highlightEmoji = "🚩",
        gradientColors = listOf(Color(0xFF9A3412), Color(0xFFC2410C), Color(0xFFEA580C)),
        categoryGroup = "TEMPLE",
        nearestTransitStop = "Januganj Chhak NH-16",
        transitRoutes = listOf("Direct City Toto from Balasore Station", "Mo Bus Route 102"),
        estimatedTransitFare = "City Toto: ₹25 • Mo Bus: ₹10",
        transitTravelTimeMins = 12,
        transitFrequency = "Totos available constantly from Station North Gate",
        transitTips = "Spacious parking available for private cars and tourist buses."
    ),
    TouristLocationItem(
        id = "panchalingeswar",
        nameEn = "Panchalingeswar Hill Temple",
        nameOr = "ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ପାହାଡ଼ ପୀଠ",
        categoryEn = "Nature & Pilgrimage",
        categoryOr = "ପ୍ରକୃତି ଓ ଶୈବପୀଠ",
        latitude = 21.4320,
        longitude = 86.7180,
        baseDistanceKm = 30.5,
        rating = 4.7,
        reviewCount = 2180,
        timings = "05:30 AM - 06:30 PM",
        shortDescEn = "Five natural rock Shiva Lingas constantly submerged under a pristine mountain perennial spring water stream.",
        shortDescOr = "ଦେବଗିରି ପାହାଡ଼ ଶୀର୍ଷରେ ପ୍ରାକୃତିକ ଝରଣାର ଶୀତଳ ଜଳରେ ସର୍ବଦା ନିମଗ୍ନ ୫ଟି ପବିତ୍ର ଶିବଲିଙ୍ଗ।",
        highlightEmoji = "⛰️",
        gradientColors = listOf(Color(0xFF065F46), Color(0xFF059669), Color(0xFF10B981)),
        categoryGroup = "TEMPLE",
        nearestTransitStop = "Panchalingeswar Foot Hill Bus Bay & OTDC Panthasala",
        transitRoutes = listOf("Balasore - Nilagiri Bus + Auto to Foothill", "OTDC Special Weekend Excursion"),
        estimatedTransitFare = "Bus to Nilagiri: ₹35 • Shared Auto to Shrine: ₹25",
        transitTravelTimeMins = 50,
        transitFrequency = "Buses leave Sahadevkhunta Bus Stand every 30 mins",
        transitTips = "Requires climbing 263 stone steps through shady hill canopy."
    ),
    TouristLocationItem(
        id = "kuldiha_sanctuary",
        nameEn = "Kuldiha Wildlife Sanctuary & Eco-Camp",
        nameOr = "କୁଲଡିହା ବନ୍ୟପ୍ରାଣୀ ଅଭୟାରଣ୍ୟ",
        categoryEn = "Eco-Tourism & Safari",
        categoryOr = "ବନ୍ୟପ୍ରାଣୀ ଓ ଇକୋ-କ୍ୟାମ୍ପ",
        latitude = 21.3980,
        longitude = 86.7450,
        baseDistanceKm = 35.0,
        rating = 4.7,
        reviewCount = 1420,
        timings = "06:00 AM - 05:00 PM (Permit Required)",
        shortDescEn = "Dense sal forest habitat home to wild Asian elephants, leopards, and giant squirrels near Rissia Dam.",
        shortDescOr = "ହାତୀ ପଲଙ୍କ କରିଡର, ଗୋହିରାଭୋଳା ସଲ୍ଟ-ଲିକ୍ ଟାୱାର, ହରିଣ, ଚିତାବାଘ ଓ ମନୋରମ ରିସିଆ ଡ୍ୟାମ୍।",
        highlightEmoji = "🐘",
        gradientColors = listOf(Color(0xFF1E3A8A), Color(0xFF1D4ED8), Color(0xFF3B82F6)),
        categoryGroup = "NATURE",
        nearestTransitStop = "Nilagiri Forest Checkpost / Kuldiha Gate",
        transitRoutes = listOf("Balasore to Nilagiri Bus, then pre-booked Forest Gypsy", "Private Taxi / Self-Drive"),
        estimatedTransitFare = "Bus to Nilagiri: ₹35 • Eco-Permit/Safari: ₹250",
        transitTravelTimeMins = 60,
        transitFrequency = "Safari vehicles should be booked at Forest Range Office",
        transitTips = "Enter before 2 PM to complete safari before dusk animal movements."
    ),
    TouristLocationItem(
        id = "balaramgadi_estuary",
        nameEn = "Balaramgadi Fishing Harbor & Estuary",
        nameOr = "ବଳରାମଗଡ଼ି ମତ୍ସ୍ୟ ବନ୍ଦର ଓ ମୁହାଣ",
        categoryEn = "Maritime & Beach",
        categoryOr = "ମତ୍ସ୍ୟ ବନ୍ଦର ଓ ସଙ୍ଗମ",
        latitude = 21.4880,
        longitude = 87.0480,
        baseDistanceKm = 12.2,
        rating = 4.6,
        reviewCount = 1120,
        timings = "04:30 AM - 07:00 PM (Morning Auction Best)",
        shortDescEn = "Budhabalanga River mouth into Bay of Bengal; vibrant colorful trawler fleet and daily fresh catch auctions.",
        shortDescOr = "ବୁଢ଼ାବଳଙ୍ଗ ନଦୀ ଓ ସମୁଦ୍ରର ମିଳନସ୍ଥଳ, ଶହ ଶହ ରଙ୍ଗୀନ ଟ୍ରଲର ଓ ସକାଳର ସତେଜ ମାଛ ନିଲାମ।",
        highlightEmoji = "⛵",
        gradientColors = listOf(Color(0xFF0F766E), Color(0xFF0D9488), Color(0xFF14B8A6)),
        categoryGroup = "BEACH",
        nearestTransitStop = "Balaramgadi Trawler Jetty Chhak",
        transitRoutes = listOf("Chandipur Mo Bus Route 101 (alight at Marine Police) + Toto (2km)", "Direct Shared Auto from Station"),
        estimatedTransitFare = "Mo Bus + Toto: ₹30 • Direct Auto: ₹60",
        transitTravelTimeMins = 25,
        transitFrequency = "Shared autos run frequently between Station and Balaramgadi",
        transitTips = "Best time to visit is 5:30 AM to 8:30 AM during trawler landings."
    ),
    TouristLocationItem(
        id = "talasari_beach",
        nameEn = "Talasari & Udaipur Red Crab Beach",
        nameOr = "ତାଳସାରୀ ଓ ଉଦୟପୁର ଲାଲ୍ କଙ୍କଡ଼ା ବେଳାଭୂମି",
        categoryEn = "Beach & Eco-Tourism",
        categoryOr = "ବେଳାଭୂମି ଓ ପ୍ରକୃତି",
        latitude = 21.6060,
        longitude = 87.4520,
        baseDistanceKm = 88.0,
        rating = 4.6,
        reviewCount = 2650,
        timings = "Open 24 Hours",
        shortDescEn = "Tranquil palm-fringed shoreline with vast colonies of red ghost crabs and Subarnarekha estuary delta.",
        shortDescOr = "ତାଳ ଓ ଝାଉଁ ବଣରେ ଘେରା ନିରୋଳା ବେଳାଭୂମି, ଲାଲ୍ କଙ୍କଡ଼ାର ରାଜୁତି ଓ ଓଡ଼ିଶା-ବଙ୍ଗଳା ସୀମାନ୍ତ।",
        highlightEmoji = "🦀",
        gradientColors = listOf(Color(0xFF9F1239), Color(0xFFBE123C), Color(0xFFE11D48)),
        categoryGroup = "BEACH",
        nearestTransitStop = "Talasari OTDC Panthasala Bus Stand",
        transitRoutes = listOf("Direct OSRTC / Private Express Bus from Balasore to Chandaneswar/Talasari", "Local Train to Jaleswar + Taxi"),
        estimatedTransitFare = "Direct Express Bus: ₹95 • Toto from Chandaneswar: ₹20",
        transitTravelTimeMins = 120,
        transitFrequency = "Buses depart Sahadevkhunta Bus Stand every 45 mins",
        transitTips = "Cross over to Udaipur beach during low tide for pristine dunes."
    ),
    TouristLocationItem(
        id = "bichitrapur_mangrove",
        nameEn = "Bichitrapur Mangrove Delta Safari",
        nameOr = "ବିଚିତ୍ରପୁର ହେନ୍ତାଳବଣ ବୋଟିଂ",
        categoryEn = "Nature & Mangrove",
        categoryOr = "ହେନ୍ତାଳବଣ ନୌକା ବିହାର",
        latitude = 21.6150,
        longitude = 87.4720,
        baseDistanceKm = 92.0,
        rating = 4.7,
        reviewCount = 980,
        timings = "07:30 AM - 05:00 PM (High Tide Best)",
        shortDescEn = "Lush tidal mangrove delta on Subarnarekha River mouth; OTDC guided boat safaris through tidal channels.",
        shortDescOr = "ସୁବର୍ଣ୍ଣରେଖା ମୁହାଣର ସବୁଜ ହେନ୍ତାଳବଣ, ଜୁଆର ବେଳେ ବୋଟ୍ ସଫାରୀ ଓ ପ୍ରବାସୀ ପକ୍ଷୀଙ୍କ କାକଳି।",
        highlightEmoji = "🚤",
        gradientColors = listOf(Color(0xFF166534), Color(0xFF15803D), Color(0xFF22C55E)),
        categoryGroup = "NATURE",
        nearestTransitStop = "Bichitrapur Eco-Tourism Boat Jetty",
        transitRoutes = listOf("Bus to Chandaneswar, then hired Toto/Auto to Bichitrapur Jetty (6km)", "OTDC Coastal Tour"),
        estimatedTransitFare = "Bus to Chandaneswar: ₹95 • Toto to Jetty: ₹40 • Boat Safari: ₹150",
        transitTravelTimeMins = 135,
        transitFrequency = "Boats operate during high tide tidal windows",
        transitTips = "Check tidal timings in the app before booking your boat trip."
    ),
    TouristLocationItem(
        id = "raibania_fort",
        nameEn = "Historic Raibania Medieval Fortress",
        nameOr = "ଐତିହାସିକ ରାଇବଣିଆ ଦୁର୍ଗ",
        categoryEn = "Heritage & History",
        categoryOr = "ପ୍ରାଚୀନ ଦୁର୍ଗ ଓ ଇତିହାସ",
        latitude = 21.9120,
        longitude = 86.9450,
        baseDistanceKm = 72.0,
        rating = 4.5,
        reviewCount = 680,
        timings = "08:00 AM - 06:00 PM",
        shortDescEn = "Largest medieval defensive fortress ruins in eastern India built by King Langula Narasingha Deva.",
        shortDescOr = "ରାଜା ଲାଙ୍ଗୁଳା ନରସିଂହ ଦେବଙ୍କ ନିର୍ମିତ ପୂର୍ବ ଭାରତର ସର୍ବବୃହତ୍ ମଧ୍ୟଯୁଗୀୟ ଦୁର୍ଗର ଭଗ୍ନାବଶେଷ।",
        highlightEmoji = "🏰",
        gradientColors = listOf(Color(0xFF581C87), Color(0xFF6B21A8), Color(0xFF7E22CE)),
        categoryGroup = "LANDMARK",
        nearestTransitStop = "Raibania Gram Panchayat / Jaleswar Sub-division",
        transitRoutes = listOf("Local Train to Jaleswar Station + Shared Cruiser to Raibania", "Direct Bus Balasore - Raibania"),
        estimatedTransitFare = "Train to Jaleswar: ₹25 • Cruiser to Fort: ₹40",
        transitTravelTimeMins = 100,
        transitFrequency = "Cruisers run every 30 mins from Jaleswar Station market",
        transitTips = "Explore the ancient moat, Jayachandi shrine and cyclopean laterite stone walls."
    ),
    TouristLocationItem(
        id = "kasafal_beach",
        nameEn = "Kasafal Beach & Marine Fishermen Village",
        nameOr = "କାସାଫାଳ ବେଳାଭୂମି ଓ ମୁହାଣ",
        categoryEn = "Beach & Marine",
        categoryOr = "ସାମୁଦ୍ରିକ ମତ୍ସ୍ୟଜୀବୀ ପୀଠ",
        latitude = 21.5250,
        longitude = 87.1150,
        baseDistanceKm = 14.5,
        rating = 4.5,
        reviewCount = 820,
        timings = "Open 24 Hours",
        shortDescEn = "Unspoiled beach with active marine fishing fleet, dry fish yards, and dramatic high-tide ocean surf.",
        shortDescOr = "ପ୍ରାକୃତିକ ନୀରବ ବେଳାଭୂମି, ସାମୁଦ୍ରିକ ମାଛ ଧରା ନୌକା ଓ ସୂର୍ଯ୍ୟାସ୍ତର ଅପରୂପ ଶୋଭା।",
        highlightEmoji = "🐟",
        gradientColors = listOf(Color(0xFF155E75), Color(0xFF0E7490), Color(0xFF06B6D4)),
        categoryGroup = "BEACH",
        nearestTransitStop = "Kasafal Sea Mouth Fish Landing Center",
        transitRoutes = listOf("Shared Auto / Toto from ITI Chhak via Kuruda", "Direct Rural Bus from Sahadevkhunta"),
        estimatedTransitFare = "Shared Auto: ₹35 • Toto: ₹45",
        transitTravelTimeMins = 35,
        transitFrequency = "Autos run every 20 mins from ITI Square",
        transitTips = "Scenic coastal rural drive through prawn farms and coconut plantations."
    ),
    TouristLocationItem(
        id = "bhusandeswar_temple",
        nameEn = "Bhusandeswar Monolithic Shiva Lingam",
        nameOr = "ଭୂଷଣ୍ଡେଶ୍ୱର ବିଶାଳ ଶିବଲିଙ୍ଗ",
        categoryEn = "Heritage & Spiritual",
        categoryOr = "ଶୈବ ତୀର୍ଥସ୍ଥଳୀ",
        latitude = 21.8210,
        longitude = 87.2340,
        baseDistanceKm = 54.0,
        rating = 4.7,
        reviewCount = 1640,
        timings = "06:00 AM - 08:00 PM",
        shortDescEn = "Home to Asia's second-largest monolithic black stone Shiva Lingam on the banks of Subarnarekha River.",
        shortDescOr = "ଏସିଆ ମହାଦେଶର ଦ୍ୱିତୀୟ ବୃହତ୍ତମ ଏକକ କୃଷ୍ଣପ୍ରସ୍ତର ଶିବଲିଙ୍ଗ, ସୁବର୍ଣ୍ଣରେଖା ନଦୀ ତଟସ୍ଥ ପବିତ୍ର ପୀଠ।",
        highlightEmoji = "🕉️",
        gradientColors = listOf(Color(0xFF374151), Color(0xFF4B5563), Color(0xFF6B7280)),
        categoryGroup = "TEMPLE",
        nearestTransitStop = "Bhusandeswar Temple Complex Stop",
        transitRoutes = listOf("Balasore to Jaleswar/Baliapal Bus + Local Toto", "Direct Tourist Van from Balasore"),
        estimatedTransitFare = "Bus: ₹60 • Toto: ₹20",
        transitTravelTimeMins = 80,
        transitFrequency = "Connecting Totos meet all arrival buses at Baliapal Bazar",
        transitTips = "The monolithic lingam is 12 feet high and 14 feet in circumference."
    ),
    TouristLocationItem(
        id = "baba_baneswar_temple",
        nameEn = "Baba Baneswar Temple & Living Turtle Sanctuary",
        nameOr = "ବାବା ବାଣେଶ୍ୱର ମନ୍ଦିର ଓ କଇଁଛ ପୁଷ୍କରିଣୀ",
        categoryEn = "Temples & Living Heritage",
        categoryOr = "ପ୍ରାଚୀନ ଶୈବପୀଠ ଓ କଇଁଛ ସଂରକ୍ଷଣ",
        latitude = 21.4820,
        longitude = 86.9580,
        baseDistanceKm = 6.5,
        rating = 4.8,
        reviewCount = 1430,
        timings = "05:00 AM - 01:00 PM, 04:00 PM - 09:00 PM",
        shortDescEn = "Ancient Shaivite shrine in Purunabalasore with a centuries-old pond nurturing hundreds of sacred black softshell river turtles.",
        shortDescOr = "ପୁରୁଣା ବାଲେଶ୍ୱରସ୍ଥିତ ଐତିହାସିକ ଶୈବପୀଠ, ପବିତ୍ର ପୋଖରୀରେ ଶହ ଶହ ବର୍ଷ ପୁରୁଣା ପବିତ୍ର କଇଁଛଙ୍କ ବାସସ୍ଥଳ।",
        highlightEmoji = "🐢",
        gradientColors = listOf(Color(0xFFD97706), Color(0xFFB45309), Color(0xFF92400E)),
        categoryGroup = "TEMPLE",
        nearestTransitStop = "Purunabalasore Chhak / Baneswar Temple Gate",
        transitRoutes = listOf("City Toto from Balasore Railway Station", "Shared Auto from Motiganj Bazar"),
        estimatedTransitFare = "Toto: ₹25 • Shared Auto: ₹15",
        transitTravelTimeMins = 15,
        transitFrequency = "Totos available every 5 mins from Station South Gate",
        transitTips = "Devotees feed puffed rice (mudhi) and coconut pieces to the gentle giant river turtles."
    ),
    TouristLocationItem(
        id = "chandaneswar_temple",
        nameEn = "Chandaneswar Shiva Shrine & Uda Parba",
        nameOr = "ଚନ୍ଦନେଶ୍ୱର ଶୈବପୀଠ (ଉଡ଼ା ପର୍ବ)",
        categoryEn = "Historic Shrines",
        categoryOr = "ପ୍ରସିଦ୍ଧ ଶୈବପୀଠ ଓ ମହାମେଳା",
        latitude = 21.6180,
        longitude = 87.4820,
        baseDistanceKm = 86.0,
        rating = 4.8,
        reviewCount = 3120,
        timings = "05:00 AM - 09:30 PM",
        shortDescEn = "Major regional pilgrimage center near the Bengal border, famed for the vibrant 13-day annual Nilaparba & Chadak/Uda festival.",
        shortDescOr = "ଓଡ଼ିଶା-ବଙ୍ଗଳା ସୀମାନ୍ତର ପ୍ରସିଦ୍ଧ ଶିବ ଧାମ, ଲକ୍ଷାଧିକ ଭକ୍ତଙ୍କ ଆଗମନ ଓ ପ୍ରସିଦ୍ଧ ଉଡ଼ା ପର୍ବର କେନ୍ଦ୍ର।",
        highlightEmoji = "🔱",
        gradientColors = listOf(Color(0xFFEA580C), Color(0xFFC2410C), Color(0xFF9A3412)),
        categoryGroup = "TEMPLE",
        nearestTransitStop = "Chandaneswar Temple Bus Terminus",
        transitRoutes = listOf("Direct Express Bus from Sahadevkhunta Bus Stand to Chandaneswar", "Local Train to Digha/Jaleswar + Auto"),
        estimatedTransitFare = "Express Bus: ₹90 • Shared Cruiser: ₹100",
        transitTravelTimeMins = 120,
        transitFrequency = "Direct buses depart every 40 mins from Sahadevkhunta",
        transitTips = "Huge pilgrim congregation during Pana Sankranti (Maha Vishuva Sankranti)."
    ),
    TouristLocationItem(
        id = "langaleswar_temple",
        nameEn = "Langaleswar Temple & Sacred Parikhi Confluence",
        nameOr = "ଲାଙ୍ଗଳେଶ୍ୱର ମନ୍ଦିର (ପାରିଖୀ)",
        categoryEn = "Vedic Shrines",
        categoryOr = "ପ୍ରାଚୀନ ଶୈବ ତୀର୍ଥ",
        latitude = 21.6210,
        longitude = 87.0980,
        baseDistanceKm = 42.0,
        rating = 4.6,
        reviewCount = 890,
        timings = "05:30 AM - 08:00 PM",
        shortDescEn = "Sacred river confluence temple where Lord Shiva according to legend ploughed the earth; serene rural riverside.",
        shortDescOr = "ପାରିଖୀ ନଦୀ କୂଳସ୍ଥିତ ଲାଙ୍ଗଳେଶ୍ୱର ପୀଠ, ଯେଉଁଠାରେ ମହାଦେବ ହଳ ଲଙ୍ଗଳ ଧରି ଭୂମି କର୍ଷଣ କରିଥିବାର ବିଶ୍ୱାସ ରହିଛି।",
        highlightEmoji = "🌾",
        gradientColors = listOf(Color(0xFF047857), Color(0xFF065F46), Color(0xFF064E3B)),
        categoryGroup = "TEMPLE",
        nearestTransitStop = "Langaleswar Chhak / Singla Road",
        transitRoutes = listOf("Balasore to Singla/Basta Bus", "Shared Auto from Basta Junction"),
        estimatedTransitFare = "Bus: ₹45 • Auto: ₹20",
        transitTravelTimeMins = 70,
        transitFrequency = "Buses leave Sahadevkhunta every hour",
        transitTips = "Peaceful riverside ambiance with historical Jagamohana stone carvings."
    ),
    TouristLocationItem(
        id = "bhujakhia_pir",
        nameEn = "Bhujakhia Pir Shrine of Communal Harmony",
        nameOr = "ଭୁଜାଖିଆ ପୀର ଦରଗାହ (ସୁନହଟ)",
        categoryEn = "Heritage & Spiritual",
        categoryOr = "ସଦଭାବନାର ପ୍ରତୀକ ଓ ପବିତ୍ର ଦରଗାହ",
        latitude = 21.4980,
        longitude = 86.9280,
        baseDistanceKm = 2.5,
        rating = 4.7,
        reviewCount = 1750,
        timings = "06:00 AM - 09:30 PM",
        shortDescEn = "Historic Sufi shrine in Sunhat revered by both Hindus and Muslims, where sweet puffed rice (Mudhi) is offered in sacred harmony.",
        shortDescOr = "ବାଲେଶ୍ୱର ସୁନହଟସ୍ଥିତ ଐତିହାସିକ ସୂଫୀ ପୀଠ, ହିନ୍ଦୁ-ମୁସଲିମ ଭ୍ରାତୃତ୍ୱର ନିଦର୍ଶନ ଓ ପବିତ୍ର ଭୁଜା (ମୁଢ଼ି) ଭୋଗ।",
        highlightEmoji = "🕊️",
        gradientColors = listOf(Color(0xFF0284C7), Color(0xFF0369A1), Color(0xFF075985)),
        categoryGroup = "TEMPLE",
        nearestTransitStop = "Sunhat Masjid Chhak",
        transitRoutes = listOf("Direct City Toto from Balasore Station", "Walking / Cycle Rickshaw from Cinema Bazar"),
        estimatedTransitFare = "Toto: ₹20 • Cycle Rickshaw: ₹20",
        transitTravelTimeMins = 8,
        transitFrequency = "Available 24x7 from Balasore Station",
        transitTips = "Annual Urs festival draws thousands celebrating composite Odia culture."
    ),
    TouristLocationItem(
        id = "fakir_mohan_memorial",
        nameEn = "Vyasa Kabi Fakir Mohan Shanti Kanan",
        nameOr = "ବ୍ୟାସକବି ଫକୀର ମୋହନ ଶାନ୍ତି କାନନ",
        categoryEn = "Historic Landmarks",
        categoryOr = "ଐତିହାସିକ ସ୍ମାରକୀ ଓ ସାହିତ୍ୟ କାନନ",
        latitude = 21.5015,
        longitude = 86.9210,
        baseDistanceKm = 3.2,
        rating = 4.8,
        reviewCount = 1280,
        timings = "07:00 AM - 07:30 PM",
        shortDescEn = "Memorial garden and tribute library dedicated to Vyasa Kabi Fakir Mohan Senapati, the father of modern Odia fiction.",
        shortDescOr = "ଆଧୁନିକ ଓଡ଼ିଆ କଥା ସାହିତ୍ୟର ଜନକ ବ୍ୟାସକବି ଫକୀର ମୋହନ ସେନାପତିଙ୍କ ସ୍ମୃତିରେ ନିର୍ମିତ ସୁରମ୍ୟ ଶାନ୍ତି କାନନ ଓ ପାଠାଗାର।",
        highlightEmoji = "📚",
        gradientColors = listOf(Color(0xFF7C3AED), Color(0xFF6D28D9), Color(0xFF5B21B6)),
        categoryGroup = "LANDMARK",
        nearestTransitStop = "Phandi Chhak / Shanti Kanan Gate",
        transitRoutes = listOf("Direct City Toto from Station", "Mo Bus Route 102 (alight at Phandi Chhak)"),
        estimatedTransitFare = "Toto: ₹20 • Mo Bus: ₹10",
        transitTravelTimeMins = 10,
        transitFrequency = "Totos operate every minute along Station Road",
        transitTips = "Features the bronze bust of Fakir Mohan, literary quotes, and landscaped reading pergolas."
    ),
    TouristLocationItem(
        id = "balasore_colonial_cemetery",
        nameEn = "Dutch & British Maritime Trade Cemetery",
        nameOr = "ଡଚ୍ ଓ ଇଂରେଜ ଐତିହାସିକ କବରସ୍ଥାନ (ବାରବାଟୀ)",
        categoryEn = "Colonial Heritage",
        categoryOr = "ପ୍ରାଚୀନ ୟୁରୋପୀୟ ସାମୁଦ୍ରିକ ଐତିହ୍ୟ",
        latitude = 21.4920,
        longitude = 86.9290,
        baseDistanceKm = 1.8,
        rating = 4.4,
        reviewCount = 540,
        timings = "08:00 AM - 06:00 PM",
        shortDescEn = "17th-century heritage cemetery preserving ornate obelisk tombs of Dutch and British maritime merchants from when Balasore was a global port.",
        shortDescOr = "ସପ୍ତଦଶ ଶତାବ୍ଦୀର ଡଚ୍ ଓ ବ୍ରିଟିଶ ବ୍ୟବସାୟୀଙ୍କ ପ୍ରାଚୀନ ସ୍ମାରକୀ ସ୍ତମ୍ଭ, ଯେତେବେଳେ ବାଲେଶ୍ୱର ପୂର୍ବ ଭାରତର ପ୍ରଧାନ ବନ୍ଦର ଥିଲା।",
        highlightEmoji = "🏛️",
        gradientColors = listOf(Color(0xFF475569), Color(0xFF334155), Color(0xFF1E293B)),
        categoryGroup = "LANDMARK",
        nearestTransitStop = "Barabati Girls High School Lane",
        transitRoutes = listOf("Direct Toto from Balasore Station", "Short Walk from Cinema Bazar"),
        estimatedTransitFare = "Toto: ₹15",
        transitTravelTimeMins = 6,
        transitFrequency = "Easily accessible within town center",
        transitTips = "Features monumental Dutch pyramidal tombs dating back to the 1680s."
    ),
    TouristLocationItem(
        id = "budhabalanga_bagha_jatin_ghat",
        nameEn = "Bagha Jatin Freedom Memorial Ghat",
        nameOr = "ବାଘା ଯତୀନ ସ୍ମୃତିପୀଠ (ଚାଷାଖଣ୍ଡ)",
        categoryEn = "Historic Landmarks",
        categoryOr = "ସ୍ୱାଧୀନତା ସଂଗ୍ରାମ ତୀର୍ଥ",
        latitude = 21.5120,
        longitude = 86.9740,
        baseDistanceKm = 8.5,
        rating = 4.8,
        reviewCount = 980,
        timings = "Open 24 Hours",
        shortDescEn = "Historic riverside monument commemorating the heroic 1915 trench battle of revolutionary leader Bagha Jatin against colonial police.",
        shortDescOr = "୧୯୧୫ ମସିହାରେ ବ୍ରିଟିଶ ପୋଲିସ ସହ ମୁହାଁମୁହିଁ ଲଢ଼େଇ କରିଥିବା ଅମର ବିପ୍ଳବୀ ବାଘା ଯତୀନଙ୍କ ଐତିହାସିକ ଯୁଦ୍ଧସ୍ଥଳୀ ଓ ସ୍ମାରକୀ।",
        highlightEmoji = "🇮🇳",
        gradientColors = listOf(Color(0xFFDC2626), Color(0xFFB91C1C), Color(0xFF991B1B)),
        categoryGroup = "LANDMARK",
        nearestTransitStop = "Chashakhand Freedom Park Stop",
        transitRoutes = listOf("Shared Auto from Sahadevkhunta via Remuna Golei", "Direct City Toto"),
        estimatedTransitFare = "Shared Auto: ₹25 • Toto: ₹40",
        transitTravelTimeMins = 20,
        transitFrequency = "Shared autos run every 15 mins",
        transitTips = "Overlooks the tranquil Budhabalanga river where the trench gun battle took place."
    ),
    TouristLocationItem(
        id = "drdo_itr_viewpoint",
        nameEn = "DRDO Missile Test Range Coastal Viewpoint",
        nameOr = "ଡିଆରଡିଓ କ୍ଷେପଣାସ୍ତ୍ର ଘାଟି ଦୃଶ୍ୟପଟ (ଚାନ୍ଦିପୁର)",
        categoryEn = "Defense Landmarks",
        categoryOr = "ପ୍ରତିରକ୍ଷା ଗୌରବ ଓ ଉପକୂଳ ଦୃଶ୍ୟପଟ",
        latitude = 21.4620,
        longitude = 87.0210,
        baseDistanceKm = 16.5,
        rating = 4.9,
        reviewCount = 2210,
        timings = "06:00 AM - 07:00 PM (Exterior Public View)",
        shortDescEn = "Viewpoint overlooking the Bay of Bengal where India's premier defense lab conducts Agni, Prithvi, and BrahMos missile trials.",
        shortDescOr = "ଭାରତର ଅଗ୍ନି, ପୃଥ୍ୱୀ ଓ ବ୍ରହ୍ମୋସ କ୍ଷେପଣାସ୍ତ୍ର ପରୀକ୍ଷଣ କେନ୍ଦ୍ରର ବାହ୍ୟ ଦୃଶ୍ୟପଟ ଓ ସାମୁଦ୍ରିକ ରାଡାର ପଏଣ୍ଟ।",
        highlightEmoji = "🚀",
        gradientColors = listOf(Color(0xFF2563EB), Color(0xFF1D4ED8), Color(0xFF1E40AF)),
        categoryGroup = "LANDMARK",
        nearestTransitStop = "Chandipur DRDO Main Gate Chhak",
        transitRoutes = listOf("Mo Bus Route 101 to Chandipur", "Direct Shared Auto from Station"),
        estimatedTransitFare = "Mo Bus: ₹20 • Shared Auto: ₹40",
        transitTravelTimeMins = 32,
        transitFrequency = "Mo Bus runs every 25 mins",
        transitTips = "Features scale model rocket displays and radar tracking installations."
    ),
    TouristLocationItem(
        id = "dagara_beach",
        nameEn = "Dagara Casuarina Beach & Silver Surf",
        nameOr = "ଡଗରା ବେଳାଭୂମି ଓ ଝାଉଁବଣ (ବାଲିଆପାଳ)",
        categoryEn = "Beach & Coastal Dunes",
        categoryOr = "ନିରୋଳା ବେଳାଭୂମି ଓ ରୂପାଲି ତରଙ୍ଗ",
        latitude = 21.5740,
        longitude = 87.3150,
        baseDistanceKm = 64.0,
        rating = 4.6,
        reviewCount = 1350,
        timings = "Open 24 Hours",
        shortDescEn = "Expansive silvery sea beach flanked by whispering casuarina woods, gentle gradient surf, and colonies of red ghost crabs.",
        shortDescOr = "ଝାଉଁ ବଣରେ ଘେରା ପ୍ରଶାନ୍ତ ବେଳାଭୂମି, ଲାଲ କଙ୍କଡ଼ାଙ୍କ ଧାବମାନ ଦୃଶ୍ୟ ଓ ଉଜ୍ଜ୍ୱଳ ସୂର୍ଯ୍ୟୋଦୟ।",
        highlightEmoji = "🌊",
        gradientColors = listOf(Color(0xFF0891B2), Color(0xFF0E7490), Color(0xFF155E75)),
        categoryGroup = "BEACH",
        nearestTransitStop = "Dagara Beach OTDC Parking / Baliapal Bus Stand",
        transitRoutes = listOf("Balasore to Baliapal Bus + Local Toto/Cruiser to Beach (8km)", "Direct Tourist Taxi"),
        estimatedTransitFare = "Bus to Baliapal: ₹65 • Toto to Dagara: ₹25",
        transitTravelTimeMins = 95,
        transitFrequency = "Buses leave Sahadevkhunta every 30-40 mins",
        transitTips = "Ideal picnic spot with wide, hard-packed sand suitable for walking and sunset strolls."
    )
)

/**
 * Calculates distance in kilometers between two GPS coordinates using the Haversine formula.
 */
fun calculateHaversineDistanceKm(
    lat1: Double,
    lon1: Double,
    lat2: Double,
    lon2: Double
): Double {
    val earthRadiusKm = 6371.0
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2).pow(2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return earthRadiusKm * c
}

/**
 * Interactive 'Tourism Hotspots' Section featuring:
 *  - Google Maps API location calculation
 *  - Card-based carousel with distance markers
 *  - Direct turn-by-turn Google Maps navigation intents
 *  - Category filter pills & Live Distance sorting
 */
@Composable
fun TourismHotspotsCarousel(
    language: AppLanguage = AppLanguage.ENGLISH,
    userLatitude: Double = 21.4934, // Default to Balasore Junction / City Center
    userLongitude: Double = 86.9135,
    onOpenHotspotMap: (TouristLocationItem) -> Unit = {},
    onAddToItinerary: (TouristLocationItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedCategoryFilter by remember { mutableStateOf("All") }
    var sortByDistance by remember { mutableStateOf(true) }

    // Origins: Station, Chandipur, Remuna, Nilagiri, Live
    var currentOriginLat by remember { mutableDoubleStateOf(userLatitude) }
    var currentOriginLon by remember { mutableDoubleStateOf(userLongitude) }
    var currentOriginId by remember { mutableStateOf("station") }

    // Auto-update countdown ticker (20 seconds cycle)
    var autoUpdateSeconds by remember { mutableIntStateOf(20) }
    var refreshPulse by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            if (autoUpdateSeconds > 1) {
                autoUpdateSeconds -= 1
            } else {
                autoUpdateSeconds = 20
                refreshPulse += 1
            }
        }
    }

    // Compute live distance from current location
    val processedLocations = remember(currentOriginLat, currentOriginLon, selectedCategoryFilter, sortByDistance, refreshPulse) {
        BALASORE_TOURIST_LOCATIONS.map { item ->
            val computedDistance = calculateHaversineDistanceKm(
                currentOriginLat,
                currentOriginLon,
                item.latitude,
                item.longitude
            )
            // Round to 1 decimal place
            val roundedDist = (computedDistance * 10).roundToInt() / 10.0
            item.copy(baseDistanceKm = roundedDist)
        }.filter { item ->
            selectedCategoryFilter == "All" ||
                    (selectedCategoryFilter == "Beach" && item.categoryEn.contains("Beach", ignoreCase = true)) ||
                    (selectedCategoryFilter == "Temple" && (item.categoryEn.contains("Temple", ignoreCase = true) || item.categoryEn.contains("Spiritual", ignoreCase = true))) ||
                    (selectedCategoryFilter == "Nature" && (item.categoryEn.contains("Nature", ignoreCase = true) || item.categoryEn.contains("Safari", ignoreCase = true))) ||
                    (selectedCategoryFilter == "Near (<20km)" && item.baseDistanceKm <= 20.0)
        }.let { list ->
            if (sortByDistance) list.sortedBy { it.baseDistanceKm } else list
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("tourism_hotspots_carousel_section")
    ) {
        // Section Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = OceanBlue.copy(alpha = 0.12f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            tint = OceanBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = if (language == AppLanguage.ODIA) "ନିକଟସ୍ଥ ପର୍ଯ୍ୟଟନ ସ୍ଥଳୀ" else "Tourism Hotspots",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A)
                        )
                    )
                    Text(
                        text = if (language == AppLanguage.ODIA) "ଗୁଗୁଲ୍ ମ୍ୟାପ୍ସ ଦୂରତା ମାର୍କର୍ (${processedLocations.size})" else "Google Maps Distance Markers (${processedLocations.size})",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Auto-Update indicator
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF10B981).copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, Color(0xFF10B981))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color(0xFF10B981), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "AUTO: ${autoUpdateSeconds}s",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF047857)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Full Map Launch button
                IconButton(
                    onClick = {
                        val firstLoc = processedLocations.firstOrNull() ?: BALASORE_TOURIST_LOCATIONS.first()
                        onOpenHotspotMap(firstLoc)
                    },
                    modifier = Modifier
                        .size(32.dp)
                        .testTag("open_hotspots_map_sheet_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Map,
                        contentDescription = "Open Full Map",
                        tint = OceanBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Origin Point Switcher
        val originTabs = listOf(
            Triple("station", "📍 Station HQ", Pair(21.4934, 86.9135)),
            Triple("chandipur", "🏖️ Chandipur", Pair(21.4695, 87.0185)),
            Triple("remuna", "🛕 Remuna", Pair(21.5285, 86.8715)),
            Triple("nilagiri", "🏞️ Nilagiri", Pair(21.4600, 86.7650))
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (language == AppLanguage.ODIA) "କେନ୍ଦ୍ର:" else "From:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64748B)
            )
            Spacer(modifier = Modifier.width(6.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(originTabs) { (id, label, coords) ->
                    val isSelected = currentOriginId == id
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) OceanBlue else Color(0xFFF1F5F9),
                        border = BorderStroke(1.dp, if (isSelected) OceanBlue else Color(0xFFE2E8F0)),
                        modifier = Modifier
                            .clickable {
                                currentOriginId = id
                                currentOriginLat = coords.first
                                currentOriginLon = coords.second
                            }
                            .testTag("origin_tab_$id")
                    ) {
                        Text(
                            text = label,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color(0xFF334155),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }

        // Filter chips row
        val filterTabs = listOf("All", "Near (<20km)", "Beach", "Temple", "Nature")
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            items(filterTabs) { tab ->
                val isSelected = selectedCategoryFilter == tab
                val label = when (tab) {
                    "All" -> if (language == AppLanguage.ODIA) "ସମସ୍ତ" else "All"
                    "Near (<20km)" -> if (language == AppLanguage.ODIA) "ନିକଟ (<୨୦ କିମି)" else "Near (<20km)"
                    "Beach" -> if (language == AppLanguage.ODIA) "ବେଳାଭୂମି" else "Beach"
                    "Temple" -> if (language == AppLanguage.ODIA) "ମନ୍ଦିର" else "Temple"
                    "Nature" -> if (language == AppLanguage.ODIA) "ପ୍ରକୃତି" else "Nature"
                    else -> tab
                }

                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCategoryFilter = tab },
                    label = {
                        Text(
                            text = label,
                            fontSize = 11.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = OceanBlue,
                        selectedLabelColor = Color.White,
                        containerColor = Color.White,
                        labelColor = Color(0xFF334155)
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = Color(0xFFCBD5E1),
                        selectedBorderColor = OceanBlue
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Card-based Carousel
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.testTag("tourism_hotspots_carousel")
        ) {
            items(processedLocations, key = { it.id }) { item ->
                HotspotCarouselCard(
                    location = item,
                    language = language,
                    context = context,
                    onOpenMap = { onOpenHotspotMap(item) },
                    onAddToItinerary = { onAddToItinerary(item) }
                )
            }
        }
    }
}

/**
 * Individual Card in the Tourism Hotspots Carousel with Google Maps distance markers.
 */
@Composable
fun HotspotCarouselCard(
    location: TouristLocationItem,
    language: AppLanguage,
    context: Context,
    onOpenMap: () -> Unit,
    onAddToItinerary: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(285.dp)
            .testTag("hotspot_carousel_card_${location.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            // Visual Header with Gradient & Distance Marker
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .background(Brush.horizontalGradient(location.gradientColors))
                    .padding(12.dp)
            ) {
                // Top Tag Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Category Badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.Black.copy(alpha = 0.35f)
                    ) {
                        Text(
                            text = if (language == AppLanguage.ODIA) location.categoryOr else location.categoryEn,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    // Prominent Google Maps Distance Marker
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, Color(0xFF0284C7)),
                        shadowElevation = 2.dp,
                        modifier = Modifier.testTag("distance_marker_${location.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.NearMe,
                                contentDescription = "Distance",
                                tint = Color(0xFF0284C7),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${location.baseDistanceKm} km",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF0F172A)
                            )
                        }
                    }
                }

                // Bottom of Header: Emoji + Driving ETA badge
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = location.highlightEmoji,
                        fontSize = 32.sp
                    )

                    // Estimated driving time
                    val drivingMin = (location.baseDistanceKm * 1.6).roundToInt().coerceAtLeast(5)
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.Black.copy(alpha = 0.40f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "~$drivingMin min",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Card Body Content
            Column(modifier = Modifier.padding(14.dp)) {
                // Title
                Text(
                    text = if (language == AppLanguage.ODIA) location.nameOr else location.nameEn,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0F172A),
                        fontSize = 15.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (language == AppLanguage.ODIA) location.nameEn else location.nameOr,
                    fontSize = 11.sp,
                    color = Color(0xFF64748B),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Ratings & Status Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${location.rating}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                        Text(
                            text = " (${location.reviewCount})",
                            fontSize = 10.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFEFF6FF)
                    ) {
                        Text(
                            text = location.entryFee,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1D4ED8),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Short Description
                Text(
                    text = if (language == AppLanguage.ODIA) location.shortDescOr else location.shortDescEn,
                    fontSize = 11.5.sp,
                    color = Color(0xFF475569),
                    lineHeight = 15.5.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Operating Hours
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = location.timings,
                        fontSize = 10.5.sp,
                        color = Color(0xFF64748B),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action Buttons: Directions (Google Maps Intent) & Explore Map
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // One-tap Google Maps Navigation Button
                    Button(
                        onClick = {
                            // Launch native Google Maps navigation intent
                            val gmmIntentUri = Uri.parse("google.navigation:q=${location.latitude},${location.longitude}&mode=d")
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                                setPackage("com.google.android.apps.maps")
                            }
                            if (mapIntent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(mapIntent)
                            } else {
                                // Fallback to universal geo URI
                                val geoUri = Uri.parse("geo:${location.latitude},${location.longitude}?q=${Uri.encode("${location.nameEn}, Balasore")}")
                                context.startActivity(Intent(Intent.ACTION_VIEW, geoUri))
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .testTag("directions_btn_${location.id}"),
                        colors = ButtonDefaults.buttonColors(containerColor = OceanBlue),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Directions,
                            contentDescription = "Directions",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଦିଗ ନିର୍ଦ୍ଦେଶ" else "Directions",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // Map Pin View Button
                    OutlinedButton(
                        onClick = onOpenMap,
                        modifier = Modifier
                            .height(36.dp)
                            .testTag("view_map_btn_${location.id}"),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = "Map",
                            tint = Color(0xFF334155),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}
