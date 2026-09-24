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

data class MoBusRouteInfo(
    val routeNumber: String,
    val routeTitle: String,
    val routeTitleOd: String,
    val keyStops: String,
    val frequencyMins: String,
    val fareRange: String
)

data class InterDistrictBusService(
    val destination: String,
    val busType: String,
    val terminalBay: String,
    val departureSchedule: String,
    val estimatedFare: String
)

val MO_BUS_ROUTES = listOf(
    MoBusRouteInfo(
        routeNumber = "Route 71",
        routeTitle = "Sahadevkhunta Bus Stand ⇄ Chandipur Beach",
        routeTitleOd = "ସହଦେବଖୁଣ୍ଟା ⇄ ଚାନ୍ଦିପୁର ବେଳାଭୂମି",
        keyStops = "Sahadevkhunta, Station Chhak, ITI, Proof Road, DRDO Gate, Chandipur OTDC Panthanivas",
        frequencyMins = "Every 25 mins (06:30 AM to 08:30 PM)",
        fareRange = "₹10 - ₹25"
    ),
    MoBusRouteInfo(
        routeNumber = "Route 72",
        routeTitle = "Sahadevkhunta ⇄ Remuna Khirachora Temple",
        routeTitleOd = "ସହଦେବଖୁଣ୍ଟା ⇄ ରେମୁଣା ଖିରଚୋରା ଗୋପୀନାଥ",
        keyStops = "Sahadevkhunta, Cinema Bazar, Fandi Chhak, Remuna Golai, FM MCH, Temple Gate",
        frequencyMins = "Every 20 mins (06:00 AM to 09:00 PM)",
        fareRange = "₹10 - ₹20"
    ),
    MoBusRouteInfo(
        routeNumber = "Route 73",
        routeTitle = "Station Chhak ⇄ FM University Nuapadhi",
        routeTitleOd = "ଷ୍ଟେସନ ଛକ ⇄ ଏଫ୍.ଏମ୍. ବିଶ୍ୱବିଦ୍ୟାଳୟ ନୂଆପଢ଼ି",
        keyStops = "Railway Station, Police Line, Zilla School, Chhanpur Industrial, FMU Main Gate",
        frequencyMins = "Every 30 mins (Aligned with class hours)",
        fareRange = "₹10 - ₹30 (Student concessions apply)"
    ),
    MoBusRouteInfo(
        routeNumber = "Route 74",
        routeTitle = "Balasore Sahadevkhunta ⇄ Soro Sub-Division",
        routeTitleOd = "ବାଲେଶ୍ୱର ⇄ ବାହାନଗା ⇄ ସୋର",
        keyStops = "Sahadevkhunta, Gopalpur, Khantapara, Bahanaga Bazar, Soro Bus Stand",
        frequencyMins = "Every 35 mins (06:00 AM to 07:45 PM)",
        fareRange = "₹15 - ₹45"
    )
)

