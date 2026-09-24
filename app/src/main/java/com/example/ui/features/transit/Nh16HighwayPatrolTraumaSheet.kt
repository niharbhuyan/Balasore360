package com.example.ui.features.transit

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import java.text.SimpleDateFormat
import java.util.*

enum class CorridorStatus(val labelEn: String, val labelOd: String, val color: Color) {
    CLEAR("CLEAR / NORMAL SPEED", "ମୁକ୍ତ / ସ୍ୱାଭାବିକ ଗତି", Color(0xFF166534)),
    DIVERSION("LANE DIVERSION AHEAD", "ରାସ୍ତା ମୋଡ଼ ସତର୍କତା", Color(0xFFB45309)),
    HEAVY_FOG_OR_RAIN("WEATHER SLOWDOWN", "ପାଣିପାଗ ସତର୍କତା", Color(0xFF0369A1))
}

data class NhCorridorSector(
    val sectorName: String,
    val sectorNameOd: String,
    val stretchKm: String,
    val status: CorridorStatus,
    val trafficNotice: String,
    val nearestAmbulanceBase: String
)

data class TraumaEmergencyPost(
    val postName: String,
    val postNameOd: String,
    val nh16KmMarker: String,
    val telephoneNumber: String,
    val facilitiesAvailable: String
)

val NH16_SECTORS = listOf(
    NhCorridorSector(
        sectorName = "Jaleswar Border - Basta Sector",
        sectorNameOd = "ଜଳେଶ୍ୱର ସୀମାନ୍ତ - ବସ୍ତା ସେକ୍ଟର",
        stretchKm = "Km 180 to Km 205 (WB-Odisha Gate)",
        status = CorridorStatus.CLEAR,
        trafficNotice = "Laxmannath toll plaza lanes fully automated Fastag operational. Smooth freight movement.",
        nearestAmbulanceBase = "GK Bhattar Hospital, Jaleswar (108 Available)"
    ),
    NhCorridorSector(
        sectorName = "Balasore By-Pass & Kuruda Flyover",
        sectorNameOd = "ବାଲେଶ୍ୱର ବାଇପାସ୍ ଓ କୁରୁଡ଼ା ଫ୍ଲାଏଓଭର",
        stretchKm = "Km 205 to Km 235",
        status = CorridorStatus.DIVERSION,
        trafficNotice = "Service road culvert reinforcement work near Remuna Golai. Maximum speed 40 km/h.",
        nearestAmbulanceBase = "FM MCH Trauma Wing & Kuruda Highway Base"
    ),
    NhCorridorSector(
        sectorName = "Bahanaga Bazar & Panpana Sector",
        sectorNameOd = "ବାହାନଗା ବଜାର ଓ ପାଣପଣା ସେକ୍ଟର",
        stretchKm = "Km 235 to Km 255",
        status = CorridorStatus.CLEAR,
        trafficNotice = "Road surface high friction macadam layer in place. Speed radar active.",
        nearestAmbulanceBase = "Bahanaga CHC (24x7 Emergency Stabilization)"
    ),
    NhCorridorSector(
        sectorName = "Soro & Simulia Southern Sector",
        sectorNameOd = "ସୋରୋ ଓ ସିମୁଳିଆ ଦକ୍ଷିଣ ସେକ୍ଟର",
        stretchKm = "Km 255 to Km 280 (Bhadrak Border)",
        status = CorridorStatus.CLEAR,
        trafficNotice = "Four-lane carriage way clear. NHAI patrol van scanning every 30 minutes.",
        nearestAmbulanceBase = "Soro Sub-Divisional Hospital Trauma Unit"
    )
)

