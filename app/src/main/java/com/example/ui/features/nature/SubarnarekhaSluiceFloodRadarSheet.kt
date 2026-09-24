package com.example.ui.features.nature

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

enum class FloodGateStatus(val labelEn: String, val labelOd: String, val color: Color) {
    NORMAL("NORMAL DISCHARGE", "ସ୍ୱାଭାବିକ ପ୍ରବାହ", Color(0xFF166534)),
    CAUTION("DISCHARGE ELEVATED", "ପ୍ରବାହ ବୃଦ୍ଧି ସତର୍କତା", Color(0xFFB45309)),
    DANGER("WARNING / HIGH TIDE BACKFLOW", "ବନ୍ୟା ବିପଦ ସତର୍କତା", Color(0xFF991B1B))
}

data class SluiceGaugePoint(
    val stationName: String,
    val stationNameOd: String,
    val riverSystem: String,
    val currentLevel: String,
    val dangerLevel: String,
    val gatePosition: String,
    val dischargeCusecs: String,
    val status: FloodGateStatus,
    val salinityPpt: String
)

data class RiverIrrigationControl(
    val divisionName: String,
    val divisionNameOd: String,
    val location: String,
    val controlRoomPhone: String,
    val commandJurisdiction: String
)

val SLUICE_GAUGE_POINTS = listOf(
    SluiceGaugePoint(
        stationName = "Rajghat Gauge Station (Subarnarekha)",
        stationNameOd = "ରାଜଘାଟ ଗେଜ୍ ଷ୍ଟେସନ୍ (ସୁବର୍ଣ୍ଣରେଖା)",
        riverSystem = "Subarnarekha River Basin",
        currentLevel = "8.45 meters",
        dangerLevel = "10.36 meters (Warning: 9.45m)",
        gatePosition = "8 / 16 Sluice Gates Regulated",
        dischargeCusecs = "24,800 Cusecs Outflow",
        status = FloodGateStatus.NORMAL,
        salinityPpt = "0.4 ppt (Freshwater - Safe for Irrigation)"
    ),
    SluiceGaugePoint(
        stationName = "Jamsholaghat Border Gauge (Jharkhand Entry)",
        stationNameOd = "ଜାମଶୋଳାଘାଟ ସୀମାନ୍ତ ଗେଜ୍",
        riverSystem = "Subarnarekha Upstream Transboundary",
        currentLevel = "46.10 meters",
        dangerLevel = "49.15 meters",
        gatePosition = "Galudih Barrage 4 Gates Open in Jharkhand",
        dischargeCusecs = "32,500 Cusecs Inflow",
        status = FloodGateStatus.CAUTION,
        salinityPpt = "0.2 ppt (Low mineral inflow)"
    ),
    SluiceGaugePoint(
        stationName = "Balaramgadi Estuary Tidal Sluice (Budhabalanga)",
        stationNameOd = "ବଳରାମଗଡ଼ି ମୁହାଣ ଟାଇଡାଲ ସ୍ଲୁଇସ୍ (ବୁଢ଼ାବଳଙ୍ଗ)",
        riverSystem = "Budhabalanga Estuary & Marine Confluence",
        currentLevel = "3.12 meters",
        dangerLevel = "4.20 meters",
        gatePosition = "High Tide Flap-Gates Closed (Anti-Backflow)",
        dischargeCusecs = "14,200 Cusecs Outflow at Ebb",
        status = FloodGateStatus.NORMAL,
        salinityPpt = "18.5 ppt (Brackish Estuary Confluence)"
    ),
    SluiceGaugePoint(
        stationName = "Bhograi-Baliapal Drainage Outfall Sluices",
        stationNameOd = "ଭୋଗରାଇ-ବାଲିଆପାଳ ନିଷ୍କାସନ ସ୍ଲୁଇସ୍",
        riverSystem = "Coastal Drainage Channels",
        currentLevel = "2.10 meters",
        dangerLevel = "3.50 meters",
        gatePosition = "Automatic One-Way Flap Gates Operating",
        dischargeCusecs = "Gravity Tidal Outfall Normal",
        status = FloodGateStatus.NORMAL,
        salinityPpt = "1.8 ppt (Paddy Protection Dykes Intact)"
    )
)

