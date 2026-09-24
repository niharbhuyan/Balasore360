package com.example.ui.features.civic

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

data class FeederMaintenanceNotice(
    val substationName: String,
    val substationNameOd: String,
    val coveredLocalities: String,
    val shutdownTiming: String,
    val maintenanceStatus: String,
    val isPowerActive: Boolean
)

data class WatcoWardSchedule(
    val wardZone: String,
    val wardZoneOd: String,
    val morningSupply: String,
    val eveningSupply: String,
    val waterQualityTds: String,
    val tankerHelpline: String
)

val TPNODL_FEEDERS = listOf(
    FeederMaintenanceNotice(
        substationName = "33/11kV Town Substation (Cinema Bazar & Motiganj)",
        substationNameOd = "୩୩/୧୧ କେଭି ଟାଉନ୍ ସବ୍-ଷ୍ଟେସନ୍ (ସିନେମା ବଜାର ଓ ମୋତିଗଞ୍ଜ)",
        coveredLocalities = "Motiganj, Gopalgaon, Station Road, Proof Road",
        shutdownTiming = "No shutdown scheduled today (Normal Grid)",
        maintenanceStatus = "All 6 outgoing 11kV feeders operational",
        isPowerActive = true
    ),
    FeederMaintenanceNotice(
        substationName = "33/11kV Remuna Feeder (Industrial & Temple Belt)",
        substationNameOd = "୩୩/୧୧ କେଭି ରେମୁଣା ଫିଡର (ଶିଳ୍ପାଞ୍ଚଳ ଓ ମନ୍ଦିର ରୁଟ୍)",
        coveredLocalities = "Remuna Golai, Khirachora Temple Area, Balgopalpur MSME",
        shutdownTiming = "Routine pole maintenance completed yesterday",
        maintenanceStatus = "Grid frequency 50.02 Hz (Stable)",
        isPowerActive = true
    ),
    FeederMaintenanceNotice(
        substationName = "33/11kV Jaleswar - Laxmannath Substation",
        substationNameOd = "୩୩/୧୧ କେଭି ଜଳେଶ୍ୱର - ଲକ୍ଷ୍ମଣନାଥ ସବ୍-ଷ୍ଟେସନ୍",
        coveredLocalities = "Jaleswar Municipality, Toll Gate, Station Chhak",
        shutdownTiming = "Continuous 24x7 supply active",
        maintenanceStatus = "Transformer oil insulation tested OK",
        isPowerActive = true
    )
)

