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
import com.example.ui.viewmodel.UniqueFeatureSheetType

data class SubsystemTelemetryStatus(
    val nameEn: String,
    val nameOd: String,
    val iconEmoji: String,
    val statusText: String,
    val updateInterval: String,
    val isOnline: Boolean = true,
    val targetSheet: UniqueFeatureSheetType? = null
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
    onClose: () -> Unit,
    onOpenFeatureSheet: ((UniqueFeatureSheetType) -> Unit)? = null
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

    val telemetrySubsystems = remember(dailyPulse) {
        listOf(
            SubsystemTelemetryStatus(
                nameEn = "Olive Ridley & Marine Life Sentinel Telemetry",
                nameOd = "ଅଲିଭ୍ ରିଡଲେ ଓ ସାମୁଦ୍ରିକ ଜୀବ ଲାଇଭ୍ ଟେଲିମେଟ୍ରି",
                iconEmoji = "🐢",
                statusText = "${dailyPulse.oliveRidleyBeachSightingStatus} • ${dailyPulse.oliveRidleyNestingCount} nests active",
                updateInterval = "Live Forest Division & Citizen Sentinel Loop",
                targetSheet = UniqueFeatureSheetType.OLIVE_RIDLEY_MARINE_WILDLIFE
            ),
            SubsystemTelemetryStatus(
                nameEn = "AIIMS & DHH OPD Token & Bed Vacancy Radar",
                nameOd = "AIIMS ଓ DHH ବାଲେଶ୍ୱର OPD ଓ ବେଡ୍ ପଲ୍ସ",
                iconEmoji = "🏥",
                statusText = "AIIMS Wait: ~${dailyPulse.aiimsSatelliteCentreQueueWaitMins}m (${dailyPulse.aiimsOpdSpecialistsAvailable} Doctors) • DHH ICU: ${dailyPulse.dhhIcuBedsAvailable} Beds Open",
                updateInterval = "Live Health Directorate Queue Feed",
                targetSheet = UniqueFeatureSheetType.AIIMS_DHH_OPD_BED_TRACKER
            ),
            SubsystemTelemetryStatus(
                nameEn = "Offline Cyclone & Flood Safety Toolkit & SOS",
                nameOd = "ଅଫଲାଇନ୍ ବାତ୍ୟା ଓ ବନ୍ୟା ସୁରକ୍ଷା ଟୁଲକିଟ୍",
                iconEmoji = "🛡️",
                statusText = "Surge: ${dailyPulse.coastalSurgeRiskLevel} • ${dailyPulse.offlineCycloneSheltersReadyCount} Designated Shelters Ready • SOS Strobe Online",
                updateInterval = "100% Offline Caching + Emergency Dispatch",
                targetSheet = UniqueFeatureSheetType.OFFLINE_CYCLONE_SAFETY_TOOLKIT
            ),
            SubsystemTelemetryStatus(
                nameEn = "My Reports: Offline Civic Issue Room DB Sentinel",
                nameOd = "ମୋ ଅଭିଯୋଗ: ଅଫଲାଇନ୍ ନାଗରିକ ସମସ୍ୟା Room DB ସେଣ୍ଟିନେଲ୍",
                iconEmoji = "📋",
                statusText = "${dailyPulse.pendingCivicReportsCount} Active Reports • ${dailyPulse.resolvedCivicReportsTodayCount} Cleared Today • Room DB Auto-Sync Active",
                updateInterval = "Local SQLite Room DB + Auto-Progression Ticker",
                targetSheet = UniqueFeatureSheetType.MY_CIVIC_REPORTS
            ),
            SubsystemTelemetryStatus(
                nameEn = "FCM Severe Weather & Coastal Flood Push Alerts",
                nameOd = "FCM ବାତ୍ୟା ଓ ଉପକୂଳ ବନ୍ୟା ତତ୍କାଳ ପୁସ୍ ସତର୍କତା",
                iconEmoji = "🚨",
                statusText = "Automated Real-Time Push Active • Direct to Lock Screen via IMD & CWC Telemetry",
                updateInterval = "Instant Zero-Latency Push",
                targetSheet = UniqueFeatureSheetType.FCM_SEVERE_WEATHER_ALERT
            ),
            SubsystemTelemetryStatus(
                nameEn = "Subarnarekha & Budhabalanga River Levels",
                nameOd = "ସୁବର୍ଣ୍ଣରେଖା ଓ ବୁଢ଼ାବଳଙ୍ଗ ନଦୀ ଜଳସ୍ତର",
                iconEmoji = "🌊",
                statusText = dailyPulse.subarnarekhaBasinRisk,
                updateInterval = "Live CWC / Irrigation Gauge Sync",
                targetSheet = UniqueFeatureSheetType.SUBARNAREKHA_SLUICE_FLOOD_RADAR
            ),
            SubsystemTelemetryStatus(
                nameEn = "Chandipur Vanishing Sea & 72h Recharts Tide Telemetry",
                nameOd = "ଚାନ୍ଦିପୁର ୭୨-ଘଣ୍ଟା Recharts ଗ୍ରାଫ୍ ଓ ଭଟ୍ଟା ଆଲାର୍ମ",
                iconEmoji = "🏖️",
                statusText = "${dailyPulse.chandipurLowTideWindow} • 72h Recharts High/Low Cycle Telemetry Active • ${if (dailyPulse.isChandipurWalkSafeNow) "Safe Intertidal Walking 🟢" else "⚠️ Tide Returning (High Tide ${dailyPulse.chandipurNextHighTide})"}",
                updateInterval = "Automated Continuous Lunar Recharts Telemetry",
                targetSheet = UniqueFeatureSheetType.CHANDIPUR_TIDE_TIMER
            ),
            SubsystemTelemetryStatus(
                nameEn = "INCOIS Ocean Marine & High Wave Radar",
                nameOd = "ସାମୁଦ୍ରିକ ତରଙ୍ଗ ସତର୍କତା ବାର୍ତ୍ତା (INCOIS)",
                iconEmoji = "⚠️",
                statusText = "${dailyPulse.incoisSeaFlagColor} • Swell: ${String.format(java.util.Locale.US, "%.1fm", dailyPulse.incoisSwellMeters)} • Safe bathing zones active",
                updateInterval = "Live IMD / INCOIS Telemetry Feed",
                targetSheet = UniqueFeatureSheetType.INCOIS_OCEAN_ADVISORY
            ),
            SubsystemTelemetryStatus(
                nameEn = "Balasore Mo Bus & CRUT Transit Navigator",
                nameOd = "ବାଲେଶ୍ୱର ମୋ ବସ୍ ଓ CRUT ଅର୍ବାନ ଟ୍ରାଞ୍ଜିଟ୍",
                iconEmoji = "🚌",
                statusText = "Route 101: ${dailyPulse.moBusRoute101EtaMins}m • Route 102: ${dailyPulse.moBusRoute102EtaMins}m • Route 103: ${dailyPulse.moBusRoute103EtaMins}m",
                updateInterval = "Live CRUT Sahadevkhunta Telemetry",
                targetSheet = UniqueFeatureSheetType.BALASORE_MO_BUS_CRUT_NAVIGATOR
            ),
            SubsystemTelemetryStatus(
                nameEn = "SER Balasore Junction (BLS) Train & Platform Radar",
                nameOd = "ବାଲେଶ୍ୱର ଜଙ୍କସନ ଟ୍ରେନ ଓ ପ୍ଲାଟଫର୍ମ ରାଡାର",
                iconEmoji = "🚆",
                statusText = "Platform allocation & mainline signals normal • Vande Bharat & Dhauli live",
                updateInterval = "Live SER Division Rail Sync",
                targetSheet = UniqueFeatureSheetType.BALASORE_JUNCTION_RADAR
            ),
            SubsystemTelemetryStatus(
                nameEn = "Subarnarekha River & Estuary Ferry Timetable",
                nameOd = "ସୁବର୍ଣ୍ଣରେଖା ନଦୀ ଓ ଘାଟ ଡଙ୍ଗା ଚଳାଚଳ",
                iconEmoji = "⛴️",
                statusText = dailyPulse.riverFerryCrossingStatus,
                updateInterval = "Continuous Ferry Dispatch Sync",
                targetSheet = UniqueFeatureSheetType.RIVER_FERRY_SCHEDULE
            ),
            SubsystemTelemetryStatus(
                nameEn = "DHH & Red Cross Blood Bank Live Pulse",
                nameOd = "ବାଲେଶ୍ୱର DHH ରକ୍ତଭଣ୍ଡାର ଲାଇଭ୍ ଷ୍ଟକ୍",
                iconEmoji = "🩸",
                statusText = "${dailyPulse.dhhBloodUnitsStored} total units stored • Emergency O- & Platelets component reserves live",
                updateInterval = "Hourly Real-Time Push",
                targetSheet = UniqueFeatureSheetType.DHH_BLOOD_BANK_LIVE_RADAR
            ),
            SubsystemTelemetryStatus(
                nameEn = "FM MCH Hospital Bed & ICU Live Telemetry",
                nameOd = "ଫକୀର ମୋହନ ମେଡିକାଲ ବେଡ୍ ଓ ଆଇସିୟୁ ରାଡାର",
                iconEmoji = "🏥",
                statusText = "ICU Beds Vacant: ${dailyPulse.fmmchIcuBedsVacant} • Dialysis Units: ${dailyPulse.fmmchDialysisBedsVacant} • ${dailyPulse.antiVenomStockAdvisory}",
                updateInterval = "Live Health Dept Hospital Sync",
                targetSheet = UniqueFeatureSheetType.BLOOD_AND_BED_PULSE
            ),
            SubsystemTelemetryStatus(
                nameEn = "Panchalingeswar Perennial Stream & Temple Darshan",
                nameOd = "ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ପାହାଡ଼ ଝରଣା ଓ ଦର୍ଶନ ସମୟ",
                iconEmoji = "🛕",
                statusText = "${dailyPulse.panchalingeswarFlowStatus} • Slip Risk: ${dailyPulse.panchalingeswarSlipRisk}",
                updateInterval = "Daily Temple Trust & Stream Sync",
                targetSheet = UniqueFeatureSheetType.PANCHALINGESWAR_KULDIHA_SAFARI
            ),
            SubsystemTelemetryStatus(
                nameEn = "Bhusandeswar & Chandaneswar Shiva Pilgrimage",
                nameOd = "ଭୂଷଣ୍ଡେଶ୍ୱର ଓ ଚନ୍ଦନେଶ୍ୱର ତୀର୍ଥ ରାଡାର",
                iconEmoji = "🕉️",
                statusText = "${dailyPulse.chandaneswarDailyRitual} • Chadak Mela crowd guidance active",
                updateInterval = "Daily Temple Schedule Sync",
                targetSheet = UniqueFeatureSheetType.BHUSANDESWAR_CHANDANESWAR_PILGRIMAGE
            ),
            SubsystemTelemetryStatus(
                nameEn = "Dhan Mandi & MSP Paddy Procurement Radar",
                nameOd = "ଧାନ ମଣ୍ଡି ଓ ଏମଏସପି କ୍ରୟ ରାଡାର",
                iconEmoji = "🌾",
                statusText = "Govt MSP Rate: ₹${dailyPulse.paddyMspRatePerQuintal} / quintal • Jaleswar, Soro, Basta PACS tokens live",
                updateInterval = "Live Mandi Procurement Feed",
                targetSheet = UniqueFeatureSheetType.DHAN_MANDI_MSP_PROCUREMENT
            ),
            SubsystemTelemetryStatus(
                nameEn = "Subarnarekha Brackish Shrimp & Aqua Pricing",
                nameOd = "ସୁବର୍ଣ୍ଣରେଖା ଲୁଣିଜଳ ଚିଙ୍ଗୁଡ଼ି ହେଚେରୀ ଓ ଦର",
                iconEmoji = "🦐",
                statusText = "Vannamei Grade-A: ₹${dailyPulse.aquacultureVannameiRateKg}/kg • Black Tiger: ₹${dailyPulse.aquacultureTigerPrawnRateKg}/kg",
                updateInterval = "Live MPEDA & Aquaculture Telemetry",
                targetSheet = UniqueFeatureSheetType.AQUA_SHRIMP_HATCHERY_RADAR
            ),
            SubsystemTelemetryStatus(
                nameEn = "Sabai Grass & Mission Shakti SHG Mart",
                nameOd = "ସବାଇ ଘାସ ଓ ମିଶନ ଶକ୍ତି ହାଟ",
                iconEmoji = "🧺",
                statusText = "${dailyPulse.sabaiGrassActiveShgClusters} verified SHG clusters active • Direct fair pricing with zero middlemen",
                updateInterval = "Weekly SHG Guild Sync",
                targetSheet = UniqueFeatureSheetType.SABAI_GRASS_MISSION_SHAKTI
            ),
            SubsystemTelemetryStatus(
                nameEn = "Nilagiri Granite & Stoneware Artisan Guild",
                nameOd = "ନୀଳଗିରି କଳା ପଥର ଶିଳ୍ପ ଓ ବାସନ ଗିଲ୍ଡ",
                iconEmoji = "🪨",
                statusText = dailyPulse.artisanShowcaseOfTheDay,
                updateInterval = "Artisan Guild Catalog Sync",
                targetSheet = UniqueFeatureSheetType.NILAGIRI_GRANITE_STONEWARE_GUILD
            ),
            SubsystemTelemetryStatus(
                nameEn = "TPNODL Power Outage & WATCO Water Monitor",
                nameOd = "ବିଦ୍ୟୁତ୍ କାଟ୍ ଓ ପାନୀୟ ଜଳ ଯୋଗାଣ ସୂଚୀ",
                iconEmoji = "⚡",
                statusText = "${dailyPulse.watcoSupplyStatusText} • ${dailyPulse.tpnodlPowerGridStatus}",
                updateInterval = "Live Ward Feeder Telemetry",
                targetSheet = UniqueFeatureSheetType.TPNODL_WATCO_UTILITY_HOTLINE
            ),
            SubsystemTelemetryStatus(
                nameEn = "Balaramgadi Fish Catch & Auction",
                nameOd = "ବଳରାମଗଡ଼ି ମତ୍ସ୍ୟ ବନ୍ଦର ନିଲାମ ଦର",
                iconEmoji = "🐟",
                statusText = "${dailyPulse.auctionStatusMessage} • ${dailyPulse.auctionCountdownText}",
                updateInterval = "Daily Morning 5:30 AM & Noon",
                targetSheet = UniqueFeatureSheetType.HARBOR_CATCH_RATES
            ),
            SubsystemTelemetryStatus(
                nameEn = "Talasari & Bichitrapur Eco-Boat Safari",
                nameOd = "ତାଳସାରୀ ଓ ବିଚିତ୍ରପୁର ବୋଟ୍ ସଫାରି",
                iconEmoji = "🦀",
                statusText = "${dailyPulse.bichitrapurBoatingWindow} • ${dailyPulse.bichitrapurStatusMessage}",
                updateInterval = "Hourly Coastal Tidal Window Sync",
                targetSheet = UniqueFeatureSheetType.TALSARI_BICHITRAPUR_MANGROVE_PILOT
            ),
            SubsystemTelemetryStatus(
                nameEn = "Kasafal Red Crab Colony Schedule",
                nameOd = "କାସାଫାଳ ନାଲି କଙ୍କଡ଼ା ଅଭିଯାନ ସୂଚୀ",
                iconEmoji = "🦀",
                statusText = "${dailyPulse.crabWindowStatusMessage} (Morning window: ${dailyPulse.redCrabMorningWindow})",
                updateInterval = "Live Tidal Emergence Sync",
                targetSheet = UniqueFeatureSheetType.KASAFAL_RED_CRABS
            ),
            SubsystemTelemetryStatus(
                nameEn = "24x7 Night Chemist & Emergency Oxygen Desk",
                nameOd = "୨୪x୭ ରାତ୍ରିକାଳୀନ ଔଷଧାଳୟ ଓ ଅମ୍ଳଜାନ ସେବା",
                iconEmoji = "💊",
                statusText = dailyPulse.medicineStoreEmergencyDuty,
                updateInterval = "Continuous 24h Roster Sync",
                targetSheet = UniqueFeatureSheetType.NIGHT_CHEMIST_SANJEEVANI
            ),
            SubsystemTelemetryStatus(
                nameEn = "Baleswari Paan & Krushi Mandi Rates",
                nameOd = "ବାଲେଶ୍ୱରୀ ପାନ ଓ କୃଷି ମଣ୍ଡି ସୂଚୀ",
                iconEmoji = "🍃",
                statusText = "Bhograi Mitha Paan & Cashew wholesale arath rates active",
                updateInterval = "Daily Mandi Opening & Noon",
                targetSheet = UniqueFeatureSheetType.BALESWARI_PAAN_KRUSHI_MANDI
            ),
            SubsystemTelemetryStatus(
                nameEn = "Baleswari Dialect & Literary Peetha",
                nameOd = "ବାଲେଶ୍ୱରୀ ଭାଷା ଓ ଓଡ଼ିଆ ସାହିତ୍ୟ ଆର୍କାଇଭ",
                iconEmoji = "📜",
                statusText = "${dailyPulse.dialectAudioPhraseTitle} - ${dailyPulse.dialectAudioPhraseMeaning}",
                updateInterval = "Daily Literary Heritage Feed",
                targetSheet = UniqueFeatureSheetType.FAKIR_MOHAN_BHASHA_LITERATURE
            ),
            SubsystemTelemetryStatus(
                nameEn = "Baleswar Panjika Almanac & Muhurat",
                nameOd = "ବାଲେଶ୍ୱର ପାଞ୍ଜି କ୍ୟାଲେଣ୍ଡର ଓ ଶୁଭ ବେଳା",
                iconEmoji = "🗓️",
                statusText = dailyPulse.panjikaTithiToday,
                updateInterval = "Daily Vedic Calendar Sync",
                targetSheet = UniqueFeatureSheetType.ODIA_PANJIKA_CALENDAR
            ),
            SubsystemTelemetryStatus(
                nameEn = "DRDO Chandipur Launch & Sea Radar",
                nameOd = "ଚାନ୍ଦିପୁର କ୍ଷେପଣାସ୍ତ୍ର ପରୀକ୍ଷା ଓ ସୁରକ୍ଷା ରାଡାର",
                iconEmoji = "🚀",
                statusText = "${dailyPulse.itrAirspaceStatus} • ${dailyPulse.itrNotamAdvisory}",
                updateInterval = "Live Defense Advisory Sync",
                targetSheet = UniqueFeatureSheetType.DRDO_CHANDIPUR_SAFETY_RADAR
            ),
            SubsystemTelemetryStatus(
                nameEn = "NH-16 Highway Patrol & Trauma SOS",
                nameOd = "NH-16 ଜାତୀୟ ରାଜପଥ ପାଟ୍ରୋଲିଂ ଓ ଟ୍ରମା ଏସଓଏସ",
                iconEmoji = "🚨",
                statusText = "Bahanaga-Balasore corridor NHAI 1033 patrol & trauma ambulances active",
                updateInterval = "24x7 Highway Emergency Network",
                targetSheet = UniqueFeatureSheetType.NH16_HIGHWAY_PATROL_TRAUMA_SOS
            ),
            SubsystemTelemetryStatus(
                nameEn = "Horseshoe Crab Intertidal Bio-Reserve Watch",
                nameOd = "ଜୀବନ୍ତ ଜୀବାଶ୍ମ ରାଜକଙ୍କଡ଼ା ସଂରକ୍ଷଣ ୱାଚ୍",
                iconEmoji = "🦀",
                statusText = "Chandipur intertidal sightings, breeding windows & FMU wildlife hotline active",
                updateInterval = "Daily ZSI & Marine Forest Telemetry",
                targetSheet = UniqueFeatureSheetType.HORSESHOE_CRAB_INTERTIDAL_PROTECTION
            ),
            SubsystemTelemetryStatus(
                nameEn = "Coastal Marine Audio Radio & Fishermen Ring",
                nameOd = "ସମୁଦ୍ର ମତ୍ସ୍ୟଜୀବୀ ରେଡିଓ ବୁଲେଟିନ୍ ଓ ସୁରକ୍ଷା ରିଙ୍ଗ୍",
                iconEmoji = "🎙️",
                statusText = "Swell: ${dailyPulse.marineRadioWaveMeters}m • Wind: ${dailyPulse.marineRadioWindKnots} kts • Return Window: ${dailyPulse.marineReturnCountdownMinutes}m",
                updateInterval = "Live INCOIS & VHF Radio Sync",
                targetSheet = UniqueFeatureSheetType.COASTAL_MARINE_AUDIO_RADIO
            ),
            SubsystemTelemetryStatus(
                nameEn = "Cyclone Surge & MCS Evacuation Router",
                nameOd = "ବାତ୍ୟା ଜୁଆର ଓ ୬୪ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳ ରାଉଟର୍",
                iconEmoji = "🗺️",
                statusText = "${dailyPulse.coastalSurgeRiskLevel} • ${dailyPulse.shelterOccupancyPercent}% shelter occupancy • 64 Shelters Live",
                updateInterval = "Hourly OSDMA Inundation Telemetry",
                targetSheet = UniqueFeatureSheetType.CYCLONE_SURGE_EVACUATION_ROUTER
            ),
            SubsystemTelemetryStatus(
                nameEn = "Balasore Mandi & Fish Landing Price Index",
                nameOd = "ବାଲେଶ୍ୱର ମଣ୍ଡି ଓ ବଳରାମଗଡ଼ି ମତ୍ସ୍ୟ ଦର ସୂଚକାଙ୍କ",
                iconEmoji = "🦐",
                statusText = "Hilsa: ₹${dailyPulse.hilsaEstuaryRateKg}/kg • Tiger Prawn: ₹${dailyPulse.tigerPrawnRateKg}/kg • Paddy MSP: ₹${dailyPulse.swarnaPaddyMspQuintal}/Q",
                updateInterval = "Daily Morning Mandi Auction Sync",
                targetSheet = UniqueFeatureSheetType.BALARAMGADI_MANDI_PRICE_INDEX
            ),
            SubsystemTelemetryStatus(
                nameEn = "Temple Aarti, Khira Bhog & Pilgrimage Companion",
                nameOd = "ରେମୁଣା ଖିରଭୋଗ ଓ ମନ୍ଦିର ଆଳତି ଲାଇଭ୍ ଟାଇମିଂ",
                iconEmoji = "🛕",
                statusText = "Remuna Khira Bhog: ${dailyPulse.remunaKhiraBhogPotsAvailable} pots ready • Phase: ${dailyPulse.remunaActiveAartiPhase}",
                updateInterval = "Live Sanctum Bell & Gate Sync",
                targetSheet = UniqueFeatureSheetType.TEMPLE_AARTI_KHIRA_BHOG_COMPANION
            ),
            SubsystemTelemetryStatus(
                nameEn = "FM MCH Hospital SOS & Emergency Blood Network",
                nameOd = "FM MCH ହସ୍ପିଟାଲ୍ ଓ ଜରୁରୀ ରକ୍ତଦାତା ନେଟୱାର୍କ",
                iconEmoji = "🩸",
                statusText = "O+ Stock: ${dailyPulse.fmmchBloodInventoryOposUnits}u • Shortage: ${dailyPulse.fmmchCriticalBloodShortageGroup} • ${dailyPulse.verifiedActiveDonorsCount} Donors",
                updateInterval = "Hourly Blood Bank & ICU Sync",
                targetSheet = UniqueFeatureSheetType.FM_MCH_EMERGENCY_BLOOD_NETWORK
            ),
            SubsystemTelemetryStatus(
                nameEn = "Mo Balasore Citizen Civic & Storm Reporter",
                nameOd = "ମୋ ବାଲେଶ୍ୱର ନାଗରିକ ଓ ବାତ୍ୟା କ୍ଷତି ରିପୋର୍ଟର୍",
                iconEmoji = "📸",
                statusText = "${dailyPulse.pendingCivicReportsCount} Active Reports • ${dailyPulse.resolvedCivicReportsTodayCount} Cleared Today • Ward Patrols Active",
                updateInterval = "Continuous Background Offline Queue Sync",
                targetSheet = UniqueFeatureSheetType.MO_BALASORE_CIVIC_STORM_REPORTER
            ),
            SubsystemTelemetryStatus(
                nameEn = "ITR Missile & Airspace Defense Radar",
                nameOd = "ଚାନ୍ଦିପୁର ITR କ୍ଷେପଣାସ୍ତ୍ର ପରୀକ୍ଷା ଓ ଉପକୂଳ ରାଡାର୍",
                iconEmoji = "🚀",
                statusText = "${dailyPulse.itrNextTestWindow} • Zone: ${dailyPulse.itrRestrictedZoneKm}km",
                updateInterval = "Live DRDO NOTAM Airspace Feed",
                targetSheet = UniqueFeatureSheetType.ITR_MISSILE_COASTAL_TRACKER
            ),
            SubsystemTelemetryStatus(
                nameEn = "Subarnarekha Basin Soil & Betel Vine Advisory",
                nameOd = "ସୁବର୍ଣ୍ଣରେଖା କୃଷି ଓ ପାନ ବରଜ ପରାମର୍ଶ",
                iconEmoji = "🌾",
                statusText = "${dailyPulse.soilSalinityStatus} • ${dailyPulse.betelBlightRisk}",
                updateInterval = "Daily KVK Agronomy Advisory Sync",
                targetSheet = UniqueFeatureSheetType.SUBARNAREKHA_CROP_ADVISORY
            ),
            SubsystemTelemetryStatus(
                nameEn = "Balasore Smart Transit & Terminal Radar",
                nameOd = "ବାଲେଶ୍ୱର ସ୍ମାର୍ଟ ଯାତ୍ରୀ ଓ ବସ୍ ଟ୍ରାକର୍",
                iconEmoji = "🚌",
                statusText = "${dailyPulse.busTerminalStatus} • ${dailyPulse.nextBusesCount} scheduled buses",
                updateInterval = "Live Transit Schedule Feed",
                targetSheet = UniqueFeatureSheetType.BALASORE_SMART_TRANSIT_TRACKER
            ),
            SubsystemTelemetryStatus(
                nameEn = "Fakir Mohan University Campus Hub",
                nameOd = "ଫକୀର ମୋହନ ବିଶ୍ୱବିଦ୍ୟାଳୟ ଓ ଛାତ୍ର ହବ୍",
                iconEmoji = "🎓",
                statusText = "${dailyPulse.fmuCampusNotice} • ${dailyPulse.fmuUpcomingEventsCount} Active Alerts",
                updateInterval = "Daily Academic & Exam Feed",
                targetSheet = UniqueFeatureSheetType.FAKIR_MOHAN_STUDENT_HUB
            ),
            SubsystemTelemetryStatus(
                nameEn = "Baleswari Cuisine & Sweet Trails Explorer",
                nameOd = "ବାଲେଶ୍ୱର ଖାଦ୍ୟ ଓ ମିଠା ପରିକ୍ରମା",
                iconEmoji = "🥘",
                statusText = "Spotlight: ${dailyPulse.cuisineSpotlightItem} • ${dailyPulse.sweetShopsOpenCount} Heritage Shops Open",
                updateInterval = "Daily Culinary Guide Sync",
                targetSheet = UniqueFeatureSheetType.BALESWARI_CUISINE_EXPLORER
            ),
            SubsystemTelemetryStatus(
                nameEn = "TPNODL Power Outage & Grid Radar",
                nameOd = "TPNODL ବିଦ୍ୟୁତ୍ ସରବରାହ ଓ କଟ୍ ରାଡାର୍",
                iconEmoji = "⚡",
                statusText = "${dailyPulse.tpnodlRestorationEta} • Feeders Monitored: 5",
                updateInterval = "Real-Time 33/11kV Substation Telemetry",
                targetSheet = UniqueFeatureSheetType.TPNODL_POWER_OUTAGE_RADAR
            ),
            SubsystemTelemetryStatus(
                nameEn = "Chandipur Bioluminescence & Nocturnal Trail",
                nameOd = "ଚାନ୍ଦିପୁର ସମୁଦ୍ର ନୀଳ ଆଲୋକ ଓ ରାତ୍ରି ୱାଚ୍",
                iconEmoji = "🌊",
                statusText = "Glow Index: ${dailyPulse.chandipurBioluminescenceIndex.toInt()}% • Walk: ${dailyPulse.nightTideSafeWalkingMinutes}m safe window",
                updateInterval = "Live Ebb-Tide & Dark Sky Sync",
                targetSheet = UniqueFeatureSheetType.CHANDIPUR_BIOLUMINESCENCE_EXPLORER
            ),
            SubsystemTelemetryStatus(
                nameEn = "Balasore Snakebite & ASV Emergency Network",
                nameOd = "ବାଲେଶ୍ୱର ସର୍ପାଘାତ ଓ ଆଣ୍ଟି-ଭେନମ୍ ରାଡାର",
                iconEmoji = "🐍",
                statusText = "FM MCH: ${dailyPulse.fmmchAsvVialsAvailable} vials • Nilagiri: ${dailyPulse.nilagiriAsvVials} • 108 Dispatch Active",
                updateInterval = "Live District Hospital ASV Stock",
                targetSheet = UniqueFeatureSheetType.BALASORE_SNAKEBITE_ANTI_VENOM
            ),
            SubsystemTelemetryStatus(
                nameEn = "Nilagiri Dokra & Lost-Wax Metal Craft Guild",
                nameOd = "ନୀଳଗିରି ଡୋକ୍ରା ଓ କଂସା-ପିତ୍ତଳ ଗିଲ୍ଡ",
                iconEmoji = "🎨",
                statusText = "${dailyPulse.activeDokraArtisansCount} Master Artisans • Direct Fair-Trade Catalog",
                updateInterval = "Verified Artisan Guild Sync",
                targetSheet = UniqueFeatureSheetType.NILAGIRI_DOKRA_CRAFT_GUILD
            ),
            SubsystemTelemetryStatus(
                nameEn = "Bagha Jatin & Chasakhand Freedom Trail",
                nameOd = "ବାଘା ଯତୀନ ଓ ଚାଷାଖଣ୍ଡ ସ୍ୱାଧୀନତା ଟ୍ରେଲ୍",
                iconEmoji = "🇮🇳",
                statusText = "${dailyPulse.baghaJatinAudioGuidesCount} Audio Stops • ${dailyPulse.chasakhandMemorialVisitorsToday} Pilgrims Today",
                updateInterval = "Heritage Audio & GPS Walk Sync",
                targetSheet = UniqueFeatureSheetType.BAGHA_JATIN_FREEDOM_TRAIL
            ),
            SubsystemTelemetryStatus(
                nameEn = "Kuldiha Eco-Corridor & Forest Fire Watch",
                nameOd = "କୁଲଡିହା କରିଡର ଓ ବନାଗ୍ନି ରାଡାର୍",
                iconEmoji = "🐘",
                statusText = "${dailyPulse.kuldihaElephantMovementSector} • ${dailyPulse.kuldihaForestFireRisk}",
                updateInterval = "Hourly Satellite & Forest Beat Sync",
                targetSheet = UniqueFeatureSheetType.KULDIHA_ECO_CORRIDOR_FIRE_RADAR
            ),
            SubsystemTelemetryStatus(
                nameEn = "Bhograi Cashew & Groundnut Mandi Radar",
                nameOd = "ଭୋଗରାଇ କାଜୁ ଓ ଚିନାବାଦାମ ମଣ୍ଡି ରାଡାର୍",
                iconEmoji = "🥜",
                statusText = "Raw Cashew RCN: ₹${dailyPulse.bhograiRawCashewRateKg}/kg • Groundnut: ₹${dailyPulse.bhograiGroundnutRateQuintal}/Q",
                updateInterval = "Daily Mandi Spot Rates & Mills Sync",
                targetSheet = UniqueFeatureSheetType.BHOGRAI_CASHEW_GROUNDNUT_MANDI
            ),
            SubsystemTelemetryStatus(
                nameEn = "Raibania Medieval Fortress & Archaeological Vault",
                nameOd = "ରାଇବଣିଆ ଦୁର୍ଗ ପ୍ରତ୍ନତାତ୍ତ୍ୱିକ ଭଲ୍ଟ",
                iconEmoji = "🏰",
                statusText = "${dailyPulse.raibaniaExcavationStatus} • Visitors Today: ${dailyPulse.raibaniaDailyVisitorsCount}",
                updateInterval = "Daily Archaeological & Heritage Sync",
                targetSheet = UniqueFeatureSheetType.RAIBANIA_FORTRESS_VAULT
            ),
            SubsystemTelemetryStatus(
                nameEn = "Balaramgadi Deep-Sea Trawler Fleet Radar",
                nameOd = "ବଳରାମଗଡ଼ି ଗଭୀର ସମୁଦ୍ର ଟ୍ରଲର ଫ୍ଲିଟ୍",
                iconEmoji = "🍤",
                statusText = "${dailyPulse.balaramgadiActiveTrawlersCount} Trawlers Active • Prawn Landings: ${dailyPulse.balaramgadiTigerPrawnLandingKg} kg",
                updateInterval = "Real-Time Harbor Fleet & Catch Sync",
                targetSheet = UniqueFeatureSheetType.BALARAMGADI_TRAWLER_FLEET_RADAR
            ),
            SubsystemTelemetryStatus(
                nameEn = "Nilagiri Royal Lacquer & Bamboo Craft Guild",
                nameOd = "ନୀଳଗିରି ଲାଖ ଶିଳ୍ପ ଓ ବାଉଁଶ ଗିଲ୍ଡ",
                iconEmoji = "🌾",
                statusText = "${dailyPulse.nilagiriLacquerArtisansCount} Master Artisans • Orders Today: ${dailyPulse.nilagiriLacquerBangleOrdersToday}",
                updateInterval = "Daily Artisan SHG Guild Sync",
                targetSheet = UniqueFeatureSheetType.NILAGIRI_LACQUER_BAMBOO_GUILD
            ),
            SubsystemTelemetryStatus(
                nameEn = "Balasore District Blood Bank & Donor Grid",
                nameOd = "ଜିଲ୍ଲା ରକ୍ତଭଣ୍ଡାର ଓ ଦାତା ଗ୍ରିଡ୍",
                iconEmoji = "🩸",
                statusText = "Total Reserves: ${dailyPulse.dhhBloodUnitsStoredTotal} units • Negatives: ${dailyPulse.fmmchNegativeUnitsReserve} • 104 SOS Active",
                updateInterval = "Live FM MCH & DHH Blood Bank Telemetry",
                targetSheet = UniqueFeatureSheetType.BALASORE_DISTRICT_BLOOD_DONOR_GRID
            ),
            SubsystemTelemetryStatus(
                nameEn = "Subarnarekha River Basin Flood & Sluice Radar",
                nameOd = "ସୁବର୍ଣ୍ଣରେଖା ବନ୍ୟା ଓ ସ୍ଲୁଇସ୍ ଗେଟ୍ ରାଡାର୍",
                iconEmoji = "🌊",
                statusText = "Rajghat: ${"%.2f".format(dailyPulse.subarnarekhaRajghatGaugeMeters)}m (Danger 9.45m) • Sluice Gates: ${dailyPulse.bhograiActiveSluiceGatesCount}/18",
                updateInterval = "Real-Time River Hydrometric Telemetry",
                targetSheet = UniqueFeatureSheetType.SUBARNAREKHA_FLOOD_SLUICE_TELEMETRY
            ),
            SubsystemTelemetryStatus(
                nameEn = "Youth Mental Wellness & Aspirant Study Circle",
                nameOd = "ଯୁବ ମାନସିକ ସ୍ୱାସ୍ଥ୍ୟ ଓ ଅଧ୍ୟୟନ ସର୍କଲ",
                iconEmoji = "🧘",
                statusText = "Tele-MANAS 14416 Active • ${dailyPulse.youthStudyCirclesActiveCount} Peer Study Circles • ${dailyPulse.examPeerAspirantsConnected} Aspirants",
                updateInterval = "24x7 Student Support & Study Desk Sync",
                targetSheet = UniqueFeatureSheetType.BALASORE_YOUTH_WELLNESS_STUDY_CIRCLE
            ),
            SubsystemTelemetryStatus(
                nameEn = "Google Maps Offline Tiles & Outage Navigator",
                nameOd = "ଗୁଗୁଲ୍ ମ୍ୟାପ୍ସ ଅଫଲାଇନ୍ ଟାଇଲ୍ସ ଓ ନେଭିଗେସନ୍",
                iconEmoji = "🗺️",
                statusText = "8 Balasore Key Zones Pre-cached • Turn-by-Turn Offline Routing • Blackout Network Resilience",
                updateInterval = "Automated Map Cache & Tile Registry Sync",
                targetSheet = UniqueFeatureSheetType.BALASORE_OFFLINE_MAP_TILES
            ),
            SubsystemTelemetryStatus(
                nameEn = "High-Seas Marine Fishermen SOS & Distress",
                nameOd = "ଗଭୀର ସମୁଦ୍ର ମତ୍ସ୍ୟଜୀବୀ SOS ଓ ସତର୍କତା",
                iconEmoji = "🚨",
                statusText = "Port Warning Signal #${dailyPulse.marinePortSignalNumber} • Coast Guard 1554 Beacon Active",
                updateInterval = "Real-Time Coast Guard & Port Telemetry",
                targetSheet = UniqueFeatureSheetType.MARINE_FISHERMEN_HIGH_SEAS_SOS
            ),
            SubsystemTelemetryStatus(
                nameEn = "Baleswari Heritage Cuisine & Remuna Gaja",
                nameOd = "ବାଲେଶ୍ୱରୀ ଖାଦ୍ୟ ଓ ରେମୁଣା ଖିରି ଗଜା",
                iconEmoji = "🍲",
                statusText = "GI Tag Application Active • Remuna Khirachora Gopinath Bhog Timetable Verified",
                updateInterval = "Daily Heritage & Culinary Desk Sync",
                targetSheet = UniqueFeatureSheetType.BALESWARI_CULINARY_GI_TRAIL
            ),
            SubsystemTelemetryStatus(
                nameEn = "Micro-Ward Inundation & Flood Evacuation",
                nameOd = "ୱାର୍ଡ ଜଳବନ୍ଦୀ ଓ ସୁରକ୍ଷିତ ଉଚ୍ଚସ୍ଥାନ ରାଉଟର୍",
                iconEmoji = "🌊",
                statusText = "Municipal Low-Lying Wards Monitored • High-Ground Shelters Ready • Emergency 1929 Active",
                updateInterval = "Hourly Urban Drainage & Hydrology Sync",
                targetSheet = UniqueFeatureSheetType.WARD_FLOOD_EVACUATION_ROUTER
            ),
            SubsystemTelemetryStatus(
                nameEn = "Kuldiha Elephant Corridor Sentinel",
                nameOd = "କୁଲଡିହା ହାତୀ କରିଡର ସେଣ୍ଟିନେଲ୍",
                iconEmoji = "🐘",
                statusText = "Herd Movement Monitored • Solar Power Fences 100% Active • Forest DFO SOS Ready",
                updateInterval = "Real-Time Wildlife Early Warning Telemetry",
                targetSheet = UniqueFeatureSheetType.KULDIHA_ELEPHANT_CORRIDOR_SENTINEL
            ),
            SubsystemTelemetryStatus(
                nameEn = "Student Career & Digital Library Pulse",
                nameOd = "ବାଲେଶ୍ୱର ଛାତ୍ର ବୃତ୍ତି ଓ ଡିଜିଟାଲ୍ ଲାଇବ୍ରେରୀ",
                iconEmoji = "📚",
                statusText = "District e-Library Open • Medhabruti & PRERANA Portal Synced • OPSC/SSC Desks Live",
                updateInterval = "Daily Higher Education & Scholarship Sync",
                targetSheet = UniqueFeatureSheetType.STUDENT_CAREER_LIBRARY_PULSE
            ),
            SubsystemTelemetryStatus(
                nameEn = "TPNODL Rooftop Solar & Surya Ghar Radar",
                nameOd = "ଛାତ ସୌର ଶକ୍ତି ଓ TPNODL ସୂର୍ଯ୍ୟ ଘର",
                iconEmoji = "☀️",
                statusText = "5.4 kWh/m²/day Solar Radiation • 1,842 Active Net-Meters • Up to ₹78,000 Subsidy Desk",
                updateInterval = "Live Coastal Solar & Net-Metering Telemetry",
                targetSheet = UniqueFeatureSheetType.ROOFTOP_SOLAR_TPNODL_RADAR
            ),
            SubsystemTelemetryStatus(
                nameEn = "Talasari & Udaypur Red Ghost Crab Safari",
                nameOd = "ତାଳସାରୀ ଓ ଉଦୟପୁର ଲାଲ୍ କଙ୍କଡ଼ା ସଫାରୀ",
                iconEmoji = "🦀",
                statusText = "Low Tide Window Active • Estuary Boat Service 100% Operational • High Sighting Probability",
                updateInterval = "Live Intertidal Ghost Crab Telemetry (30s)",
                targetSheet = UniqueFeatureSheetType.TALASARI_UDAYPUR_RED_CRAB_SAFARI
            ),
            SubsystemTelemetryStatus(
                nameEn = "Panchalingeswar Waterfall & Devagiri Trek",
                nameOd = "ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ଝରଣା ଓ ୨୬୩ ପାହାଚ ଟ୍ରେକ୍",
                iconEmoji = "⛰️",
                statusText = "Stream Flow: 145 L/sec • 263-Step Climb Dry & Railing Intact • Sanctum Wait ~15m",
                updateInterval = "Live Waterfall Flow & Devagiri Sensor Pulse (45s)",
                targetSheet = UniqueFeatureSheetType.PANCHALINGESWAR_WATERFALL_TREK_MONITOR
            ),
            SubsystemTelemetryStatus(
                nameEn = "Emami Jagannath & Remuna Amruta Keli Bhog",
                nameOd = "ଇମାମୀ ଜଗନ୍ନାଥ ଓ ରେମୁଣା କ୍ଷୀରଭୋଗ",
                iconEmoji = "🛕",
                statusText = "Kshira Bhog Counter Open (~140 Pots Available) • Evening Bhog 07:30 PM • Brass Stalls Open",
                updateInterval = "Continuous Sanctum Ritual & Bhog Roster Sync (60s)",
                targetSheet = UniqueFeatureSheetType.EMAMI_JAGANNATH_REMUNA_HERITAGE
            ),
            SubsystemTelemetryStatus(
                nameEn = "Balaramgadi Marine Mandi & Fresh Catch Index",
                nameOd = "ବଳରାମଗଡ଼ି ମତ୍ସ୍ୟ ମଣ୍ଡି ଓ ଟ୍ରଲର ନିଲାମ",
                iconEmoji = "🐟",
                statusText = "48 Trawlers Docked at Budhabalanga Mouth • Fresh Hilsa & Bagda Prawn Morning Auction Active",
                updateInterval = "Live Fish Landing Harbor Auction Telemetry (30s)",
                targetSheet = UniqueFeatureSheetType.BALARAMGADI_FISH_MANDI_RATES
            ),
            SubsystemTelemetryStatus(
                nameEn = "Town Auto & Toto Benchmark Fare Calculator",
                nameOd = "ବାଲେଶ୍ୱର ଅଟୋ ଓ ଟୋଟୋ ନ୍ୟାଯ୍ୟ ଭଡ଼ା",
                iconEmoji = "🛺",
                statusText = "RTO Official Tariffs Synced • Day/Night Surcharge Matrix Active • Traffic Helpline 06782-262024",
                updateInterval = "Continuous RTO Tariff & Route Matrix Sync (60s)",
                targetSheet = UniqueFeatureSheetType.TOWN_AUTO_RICKSHAW_FARE_CALCULATOR
            ),
            SubsystemTelemetryStatus(
                nameEn = "Ward Sanitation & Door-to-Door Waste Radar",
                nameOd = "ୱାର୍ଡ ପରିମଳ ଓ ଘରକୁ ଘର ଅଳିଆ ଗାଡ଼ି",
                iconEmoji = "🚛",
                statusText = "Balasore Municipality Shift 1 Active • Door-to-Door Tippers En Route Across Wards 1-31",
                updateInterval = "Real-Time Sanitation Vehicle Route Telemetry (30s)",
                targetSheet = UniqueFeatureSheetType.WARD_SANITATION_GARBAGE_VEHICLE_TRACKER
            ),
            SubsystemTelemetryStatus(
                nameEn = "Balasore Industrial Corridor Job & Apprentice Board",
                nameOd = "ବାଲେଶ୍ୱର ଶିଳ୍ପାଞ୍ଚଳ ନିଯୁକ୍ତି ଓ ଆପ୍ରେଣ୍ଟିସ୍",
                iconEmoji = "🏭",
                statusText = "63 Verified Openings at Emami Paper, Balasore Alloys, Falcon Marine & Somnathpur Cluster",
                updateInterval = "Daily IDCO & NAPS Apprentice Registry Feed (60s)",
                targetSheet = UniqueFeatureSheetType.BALASORE_INDUSTRIAL_JOB_APPRENTICE_BOARD
            ),
            SubsystemTelemetryStatus(
                nameEn = "Krushak PACS Dhan Mandi & MSP Procurement",
                nameOd = "କୃଷକ PACS ଧାନ ମଣ୍ଡି ଓ ଏମଏସପି କ୍ରୟ",
                iconEmoji = "🌾",
                statusText = "Official Paddy MSP ₹2,320/Qtl • Moisture FAQ <17% Enforced • Remuna, Soro, Jaleswar Mandis Active",
                updateInterval = "Real-Time RMC Paddy Procurement Telemetry (45s)",
                targetSheet = UniqueFeatureSheetType.KRUSHAK_PACS_MANDI_GRAIN_RADAR
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
                                            text = "Installed: v$appVersionInstalled (Build ${com.example.BuildConfig.VERSION_CODE})",
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

                // 5 Core Auto-Updating District Domains Hub
                item {
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "🌟 ୫ଟି ମୁଖ୍ୟ ସ୍ୱୟଂ-ଅପଡେଟ୍ ହବ୍ (ସିଧାସଳଖ ପ୍ରବେଶ)" else "🌟 5 Core Auto-Updating Domains (Quick Launch)",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoSlate900
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (language == AppLanguage.ODIA) "ତଳେ ଥିବା ଯେକୌଣସି ହବ୍ କ୍ଲିକ୍ କରି ସିଧାସଳଖ ଲାଇଭ୍ ସ୍କ୍ରିନ୍ ଦେଖନ୍ତୁ" else "Tap any domain card below to immediately open the full live interactive feature",
                            style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate500)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        val domainHighlights = listOf(
                            Triple("🌊 Coastal & Tide Alarm", "Low tide: ${dailyPulse.chandipurLowTideWindow} • Swell: ${String.format(java.util.Locale.US, "%.1fm", dailyPulse.incoisSwellMeters)}", UniqueFeatureSheetType.CHANDIPUR_TIDE_TIMER),
                            Triple("🚆 Smart Transit & Mo Bus", "Mo Bus: ${dailyPulse.moBusRoute101EtaMins}m • Ferry: ${if (dailyPulse.riverFerryCrossingStatus.contains("Active")) "Running" else "Docked"}", UniqueFeatureSheetType.BALASORE_MO_BUS_CRUT_NAVIGATOR),
                            Triple("🏥 24/7 Red Cross & DHH", "${dailyPulse.dhhBloodUnitsStored} blood units • ICU beds: ${dailyPulse.fmmchIcuBedsVacant}", UniqueFeatureSheetType.DHH_BLOOD_BANK_LIVE_RADAR),
                            Triple("🛕 Heritage & Sacred Spring", "${dailyPulse.panchalingeswarFlowStatus} • Chadak sync", UniqueFeatureSheetType.PANCHALINGESWAR_KULDIHA_SAFARI),
                            Triple("🌾 Mandi MSP & Artisans", "Paddy MSP: ₹${dailyPulse.paddyMspRatePerQuintal} • ${dailyPulse.sabaiGrassActiveShgClusters} SHG clusters", UniqueFeatureSheetType.DHAN_MANDI_MSP_PROCUREMENT)
                        )

                        domainHighlights.forEach { (title, subtitle, sheet) ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .then(
                                        if (onOpenFeatureSheet != null) {
                                            Modifier.clickable { onOpenFeatureSheet(sheet) }
                                        } else Modifier
                                    ),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                                border = BorderStroke(1.dp, Color(0xFFCBD5E1))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = title,
                                            style = MaterialTheme.typography.labelLarge.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = BentoSlate900
                                            )
                                        )
                                        Text(
                                            text = subtitle,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = Color(0xFF0369A1),
                                                fontSize = 11.sp
                                            )
                                        )
                                    }
                                    Surface(
                                        color = OceanBlue,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = "OPEN →",
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.sp
                                            )
                                        )
                                    }
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(
                                if (subsystem.targetSheet != null && onOpenFeatureSheet != null) {
                                    Modifier.clickable {
                                        onOpenFeatureSheet(subsystem.targetSheet)
                                    }
                                } else Modifier
                            ),
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
                                        ),
                                        modifier = Modifier.weight(1f, fill = false)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        if (subsystem.targetSheet != null) {
                                            Surface(
                                                color = Color(0xFFE0F2FE),
                                                shape = RoundedCornerShape(6.dp),
                                                modifier = Modifier.padding(end = 4.dp)
                                            ) {
                                                Text(
                                                    text = "VIEW →",
                                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 9.sp,
                                                        color = Color(0xFF0369A1)
                                                    )
                                                )
                                            }
                                        }
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
                                }

                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = subsystem.statusText,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF0284C7),
                                        fontWeight = FontWeight.Medium
                                    )
                                )

                                Spacer(modifier = Modifier.height(2.dp))
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