val RIVER_IRRIGATION_CONTROLS = listOf(
    RiverIrrigationControl(
        divisionName = "Subarnarekha Irrigation Project (SIP) Control Room",
        divisionNameOd = "ସୁବର୍ଣ୍ଣରେଖା ଜଳସେଚନ ପ୍ରକଳ୍ପ ନିୟନ୍ତ୍ରଣ କକ୍ଷ",
        location = "Laxmannath Road, Jaleswar",
        controlRoomPhone = "06781-222314",
        commandJurisdiction = "Jaleswar, Bhograi, Basta barrages & canal network"
    ),
    RiverIrrigationControl(
        divisionName = "Balasore Drainage & Flood Control Division",
        divisionNameOd = "ବାଲେଶ୍ୱର ଜଳ ନିଷ୍କାସନ ଓ ବନ୍ୟା ନିୟନ୍ତ୍ରଣ ବିଭାଗ",
        location = "Irrigation Colony, Balasore Town",
        controlRoomPhone = "06782-262145",
        commandJurisdiction = "Budhabalanga, Jalaka & Kansbans river embankments"
    ),
    RiverIrrigationControl(
        divisionName = "Central Water Commission (CWC) Balasore Telemetry",
        divisionNameOd = "କେନ୍ଦ୍ରୀୟ ଜଳ ଆୟୋଗ ଟେଲିମେଟ୍ରି ସେଲ୍",
        location = "Near Court Square, Balasore",
        controlRoomPhone = "06782-262290",
        commandJurisdiction = "Transboundary flood forecast modeling & hydrometeorology"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubarnarekhaSluiceFloodRadarSheet(
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }
    var lastSyncTimestamp by remember { mutableStateOf("Just now") }

    LaunchedEffect(Unit) {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        lastSyncTimestamp = sdf.format(Date())
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
                .testTag("subarnarekha_sluice_flood_radar_sheet")
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
                            .background(Color(0xFFE0F2FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🌊", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ସୁବର୍ଣ୍ଣରେଖା ଓ ବୁଢ଼ାବଳଙ୍ଗ ନଦୀ ସ୍ଲୁଇସ୍ ରାଡାର" else "River Sluice Gates & Estuary Radar",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଗାଲୁଡିହ ବ୍ୟାରେଜ୍ ପ୍ରବାହ • ରାଜଘାଟ ଗେଜ୍ • ଲୁଣା ଜଳ ଅନୁପ୍ରବେଶ" else "Galudih Inflow • Rajghat Gauge • Salinity Intrusion",
                            fontSize = 12.sp,
                            color = Color(0xFF0284C7)
                        )
                    }
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Auto-update Telemetry Banner
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF0FDF4),
                border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
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
                            text = "Telemetry live synced at $lastSyncTimestamp",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF166534)
                        )
                    }
                    Text(
                        text = "RIVERS BELOW DANGER",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF166534)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFFF8FAFC),
                contentColor = Color(0xFF0284C7),
                indicator = {},
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଗେଜ୍ ଓ ସ୍ଲୁଇସ୍ ଗେଟ୍" else "Gauges & Sluices",
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
                            text = if (language == AppLanguage.ODIA) "ଲୁଣା ଜଳ ସତର୍କତା" else "Salinity Index",
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
                            text = if (language == AppLanguage.ODIA) "କଣ୍ଟ୍ରୋଲ୍ ରୁମ୍" else "SIP Control",
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
                        items(SLUICE_GAUGE_POINTS) { gauge ->
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
                                                text = if (language == AppLanguage.ODIA) gauge.stationNameOd else gauge.stationName,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "Basin: ${gauge.riverSystem}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF64748B)
                                            )
                                        }
                                        Text(
                                            text = if (language == AppLanguage.ODIA) gauge.status.labelOd else gauge.status.labelEn,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = gauge.status.color,
                                            modifier = Modifier
                                                .background(gauge.status.color.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text("Current Level", fontSize = 10.sp, color = Color(0xFF64748B))
                                            Text(gauge.currentLevel, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0369A1))
                                        }
                                        Column {
                                            Text("Danger Level", fontSize = 10.sp, color = Color(0xFF64748B))
                                            Text(gauge.dangerLevel, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF991B1B))
                                        }
                                        Column {
                                            Text("Discharge / Inflow", fontSize = 10.sp, color = Color(0xFF64748B))
                                            Text(gauge.dischargeCusecs, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFFF1F5F9),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "⚙️ ${gauge.gatePosition} • Salinity: ${gauge.salinityPpt}",
                                            fontSize = 11.sp,
                                            color = Color(0xFF334155),
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.82f)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFEFF6FF),
                            border = BorderStroke(1.dp, Color(0xFFBFDBFE)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଲୁଣା ଜଳ ଅନୁପ୍ରବେଶ ଓ ଚାଷୀ ସୁରକ୍ଷା ମାର୍ଗଦର୍ଶିକା" else "Estuary Salinity Intrusion Farmer Advisory",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E40AF)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "During Full Moon (Purnima) and New Moon (Amavasya) spring tides, tidal seawater pushes up to 22 km inland through the Budhabalanga and Subarnarekha river mouths. The automated saline embankments and backflow flaps in Bhograi, Baliapal, and Remuna protect over 45,000 hectares of high-yield Swarna and Pooja paddy varieties.",
                                    fontSize = 12.sp,
                                    color = Color(0xFF1E3A8A),
                                    lineHeight = 17.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🟢", fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Safe threshold (< 1.5 ppt): Full irrigation canal supply permitted.", fontSize = 11.sp, color = Color(0xFF166534))
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🔴", fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Saline ingress (> 4.0 ppt): Flap gates close automatically; switch to tube-wells.", fontSize = 11.sp, color = Color(0xFF991B1B))
                                }
                            }
                        }
                    }
                }
                2 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxHeight(0.82f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(RIVER_IRRIGATION_CONTROLS) { item ->
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
                                                text = if (language == AppLanguage.ODIA) item.divisionNameOd else item.divisionName,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "📍 ${item.location}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF475569)
                                            )
                                            Text(
                                                text = "🌊 Jurisdiction: ${item.commandJurisdiction}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF0369A1),
                                                modifier = Modifier.padding(top = 2.dp)
                                            )
                                        }
                                        Button(
                                            onClick = {
                                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${item.controlRoomPhone}"))
                                                context.startActivity(intent)
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
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
            }
        }
    }
}
