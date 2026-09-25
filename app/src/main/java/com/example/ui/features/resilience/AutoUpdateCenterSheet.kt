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
                nameEn = "Subarnarekha & Budhabalanga River Levels",
                nameOd = "ସୁବର୍ଣ୍ଣରେଖା ଓ ବୁଢ଼ାବଳଙ୍ଗ ନଦୀ ଜଳସ୍ତର",
                iconEmoji = "🌊",
                statusText = dailyPulse.subarnarekhaBasinRisk,
                updateInterval = "Live CWC / Irrigation Gauge Sync",
                targetSheet = UniqueFeatureSheetType.SUBARNAREKHA_SLUICE_FLOOD_RADAR
            ),
            SubsystemTelemetryStatus(
                nameEn = "Chandipur Vanishing Sea & Tide Reversal Alarm",
                nameOd = "ଚାନ୍ଦିପୁର ସମୁଦ୍ର ଭଟ୍ଟା ସମୟ ନିୟନ୍ତ୍ରକ ଓ ଆଲାର୍ମ",
                iconEmoji = "🏖️",
                statusText = "${dailyPulse.chandipurLowTideWindow} • ${if (dailyPulse.isChandipurWalkSafeNow) "Safe Intertidal Walking 🟢" else "⚠️ Tide Returning (High Tide ${dailyPulse.chandipurNextHighTide})"}",
                updateInterval = "Continuous Lunar Algorithmic Sync",
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
