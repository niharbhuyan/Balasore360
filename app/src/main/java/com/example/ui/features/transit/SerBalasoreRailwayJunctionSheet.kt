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

data class SerTrainHalt(
    val trainNoAndName: String,
    val trainNameOd: String,
    val scheduledTime: String,
    val platformExpected: String,
    val direction: String,
    val coachPositionSummary: String
)

data class PassengerMemuSchedule(
    val trainName: String,
    val route: String,
    val departureTime: String,
    val frequency: String,
    val fareRs: String
)

val SER_BALASORE_TRAINS = listOf(
    SerTrainHalt(
        trainNoAndName = "22895 / 22896 Howrah - Puri Vande Bharat Express",
        trainNameOd = "ହାୱଡ଼ା - ପୁରୀ ବନ୍ଦେ ଭାରତ ଏକ୍ସପ୍ରେସ୍",
        scheduledTime = "09:03 AM (Up) / 05:42 PM (Dn)",
        platformExpected = "PF 1 (Up) / PF 2 (Dn)",
        direction = "Howrah ⇄ Puri via Balasore",
        coachPositionSummary = "Engine - C1 to C14 - E1/E2 (Executive) - Rear"
    ),
    SerTrainHalt(
        trainNoAndName = "12821 / 12822 Dhauli Express",
        trainNameOd = "ଧଉଳି ଏକ୍ସପ୍ରେସ୍ (ହାୱଡ଼ା - ପୁରୀ)",
        scheduledTime = "12:45 PM (Up) / 02:15 PM (Dn)",
        platformExpected = "PF 2 (Up) / PF 1 (Dn)",
        direction = "Howrah ⇄ Puri",
        coachPositionSummary = "Gen - D1 to D8 (CC) - C1/C2 - DL1 to DL4"
    ),
    SerTrainHalt(
        trainNoAndName = "12841 / 12842 Coromandel Superfast Express",
        trainNameOd = "କରମଣ୍ଡଳ ଏକ୍ସପ୍ରେସ୍ (ହାୱଡ଼ା - ଚେନ୍ନାଇ)",
        scheduledTime = "06:50 PM (Up) / 07:10 AM (Dn)",
        platformExpected = "PF 1 / PF 2",
        direction = "Shalimar ⇄ MGR Chennai Central",
        coachPositionSummary = "24-Coach LHB: EOG - B1 to B6 - A1/A2 - S1 to S7"
    ),
    SerTrainHalt(
        trainNoAndName = "18409 / 18410 Sri Jagannath Express",
        trainNameOd = "ଶ୍ରୀ ଜଗନ୍ନାଥ ଏକ୍ସପ୍ରେସ୍",
        scheduledTime = "10:15 PM (Up) / 04:30 AM (Dn)",
        platformExpected = "PF 3 / PF 4",
        direction = "Howrah ⇄ Puri",
        coachPositionSummary = "Pantry - 3A - 2A - SL - Gen Coaches"
    )
)

val LOCAL_MEMU_TRAINS = listOf(
    PassengerMemuSchedule(
        trainName = "Rupsa - Bhanjpur (Baripada) Special Passenger",
        route = "Rupsa Jn -> Betnoti -> Baripada -> Bhanjpur",
        departureTime = "07:30 AM & 03:45 PM",
        frequency = "Daily Service",
        fareRs = "₹15 - ₹25"
    ),
    PassengerMemuSchedule(
        trainName = "Balasore - Bhadrak MEMU Local",
        route = "Balasore -> Khantapara -> Soro -> Markona -> Bhadrak",
        departureTime = "08:10 AM, 01:20 PM, 06:40 PM",
        frequency = "Daily Regular",
        fareRs = "₹10 - ₹20"
    ),
    PassengerMemuSchedule(
        trainName = "Balasore - Kharagpur MEMU Passenger",
        route = "Balasore -> Haldipada -> Basta -> Jaleswar -> Belda -> KGP",
        departureTime = "06:15 AM, 11:00 AM, 05:30 PM",
        frequency = "Daily Frequent",
        fareRs = "₹15 - ₹30"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SerBalasoreRailwayJunctionSheet(
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
                .testTag("ser_balasore_railway_sheet")
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
                            .background(Color(0xFFE0E7FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🚆", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ଓ ଜଳେଶ୍ୱର ରେଳ ଜଙ୍କସନ ରାଡାର" else "Balasore & Jaleswar SER Rail Radar",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ପ୍ଲାଟଫର୍ମ ସ୍ଥିତି • ବନ୍ଦେ ଭାରତ କୋଚ୍ • ମେମୁ ପାସେଞ୍ଜର" else "Live Platforms • Vande Bharat • MEMU Trains",
                            fontSize = 12.sp,
                            color = Color(0xFF4338CA)
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
                color = Color(0xFFEEF2FF),
                border = BorderStroke(1.dp, Color(0xFFC7D2FE)),
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
                                .background(Color(0xFF4F46E5))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "South Eastern Railway Kharagpur Div synced at $syncTime",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF3730A3)
                        )
                    }
                    Text(
                        text = "BLS JN (4 PFs)",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4338CA)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFFF8FAFC),
                contentColor = Color(0xFF4338CA),
                indicator = {},
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଏକ୍ସପ୍ରେସ୍ ଟ୍ରେନ୍" else "Express Trains",
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
                            text = if (language == AppLanguage.ODIA) "ମେମୁ ଲୋକାଲ" else "MEMU Locals",
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
                            text = if (language == AppLanguage.ODIA) "କୁଲି ଓ ସୁବିଧା" else "Porters & SOS",
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
                        items(SER_BALASORE_TRAINS) { train ->
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
                                                text = if (language == AppLanguage.ODIA) train.trainNameOd else train.trainNoAndName,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "⏰ Time: ${train.scheduledTime}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF0284C7),
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                        Text(
                                            text = train.platformExpected,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF4338CA),
                                            modifier = Modifier
                                                .background(Color(0xFFEEF2FF), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "📍 Route: ${train.direction}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF475569)
                                    )
                                    Text(
                                        text = "🚃 Formation: ${train.coachPositionSummary}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF166534),
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
                        items(LOCAL_MEMU_TRAINS) { memu ->
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
                                                text = memu.trainName,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "🛤️ ${memu.route}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF475569)
                                            )
                                        }
                                        Text(
                                            text = memu.fareRs,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF15803D),
                                            modifier = Modifier
                                                .background(Color(0xFFDCFCE7), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "🕒 Departures: ${memu.departureTime} (${memu.frequency})",
                                        fontSize = 11.sp,
                                        color = Color(0xFF0284C7),
                                        fontWeight = FontWeight.SemiBold
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
                            color = Color(0xFFF8FAFC),
                            border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଅନୁମୋଦିତ କୁଲି ମଜୁରୀ ଓ ଷ୍ଟେସନ୍ ସୁବିଧା" else "Approved Porter (Coolie) Rates & Station Amenities",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("• Up to 40 kg luggage: ₹100 per trip between any platform", fontSize = 12.sp, color = Color(0xFF334155))
                                Text("• Wheelchair / Stretcher Porter: ₹150 (Available at PF 1 Help Desk)", fontSize = 12.sp, color = Color(0xFF334155))
                                Text("• Battery Operated Car: Free for Senior Citizens & Divyang (PF 1 to 4)", fontSize = 12.sp, color = Color(0xFF334155))
                                Text("• AC Retiring Room & Dormitory: Online booking via IRCTC / Station Master", fontSize = 12.sp, color = Color(0xFF334155))
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:139"))
                                context.startActivity(intent)
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Dial Indian Railways & RPF Security Helpline 139", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