val INTER_DISTRICT_BUSES = listOf(
    InterDistrictBusService(
        destination = "Bhubaneswar (Baramunda ISBT)",
        busType = "OSRTC AC Volvo & High-Comfort Express",
        terminalBay = "Bay 1 & 2, Sahadevkhunta Terminal",
        departureSchedule = "Every 30 mins (24x7 service corridor)",
        estimatedFare = "₹240 - ₹380"
    ),
    InterDistrictBusService(
        destination = "Kolkata (Babughat / Esplanade)",
        busType = "Deluxe Sleeper Coaches (via Jaleswar / Kharagpur)",
        terminalBay = "Bay 7, Sahadevkhunta Terminal",
        departureSchedule = "Frequent night departures (09:00 PM - 11:30 PM)",
        estimatedFare = "₹350 - ₹550"
    ),
    InterDistrictBusService(
        destination = "Baripada (Mayurbhanj)",
        busType = "Regional Express Fleet (via Baisinga)",
        terminalBay = "Bay 4, Sahadevkhunta Terminal",
        departureSchedule = "Every 20 mins (05:30 AM - 08:30 PM)",
        estimatedFare = "₹70 - ₹95"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalasoreMoBusCrutNavigatorSheet(
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
                .testTag("balasore_mo_bus_crut_sheet")
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
                            .background(Color(0xFFCCFBF1)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🚌", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ମୋ ବସ୍ ଓ ସହଦେବଖୁଣ୍ଟା ଟର୍ମିନାଲ୍" else "Balasore Mo Bus & CRUT Transit Hub",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ସହରୀ ମୋ ବସ୍ ରୁଟ୍ • ସହଦେବଖୁଣ୍ଟା ବେ • ଭୁବନେଶ୍ୱର ଏକ୍ସପ୍ରେସ୍" else "Urban Mo Bus Routes • Sahadevkhunta Bays • Inter-District",
                            fontSize = 12.sp,
                            color = Color(0xFF0F766E)
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
                color = Color(0xFFF0FDFA),
                border = BorderStroke(1.dp, Color(0xFF99F6E4)),
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
                                .background(Color(0xFF0D9488))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "CRUT & Sahadevkhunta Terminal schedule live ($syncTime)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF115E59)
                        )
                    }
                    Text(
                        text = "FLEET ACTIVE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F766E)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFFF8FAFC),
                contentColor = Color(0xFF0F766E),
                indicator = {},
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ମୋ ବସ୍ ରୁଟ୍" else "Mo Bus Routes",
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
                            text = if (language == AppLanguage.ODIA) "ଅନ୍ତଃ-ଜିଲ୍ଲା ଏକ୍ସପ୍ରେସ" else "Inter-District",
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
                            text = if (language == AppLanguage.ODIA) "ରିହାତି ପାସ୍" else "Concession Pass",
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
                        items(MO_BUS_ROUTES) { route ->
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
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = route.routeNumber,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier
                                                .background(Color(0xFF0F766E), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                        Text(
                                            text = "Fare: ${route.fareRange}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF15803D)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = if (language == AppLanguage.ODIA) route.routeTitleOd else route.routeTitle,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A)
                                    )
                                    Text(
                                        text = "🛑 Stops: ${route.keyStops}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF64748B),
                                        lineHeight = 15.sp,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                    Text(
                                        text = "⏱️ Frequency: ${route.frequencyMins}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF0284C7),
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(top = 4.dp)
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
                        items(INTER_DISTRICT_BUSES) { bus ->
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
                                                text = "To ${bus.destination}",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = bus.busType,
                                                fontSize = 11.sp,
                                                color = Color(0xFF64748B)
                                            )
                                        }
                                        Text(
                                            text = bus.estimatedFare,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0F766E),
                                            modifier = Modifier
                                                .background(Color(0xFFCCFBF1), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "📍 ${bus.terminalBay}",
                                        fontSize = 11.sp,
                                        color = Color(0xFFB45309),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "🕒 Schedule: ${bus.departureSchedule}",
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
                            color = Color(0xFFF0FDFA),
                            border = BorderStroke(1.dp, Color(0xFF99F6E4)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ମୋ ବସ୍ ଡିଜିଟାଲ୍ ପାସ୍ ଓ ଛାତ୍ର ରିହାତି" else "Mo Bus Concessions & Monthly Passes",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F766E)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("• Student Monthly Concession Pass: ₹150 / month flat across all Balasore routes with valid student ID.", fontSize = 12.sp, color = Color(0xFF115E59))
                                Text("• Senior Citizens (>60 yrs): 50% concession on presenting Aadhaar card to the bus conductor.", fontSize = 12.sp, color = Color(0xFF115E59))
                                Text("• Cashless UPI QR Ticketing: Available on all conductor handheld billing machines.", fontSize = 12.sp, color = Color(0xFF115E59))
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:06782255100"))
                                context.startActivity(intent)
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F766E)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Call Sahadevkhunta Terminal Enquiry (06782-255100)", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
