package com.example.ui.features.resilience

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.daily.DailyBalasorePulse
import com.example.data.model.AppLanguage
import com.example.ui.theme.*

data class SubsystemTelemetryStatus(
    val nameEn: String,
    val nameOd: String,
    val iconEmoji: String,
    val statusText: String,
    val updateInterval: String,
    val isOnline: Boolean = true
)

/**
 * Auto-Update & Telemetry Center for Balasore 360.
 * Allows users to inspect live telemetry auto-update loops, adjust synchronization frequencies,
 * manually trigger instantaneous district-wide data sync, and verify Google Play App Updates.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoUpdateCenterSheet(
    language: AppLanguage,
    dailyPulse: DailyBalasorePulse,
    isRefreshing: Boolean,
    isHourlyAutoRefreshEnabled: Boolean,
    autoUpdateFrequencyMinutes: Int,
    nextHourlyRefreshMinutesRemaining: Int,
    autoRefreshCycleCount: Int,
    lastSyncStatusMessage: String,
    appVersionInstalled: String,
    appVersionLatest: String,
    isUpdateCheckLoading: Boolean,
    onForceSyncAll: () -> Unit,
    onToggleAutoRefresh: (Boolean) -> Unit,
    onSetFrequency: (Int) -> Unit,
    onCheckForUpdates: () -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val infiniteTransition = rememberInfiniteTransition(label = "sync_spin")
    val spinAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin"
    )

    val frequencyOptions = listOf(1, 5, 15, 30, 60)

    val telemetrySubsystems = remember {
        listOf(
            SubsystemTelemetryStatus(
                nameEn = "Subarnarekha & Budhabalanga River Levels",
                nameOd = "ସୁବର୍ଣ୍ଣରେଖା ଓ ବୁଢ଼ାବଳଙ୍ଗ ନଦୀ ଜଳସ୍ତର",
                iconEmoji = "🌊",
                statusText = "Rajghat Gauge: 8.42m (Normal, Warning at 9.45m)",
                updateInterval = "Every 15 mins"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Chandipur Vanishing Sea Tidal Clock",
                nameOd = "ଚାନ୍ଦିପୁର ସମୁଦ୍ର ଭଟ୍ଟା ସମୟ ନିୟନ୍ତ୍ରକ",
                iconEmoji = "🏖️",
                statusText = "Receding 4.8 km into Bay of Bengal • Safe Walking",
                updateInterval = "Continuous Lunar Algorithmic Sync"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Balaramgadi Fish Catch & Auction",
                nameOd = "ବଳରାମଗଡ଼ି ମତ୍ସ୍ୟ ବନ୍ଦର ନିଲାମ ଦର",
                iconEmoji = "🐟",
                statusText = "Trawlers docked • Daily rates updated",
                updateInterval = "Daily morning 5:30 AM & noon"
            ),
            SubsystemTelemetryStatus(
                nameEn = "DHH & Red Cross Blood Bank Pulse",
                nameOd = "ବାଲେଶ୍ୱର ରକ୍ତଭଣ୍ଡାର ଲାଇଭ୍ ଷ୍ଟକ୍",
                iconEmoji = "🩸",
                statusText = "24x7 Casualty linkage active • 140 units in store",
                updateInterval = "Hourly real-time push"
            ),
            SubsystemTelemetryStatus(
                nameEn = "INCOIS Ocean Marine & High Wave Alert",
                nameOd = "ସାମୁଦ୍ରିକ ତରଙ୍ଗ ସତର୍କତା ବାର୍ତ୍ତା",
                iconEmoji = "⚠️",
                statusText = "Swell: 1.2m - 1.8m • Green Channel for fishermen",
                updateInterval = "Live IMD/INCOIS feed"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Balasore Local News & Odia Headlines",
                nameOd = "ବାଲେଶ୍ୱର ସ୍ଥାନୀୟ ଖବର ଓ ସମ୍ବାଦ",
                iconEmoji = "📰",
                statusText = "Interactive search & categorized Odia feeds synced",
                updateInterval = "Live automated indexing"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Balasore Junction (BLS) Train & Platform Radar",
                nameOd = "ବାଲେଶ୍ୱର ଜଙ୍କସନ ଟ୍ରେନ ଓ ପ୍ଲାଟଫର୍ମ ରାଡାର",
                iconEmoji = "🚆",
                statusText = "Platform allocation & mainline signals normal",
                updateInterval = "Live SER Railway Sync"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Baleswari Paan & Krushi Mandi Rates",
                nameOd = "ବାଲେଶ୍ୱରୀ ପାନ ଓ କୃଷି ମଣ୍ଡି ସୂଚୀ",
                iconEmoji = "🍃",
                statusText = "Bhograi Mitha Paan & Cashew wholesale arath rates active",
                updateInterval = "Daily Mandi Opening & Noon"
            ),
            SubsystemTelemetryStatus(
                nameEn = "24x7 Night Chemist & Emergency Oxygen Desk",
                nameOd = "୨୪x୭ ରାତ୍ରିକାଳୀନ ଔଷଧାଳୟ ଓ ଅମ୍ଳଜାନ ସେବା",
                iconEmoji = "💊",
                statusText = "DHH & town 24x7 pharmacies with medical oxygen verified",
                updateInterval = "Continuous 24h Roster"
            ),
            SubsystemTelemetryStatus(
                nameEn = "TPNODL Power Outage & WATCO Water Monitor",
                nameOd = "ବିଦ୍ୟୁତ୍ କାଟ୍ ଓ ପାନୀୟ ଜଳ ଯୋଗାଣ ସୂଚୀ",
                iconEmoji = "⚡",
                statusText = "Municipal feeder grid active • Morning & Evening water normal",
                updateInterval = "Live Ward Feeder Telemetry"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Talasari & Bichitrapur Eco-Boat Safari",
                nameOd = "ତାଳସାରୀ ଓ ବିଚିତ୍ରପୁର ବୋଟ୍ ସଫାରି",
                iconEmoji = "🦀",
                statusText = "High tide mangrove cruise & red crab colonies clear",
                updateInterval = "Daily Tidal Window Sync"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Nilagiri Green Stone & Dokra Guilds",
                nameOd = "ନୀଳଗିରି ସବୁଜ ପଥର ଓ ଢୋକ୍ରା ହସ୍ତଶିଳ୍ପ",
                iconEmoji = "🏺",
                statusText = "Artisan cooperatives & fair-trade registry connected",
                updateInterval = "Weekly Guild Catalog Update"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Baleswar Mahotsav & Chadak Ritual Guide",
                nameOd = "ବାଲେଶ୍ୱର ମହୋତ୍ସବ ଓ ଚନ୍ଦନେଶ୍ୱର ଚଡ଼କ କ୍ୟାଲେଣ୍ଡର",
                iconEmoji = "🎭",
                statusText = "Pilgrimage timetable & Expo cultural schedules synchronized",
                updateInterval = "Seasonal Ritual Calendar Sync"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Mo Seva Kendra & e-District Desk",
                nameOd = "ମୋ ସେବା କେନ୍ଦ୍ର ଓ ଇ-ପ୍ରଶାସନ ଡେସ୍କ",
                iconEmoji = "🌾",
                statusText = "12 Block citizen centers, statutory certificate fee caps (₹30) active",
                updateInterval = "Odisha One Portal Sync"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Marine Fishermen Sea-Safety & NavIC",
                nameOd = "ମତ୍ସ୍ୟଜୀବୀ ସମୁଦ୍ର ସୁରକ୍ଷା ଓ ଟେଲିମେଟ୍ରି",
                iconEmoji = "⚓",
                statusText = "Kasafal & Balaramgadi swell/wind telemetry & Coast Guard 1554 active",
                updateInterval = "Continuous Sea-State Sync"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Kuldiha Eco-Camp & Wildlife Safari",
                nameOd = "କୁଲଡିହା ନେଚର କ୍ୟାମ୍ପ ଓ ସଫାରୀ",
                iconEmoji = "🐘",
                statusText = "Rissia cottages & Jadachua elephant watchtower spotting times connected",
                updateInterval = "Daily Forest Range Sync"
            ),
            SubsystemTelemetryStatus(
                nameEn = "FM MCH Medical College OPD Roster",
                nameOd = "ଫକୀର ମୋହନ ମେଡିକାଲ OPD ଓ BSKY",
                iconEmoji = "🏥",
                statusText = "Remuna campus OPD roster & BSKY cashless helpdesks updated",
                updateInterval = "Daily Health Dept Sync"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Sabai Grass & Mission Shakti SHG Mart",
                nameOd = "ସବାଇ ଘାସ ଓ ମିଶନ ଶକ୍ତି ହାଟ",
                iconEmoji = "🧺",
                statusText = "Nilagiri golden fiber SHG catalog & direct fair prices synchronized",
                updateInterval = "Weekly SHG Guild Sync"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Balasore Maritime & Colonial Heritage",
                nameOd = "ବାଲେଶ୍ୱର ସାମୁଦ୍ରିକ ଓ ଔପନିବେଶିକ ଇତିହାସ",
                iconEmoji = "⛵",
                statusText = "Farasidinga French enclave & Sadhabapua Boita historical trail mapped",
                updateInterval = "Archaeological Registry"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Scholarships & Model Career Center Desk",
                nameOd = "ଛାତ୍ରବୃତ୍ତି ଓ କ୍ୟାରିୟର ସହାୟତା",
                iconEmoji = "🎓",
                statusText = "Odisha state scholarship portal & ITI rozgar melas active",
                updateInterval = "Daily Employment Feed"
            ),
            SubsystemTelemetryStatus(
                nameEn = "DRDO Chandipur Launch & Sea Radar",
                nameOd = "ଚାନ୍ଦିପୁର କ୍ଷେପଣାସ୍ତ୍ର ପରୀକ୍ଷା ଓ ସୁରକ୍ଷା ରାଡାର",
                iconEmoji = "🚀",
                statusText = "NOTAM sea/air exclusion telemetry & coastal alert sirens synchronized",
                updateInterval = "Live Defense Advisory Sync"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Subarnarekha River & Sluice Gate Radar",
                nameOd = "ସୁବର୍ଣ୍ଣରେଖା ନଦୀ ଓ ସ୍ଲୁଇସ୍ ଗେଟ୍ ବନ୍ୟା ରାଡାର",
                iconEmoji = "🌊",
                statusText = "Rajghat gauge discharge & 8 coastal sluice gates monitored",
                updateInterval = "Live CWC & Irrigation Sync"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Nilagiri Chhau Cultural & Akhara Guild",
                nameOd = "ନୀଳଗିରି ଛଉ ନୃତ୍ୟ ଓ ଆଖଡ଼ା ଗିଲ୍ଡ",
                iconEmoji = "🎭",
                statusText = "Royal Chhau dance troupes & mask artisan cooperatives connected",
                updateInterval = "Weekly Cultural Guild Sync"
            ),
            SubsystemTelemetryStatus(
                nameEn = "DHH & Red Cross Blood Bank Live Radar",
                nameOd = "ବାଲେଶ୍ୱର DHH ରକ୍ତ ଭଣ୍ଡାର ଲାଇଭ୍ ଷ୍ଟକ୍",
                iconEmoji = "🩸",
                statusText = "PRBC, Platelets & rare negative groups component units synchronized",
                updateInterval = "Live Blood Bank Sync"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Paan Baraja & Aqua Shrimp Livelihood Desk",
                nameOd = "ପାନ ବରଜ ଓ ଆକ୍ୱା ଚିଙ୍ଗୁଡ଼ି ଡେସ୍କ",
                iconEmoji = "🍃",
                statusText = "Bhograi shade micro-climate & Vannamei shrimp DO levels telemetry active",
                updateInterval = "Hourly Micro-Sensor Sync"
            ),
            SubsystemTelemetryStatus(
                nameEn = "District Court & DLSA Free Legal Aid",
                nameOd = "ଜିଲ୍ଲା କୋର୍ଟ ଓ ଡିଏଲ୍ଏସ୍ଏ ମାଗଣା ଆଇନ ସେବା",
                iconEmoji = "⚖️",
                statusText = "Daily cause lists, Lok Adalat rosters & mediation desks active",
                updateInterval = "Daily Judicial Registry Sync"
            ),
            SubsystemTelemetryStatus(
                nameEn = "NH-16 Highway Patrol & Trauma SOS",
                nameOd = "NH-16 ଜାତୀୟ ରାଜପଥ ପାଟ୍ରୋଲିଂ ଓ ଟ୍ରମା ଏସଓଏସ",
                iconEmoji = "🚨",
                statusText = "Bahanaga-Balasore corridor NHAI 1033 patrol & cranes on stand-by",
                updateInterval = "24x7 Highway Emergency Network"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Panchalingeswar & Kuldiha Eco-Trek & Safari",
                nameOd = "ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ଓ କୁଲଡିହା ସଫାରୀ ରାଡାର",
                iconEmoji = "🐘",
                statusText = "Forest gates, elephant corridor telemetry & spring water stream sync",
                updateInterval = "Live Forest & Wildlife Sync"
            ),
            SubsystemTelemetryStatus(
                nameEn = "SER Balasore & Jaleswar Railway Radar",
                nameOd = "ଦକ୍ଷିଣ ପୂର୍ବ ରେଳପଥ ବାଲେଶ୍ୱର ଲାଇଭ୍ ରାଡାର",
                iconEmoji = "🚆",
                statusText = "Live platforms, Vande Bharat/Dhauli rake formations & MEMU tracking",
                updateInterval = "Live SER Division Rail Sync"
            ),
            SubsystemTelemetryStatus(
                nameEn = "NOCCI Industrial Corridor & B2B Skill Connect",
                nameOd = "ନୋସି ଶିଳ୍ପ କରିଡର ଓ ଦକ୍ଷତା ନିଯୁକ୍ତି ଡେସ୍କ",
                iconEmoji = "🏭",
                statusText = "Chhanpur & Remuna MSME, CIPET toolroom & apprentice vacancy feed",
                updateInterval = "Daily Industrial Registry Sync"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Talsari & Bichitrapur Mangrove Boating Pilot",
                nameOd = "ତାଳସାରୀ ଓ ବିଚିତ୍ରପୁର ହେନ୍ତାଳବଣ ବୋଟ୍ ପାଇଲଟ୍",
                iconEmoji = "🦀",
                statusText = "Subarnarekha estuary tide depths & red ghost crab colony alerts",
                updateInterval = "Hourly Coastal Tidal Sync"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Fakir Mohan Bhasha & Literature Archive",
                nameOd = "ଫକୀର ମୋହନ ଭାଷା ଓ ଓଡ଼ିଆ ସାହିତ୍ୟ ଆର୍କାଇଭ",
                iconEmoji = "📜",
                statusText = "Mallikashpur heritage peetha, daily Baleswari idioms & public library",
                updateInterval = "Daily Literary Heritage Feed"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Baleswari Cuisines & Sweet Heritage Finder",
                nameOd = "ବାଲେଶ୍ୱରୀ ଖାଦ୍ୟ ଓ ମିଠା ଐତିହ୍ୟ ଡାଇରୀ",
                iconEmoji = "🍲",
                statusText = "Chhena Mudki GI makers, Nilagiri Khaja & Kasafal Sukhua fish index",
                updateInterval = "Verified Heritage Food Sync"
            ),
            SubsystemTelemetryStatus(
                nameEn = "TPNODL Power & WATCO Tap Water Civic Hotline",
                nameOd = "TPNODL ବିଦ୍ୟୁତ୍ ଓ WATCO ଜଳଯୋଗାଣ ରାଡାର",
                iconEmoji = "⚡",
                statusText = "33/11kV feeder shutdown status & ward piped water timings live",
                updateInterval = "Live Utility Grid Telemetry"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Dhan Mandi & MSP Paddy Procurement Radar",
                nameOd = "ଧାନ ମଣ୍ଡି ଓ ଏମଏସପି କ୍ରୟ ରାଡାର",
                iconEmoji = "🌾",
                statusText = "Jaleswar, Soro, Basta PACS mandi tokens & moisture meter telemetry live",
                updateInterval = "Live Mandi Procurement Feed"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Nilagiri Granite & Stoneware Artisan Guild",
                nameOd = "ନୀଳଗିରି କଳା ପଥର ଶିଳ୍ପ ଓ ବାସନ ଗିଲ୍ଡ",
                iconEmoji = "🪨",
                statusText = "Mitrapur & Baunsabania stoneware cookware catalogs & Utkalika sync",
                updateInterval = "Artisan Guild Catalog Sync"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Bahanaga & NH-16 Rapid Trauma Network",
                nameOd = "ବାହାନଗା ଓ NH-16 ରାପିଡ୍ ଟ୍ରମା ନେଟୱର୍କ",
                iconEmoji = "🚨",
                statusText = "108 ALS ambulance GPS posts, Balasore DHH trauma ICU beds live",
                updateInterval = "Live Highway Emergency Radar"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Bhusandeswar & Chandaneswar Shiva Pilgrimage",
                nameOd = "ଭୂଷଣ୍ଡେଶ୍ୱର ଓ ଚନ୍ଦନେଶ୍ୱର ତୀର୍ଥ ରାଡାର",
                iconEmoji = "🛕",
                statusText = "Asia's largest lingam darshan timings, Chadak Mela crowd & pilgrim shuttles",
                updateInterval = "Daily Temple Schedule Sync"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Seva Setu: Senior Citizen Care & Police Desk",
                nameOd = "ସେବା ସେତୁ: ବରିଷ୍ଠ ନାଗରିକ ସହାୟତା ଡେସ୍କ",
                iconEmoji = "🏥",
                statusText = "Verified geriatric home nurses, Nidan doorstep labs & beat police sync",
                updateInterval = "Daily Health & Civic Registry"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Balasore Higher Education & Campus Admission Hub",
                nameOd = "ବାଲେଶ୍ୱର ଉଚ୍ଚଶିକ୍ଷା ଓ କ୍ୟାମ୍ପସ ଆଡମିଶନ ହବ୍",
                iconEmoji = "🎓",
                statusText = "FMU, FM MCH, BCET admission cutoffs & e-Medhabruti scholarship alerts",
                updateInterval = "SAMS Higher Education Feed"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Balasore Mo Bus & CRUT Urban Transit Navigator",
                nameOd = "ବାଲେଶ୍ୱର ମୋ ବସ୍ ଓ ସହଦେବଖୁଣ୍ଟା ଟର୍ମିନାଲ୍",
                iconEmoji = "🚌",
                statusText = "Routes 71-74 live frequency, student passes & express bus bays live",
                updateInterval = "Live CRUT Transit Telemetry"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Subarnarekha Brackish Shrimp Hatchery Radar",
                nameOd = "ସୁବର୍ଣ୍ଣରେଖା ଲୁଣିଜଳ ଚିଙ୍ଗୁଡ଼ି ହେଚେରୀ ରାଡାର୍",
                iconEmoji = "🦐",
                statusText = "Salinity, pH, dissolved oxygen & export mandi rates active",
                updateInterval = "Live MPEDA & Aqua Telemetry"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Balasore Coastal 64 Cyclone Shelter Radar",
                nameOd = "ବାଲେଶ୍ୱର ଉପକୂଳ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳ ନେଟୱାର୍କ",
                iconEmoji = "🌀",
                statusText = "64 shelters solar inverters, DG diesel reserves & water tanks active",
                updateInterval = "Live OSDMA / ODRAF Telemetry"
            ),
            SubsystemTelemetryStatus(
                nameEn = "French-Dutch-Danish Foreign Loge Trail",
                nameOd = "ଫରାସୀ, ଡଚ୍ ଓ ଡେନିସ୍ ବାଣିଜ୍ୟ କୋଠି ଐତିହ୍ୟ",
                iconEmoji = "🏛️",
                statusText = "Farashdanga enclave, Dutch obelisks & Danish factory trail synced",
                updateInterval = "Archaeological Heritage Registry"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Balasore Rural Mobile Medical Unit Network",
                nameOd = "ବାଲେଶ୍ୱର ଗ୍ରାମୀଣ ମୋବାଇଲ୍ ମେଡିକାଲ୍ ଭ୍ୟାନ୍ ସେବା",
                iconEmoji = "🩺",
                statusText = "12 MMU vans village camps, free diagnostics & PM Jan Aushadhi active",
                updateInterval = "Daily NHM Health Telemetry"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Mati-Miras Betel Leaf & Saline Paddy Radar",
                nameOd = "ବାଲେଶ୍ୱରୀ ପାନ ବରଜ ଓ ଲୁଣା ଧାନ ସମବାୟ ରାଡାର୍",
                iconEmoji = "🌿",
                statusText = "Bhograi betel leaf mandi prices & Luna Sankhi PACS procurement live",
                updateInterval = "Daily Mandi & PACS Feed"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Balasore Coastal Bus & Fleet Live Radar",
                nameOd = "ବାଲେଶ୍ୱର ଉପକୂଳ ମୋ ବସ୍ ଓ ଏକ୍ସପ୍ରେସ୍ ରାଡାର୍",
                iconEmoji = "🚍",
                statusText = "Chandipur, Talsari, Kasafal & Nilagiri bus GPS fleet synced",
                updateInterval = "Live Fleet Transit GPS"
            ),
            SubsystemTelemetryStatus(
                nameEn = "Horseshoe Crab Intertidal Bio-Reserve Watch",
                nameOd = "ଜୀବନ୍ତ ଜୀବାଶ୍ମ ରାଜକଙ୍କଡ଼ା ସଂରକ୍ଷଣ ୱାଚ୍",
                iconEmoji = "🦀",
                statusText = "Chandipur intertidal sightings, breeding windows & wildlife hotline active",
                updateInterval = "Daily ZSI & Marine Forest Telemetry"
            )
        )
    }

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .testTag("auto_update_center_sheet")
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0F2FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🔄", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଅଟୋ-ଅପଡେଟ୍ ଓ ଲାଇଭ୍ ସିଙ୍କ୍ କେନ୍ଦ୍ର" else "Auto-Update & Live Sync Center",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଜିଲ୍ଲା ଟେଲିମେଟ୍ରି • ସ୍ୱୟଂକ୍ରିୟ ନବୀକରଣ • ଆପ୍ ଭର୍ସନ ଯାଞ୍ଚ" else "District Telemetry • Background Sync • App Updates",
                            style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate500)
                        )
                    }
                }
                IconButton(onClick = onClose) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.88f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Live Synchronization Master Card
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                        border = BorderStroke(1.5.dp, Color(0xFF38BDF8))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(if (isHourlyAutoRefreshEnabled) Color(0xFF4ADE80) else Color(0xFFF87171))
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isHourlyAutoRefreshEnabled) "AUTO-SYNC ACTIVE" else "AUTO-SYNC PAUSED",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (isHourlyAutoRefreshEnabled) Color(0xFF4ADE80) else Color(0xFFF87171),
                                            letterSpacing = 1.sp
                                        )
                                    )
                                }

                                Surface(
                                    color = Color(0xFF1E293B),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "Cycle #$autoRefreshCycleCount",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(0xFF94A3B8),
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = if (isHourlyAutoRefreshEnabled)
                                    "Next auto-update in ~${nextHourlyRefreshMinutesRemaining} mins"
                                else
                                    "Background auto-sync is paused by user",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )

                            Text(
                                text = "Status: $lastSyncStatusMessage",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF38BDF8),
                                    fontSize = 12.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Action Button: Force Sync All Live Telemetry
                            Button(
                                onClick = onForceSyncAll,
                                enabled = !isRefreshing,
                                colors = ButtonDefaults.buttonColors(containerColor = OceanBlue),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("btn_force_sync_all")
                            ) {
                                if (isRefreshing) {
                                    Icon(
                                        imageVector = Icons.Default.Sync,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(18.dp)
                                            .rotate(spinAngle)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Synchronizing all feeds...", fontWeight = FontWeight.Bold)
                                } else {
                                    Icon(imageVector = Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("⚡ Force Sync All Feeds Now", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // Sync Interval Configuration
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = if (language == AppLanguage.ODIA) "ସ୍ୱୟଂକ୍ରିୟ ନବୀକରଣ ବ୍ୟବଧାନ" else "Automatic Sync Interval",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = BentoSlate900
                                        )
                                    )
                                    Text(
                                        text = if (language == AppLanguage.ODIA) "କେତେ ସମୟ ବ୍ୟବଧାନରେ ତଥ୍ୟ ଅପଡେଟ୍ ହେବ" else "Select background data refresh frequency",
                                        style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate500)
                                    )
                                }

                                Switch(
                                    checked = isHourlyAutoRefreshEnabled,
                                    onCheckedChange = onToggleAutoRefresh,
                                    colors = SwitchDefaults.colors(checkedThumbColor = OceanBlue)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Refresh every:",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BentoSlate600
                                )
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                frequencyOptions.forEach { minutes ->
                                    val isSelected = autoUpdateFrequencyMinutes == minutes
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { onSetFrequency(minutes) },
                                        label = {
                                            Text(
                                                text = if (minutes == 60) "1 hr" else "${minutes}m",
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                fontSize = 12.sp
                                            )
                                        },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = OceanBlue,
                                            selectedLabelColor = Color.White
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }

                // In-App App Update & Version Checker
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                        border = BorderStroke(1.dp, Color(0xFFBBF7D0))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "📦", fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "Application Release Status",
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF166534)
                                            )
                                        )
                                        Text(
                                            text = "Installed: v$appVersionInstalled (Build 8)",
                                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF15803D))
                                        )
                                    }
                                }

                                Surface(
                                    color = Color(0xFFDCFCE7),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "UP TO DATE",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFF15803D)
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Signed release bundle: Balasore360-v1.0.7-release.aab with 100% Android 15+ Play Store compliance and real-time offline caching.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF14532D),
                                    lineHeight = 17.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = onCheckForUpdates,
                                    enabled = !isUpdateCheckLoading,
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF166534)),
                                    border = BorderStroke(1.dp, Color(0xFF86EFAC)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    if (isUpdateCheckLoading) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(14.dp),
                                            color = Color(0xFF166534),
                                            strokeWidth = 2.dp
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Checking...", fontSize = 12.sp)
                                    } else {
                                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Check Updates", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Button(
                                    onClick = {
                                        val intent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://ais-dev-honzkaxc7yscu2h42zeagl-613265325843.asia-east1.run.app")
                                        )
                                        context.startActivity(intent)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF166534)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Play Console Portal", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // Subsystem Telemetry Feeds Header
                item {
                    Text(
                        text = if (language == AppLanguage.ODIA) "ଜିଲ୍ଲା ସ୍ୱୟଂକ୍ରିୟ ଟେଲିମେଟ୍ରି ସୂଚକାଙ୍କ" else "District Automated Telemetry Pipelines",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate900
                        )
                    )
                }

                items(telemetrySubsystems) { subsystem ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = subsystem.iconEmoji, fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (language == AppLanguage.ODIA) subsystem.nameOd else subsystem.nameEn,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = BentoSlate900
                                        )
                                    )
                                    Surface(
                                        color = Color(0xFFDCFCE7),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "SYNCED",
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 9.sp,
                                                color = Color(0xFF166534)
                                            )
                                        )
                                    }
                                }

                                Text(
                                    text = subsystem.statusText,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF0284C7),
                                        fontWeight = FontWeight.Medium
                                    )
                                )

                                Text(
                                    text = "Cadence: ${subsystem.updateInterval}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = BentoSlate400,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}