val WATCO_SCHEDULES = listOf(
    WatcoWardSchedule(
        wardZone = "Balasore Municipality Ward 1 to 10 (Town North)",
        wardZoneOd = "ବାଲେଶ୍ୱର ପୌରପାଳିକା ୱାର୍ଡ ୧ ରୁ ୧୦ (ଉତ୍ତର ସହର)",
        morningSupply = "05:30 AM - 08:30 AM",
        eveningSupply = "05:00 PM - 07:30 PM",
        waterQualityTds = "142 ppm (Drink from Tap Standard)",
        tankerHelpline = "06782262234"
    ),
    WatcoWardSchedule(
        wardZone = "Ward 11 to 20 & Mallikashpur / FM College Belt",
        wardZoneOd = "ୱାର୍ଡ ୧୧ ରୁ ୨୦ ଓ ମଲ୍ଲିକାଶପୁର / ଏଫ୍.ଏମ୍. କଲେଜ ଅଞ୍ଚଳ",
        morningSupply = "06:00 AM - 09:00 AM",
        eveningSupply = "05:30 PM - 08:00 PM",
        waterQualityTds = "138 ppm (Chlorine residual optimal)",
        tankerHelpline = "06782262234"
    ),
    WatcoWardSchedule(
        wardZone = "Ward 21 to 31 & Sahadevkhunta Bus Stand Area",
        wardZoneOd = "ୱାର୍ଡ ୨୧ ରୁ ୩୧ ଓ ସହଦେବଖୁଣ୍ଟା ବସ୍ ଟର୍ମିନାଲ୍ ଅଞ୍ଚଳ",
        morningSupply = "05:45 AM - 08:45 AM",
        eveningSupply = "05:15 PM - 07:45 PM",
        waterQualityTds = "145 ppm (Purified surface supply)",
        tankerHelpline = "06782262234"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TpnodlWatcoUtilityHotlineSheet(
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }
    var syncTime by remember { mutableStateOf("Just now") }

    LaunchedEffect(Unit) {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        syncTime = sdf.format(Date())
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
                .testTag("tpnodl_watco_utility_sheet")
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
                        Text("⚡", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "TPNODL ବିଦ୍ୟୁତ୍ ଓ WATCO ଜଳଯୋଗାଣ ରାଡାର" else "TPNODL Power & WATCO Water Radar",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ସବ୍-ଷ୍ଟେସନ୍ ସ୍ଥିତି • ପାନୀୟ ଜଳ ସମୟ • ଫିଉଜ୍-ଅଫ୍ ୧୯୧୨" else "Substation Status • Piped Tap Water • Fuse-Off 1912",
                            fontSize = 12.sp,
                            color = Color(0xFFB45309)
                        )
                    }
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Sync pill
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFFFFBEB),
                border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
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
                            text = "TPNODL & WATCO Grid Live Telemetry ($syncTime)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF92400E)
                        )
                    }
                    Text(
                        text = "GRID HEALTHY",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF15803D)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFFF8FAFC),
                contentColor = Color(0xFFB45309),
                indicator = {},
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବିଦ୍ୟୁତ୍ ଫିଡର" else "Power Feeders",
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
                            text = if (language == AppLanguage.ODIA) "ୱାଟକୋ ପାନୀୟ ଜଳ" else "WATCO Water",
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
                            text = if (language == AppLanguage.ODIA) "ହେଲ୍ପଲାଇନ ୧୯୧୨" else "Hotlines",
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
                        items(TPNODL_FEEDERS) { feeder ->
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
                                                text = if (language == AppLanguage.ODIA) feeder.substationNameOd else feeder.substationName,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "📍 Localities: ${feeder.coveredLocalities}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF64748B)
                                            )
                                        }
                                        Text(
                                            text = if (feeder.isPowerActive) "ACTIVE" else "SHUTDOWN",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (feeder.isPowerActive) Color(0xFF16A34A) else Color(0xFFDC2626),
                                            modifier = Modifier
                                                .background(
                                                    if (feeder.isPowerActive) Color(0xFFDCFCE7) else Color(0xFFFEE2E2),
                                                    RoundedCornerShape(6.dp)
                                                )
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "⚡ ${feeder.maintenanceStatus}",
                                        fontSize = 12.sp,
                                        color = Color(0xFF166534),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "🕒 ${feeder.shutdownTiming}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF334155),
                                        modifier = Modifier.padding(top = 2.dp)
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
                        items(WATCO_SCHEDULES) { watco ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = if (language == AppLanguage.ODIA) watco.wardZoneOd else watco.wardZone,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "🌅 Morning: ${watco.morningSupply}",
                                            fontSize = 11.sp,
                                            color = Color(0xFF0284C7),
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = "🌆 Evening: ${watco.eveningSupply}",
                                            fontSize = 11.sp,
                                            color = Color(0xFF0F766E),
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "💧 Quality: ${watco.waterQualityTds}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF334155)
                                    )
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
                            color = Color(0xFFFEF2F2),
                            border = BorderStroke(1.dp, Color(0xFFFCA5A5)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "୨୪/୭ ବିଦ୍ୟୁତ୍ ଓ ଜରୁରୀ ପାନୀୟ ଜଳ ଟ୍ୟାଙ୍କର ସେବା" else "24x7 Power Fuse-off & Emergency Water Tankers",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF991B1B)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("• TPNODL 24x7 Toll-Free Helpline: Dial 1912 for power outage, spark, or transformer fuse-call.", fontSize = 12.sp, color = Color(0xFF7F1D1D))
                                Text("• WATCO Balasore Emergency Tanker: For marriage / community functions or pipeline repair.", fontSize = 12.sp, color = Color(0xFF7F1D1D))
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1912"))
                                context.startActivity(intent)
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Call TPNODL Power Helpline 1912", fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:06782262234"))
                                context.startActivity(intent)
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.WaterDrop, contentDescription = "Call", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Call WATCO Municipal Water Desk (06782-262234)", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