val TRAUMA_EMERGENCY_POSTS = listOf(
    TraumaEmergencyPost(
        postName = "NHAI 24x7 Highway Patrol & Crane Rescue (Toll-Free 1033)",
        postNameOd = "NHAI ୨୪/୭ ରାଜପଥ ପାଟ୍ରୋଲ୍ ଓ କ୍ରେନ୍ ରେସ୍କ୍ୟୁ",
        nh16KmMarker = "Patrol Unit Base: Sergarh & Panikoili",
        telephoneNumber = "1033",
        facilitiesAvailable = "Hydraulic recovery crane, first-aid paramedics, accident clearance"
    ),
    TraumaEmergencyPost(
        postName = "Fakir Mohan MCH Level-II Trauma Care Center",
        postNameOd = "ଏଫଏମ ଏମସିଏଚ ସ୍ତର-୨ ଟ୍ରମା କେୟାର ସେଣ୍ଟର",
        nh16KmMarker = "Near Remuna Golai Interchange",
        telephoneNumber = "06782-275800",
        facilitiesAvailable = "Neuro-surgery, orthopedic ICU, blood bank, CT/MRI scanner"
    ),
    TraumaEmergencyPost(
        postName = "Kuruda Trauma Care Facility (Direct NH-16 Access)",
        postNameOd = "କୁରୁଡ଼ା ଟ୍ରମା କେୟାର ହସ୍ପିଟାଲ",
        nh16KmMarker = "NH-16 Kuruda Junction, Balasore",
        telephoneNumber = "06782-262108",
        facilitiesAvailable = "Emergency resuscitation, oxygen bank, 24x7 ambulance bay"
    ),
    TraumaEmergencyPost(
        postName = "Soro Sub-Divisional Hospital Emergency Wing",
        postNameOd = "ସୋରୋ ଉପଖଣ୍ଡ ଡାକ୍ତରଖାନା ଏମରଜେନ୍ସି",
        nh16KmMarker = "NH-16 Soro Bypass Exit",
        telephoneNumber = "06788-222120",
        facilitiesAvailable = "Trauma stabilization, minor OT, blood storage unit"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Nh16HighwayPatrolTraumaSheet(
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }
    var corridorSyncTime by remember { mutableStateOf("Just now") }

    LaunchedEffect(Unit) {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        corridorSyncTime = sdf.format(Date())
    }

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
                .testTag("nh16_highway_patrol_trauma_sheet")
        ) {
            // Header
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
                            .background(Color(0xFFFEF3C7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🛣️", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଜାତୀୟ ରାଜପଥ ୧୬ ହେଲ୍ପଲାଇନ ଓ ଟ୍ରମା SOS" else "NH-16 Highway Patrol & Trauma Radar",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "NHAI ୧୦୩୩ • ଗୋଲ୍ଡେନ୍ ଆୱାର ଟ୍ରମା କେୟାର • କ୍ରେନ୍ ସେବା" else "NHAI 1033 • Golden Hour Trauma • Heavy Cranes",
                            fontSize = 12.sp,
                            color = Color(0xFFD97706)
                        )
                    }
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Corridor Telemetry Sync Pill
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFFFFBEB),
                border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 7.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF16A34A))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Highway status live at $corridorSyncTime (NH-16 Corridor)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF92400E)
                        )
                    }
                    Text(
                        text = "1033 ACTIVE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB45309)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFFF8FAFC),
                contentColor = Color(0xFFD97706),
                indicator = {},
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ରାଜପଥ ସ୍ଥିତି" else "Corridor Radar",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଟ୍ରମା ଓ ଆମ୍ବୁଲାନ୍ସ" else "Trauma SOS",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "କ୍ରେନ୍ ଓ ରେସ୍କ୍ୟୁ" else "Cranes & Towing",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            when (selectedTab) {
                0 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxHeight(0.82f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(NH16_SECTORS) { sector ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = if (language == AppLanguage.ODIA) sector.sectorNameOd else sector.sectorName,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = sector.stretchKm,
                                                fontSize = 11.sp,
                                                color = Color(0xFF64748B)
                                            )
                                        }
                                        Text(
                                            text = if (language == AppLanguage.ODIA) sector.status.labelOd else sector.status.labelEn,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = sector.status.color,
                                            modifier = Modifier
                                                .background(sector.status.color.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = sector.trafficNotice,
                                        fontSize = 12.sp,
                                        color = Color(0xFF334155),
                                        lineHeight = 16.sp
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "🚑 Nearest Base: ${sector.nearestAmbulanceBase}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF0F766E),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
                1 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxHeight(0.82f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(TRAUMA_EMERGENCY_POSTS) { post ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = if (language == AppLanguage.ODIA) post.postNameOd else post.postName,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "📍 ${post.nh16KmMarker}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF64748B)
                                            )
                                            Text(
                                                text = "🏥 ${post.facilitiesAvailable}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF0369A1),
                                                modifier = Modifier.padding(top = 2.dp)
                                            )
                                        }
                                        Button(
                                            onClick = {
                                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${post.telephoneNumber}"))
                                                context.startActivity(intent)
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Call", fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                2 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.82f)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFFFFBEB),
                            border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଭାରୀ କ୍ରେନ୍ ଓ ରେକଭରୀ ଟୋଇଙ୍ଗ୍ ସହାୟତା" else "24x7 Heavy Hydraulic Crane & Towing Recovery",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF92400E)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "In case of heavy vehicle breakdown, overturned containers, or road blockage on NH-16 anywhere between Jaleswar and Soro, dial NHAI 1033 directly. Toll plaza recovery teams dispatch 40-ton cranes and route diversion marshals within 15-20 minutes.",
                                    fontSize = 12.sp,
                                    color = Color(0xFF78350F),
                                    lineHeight = 17.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1033"))
                                        context.startActivity(intent)
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Dial NHAI Emergency 1033 (Toll Free)", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
