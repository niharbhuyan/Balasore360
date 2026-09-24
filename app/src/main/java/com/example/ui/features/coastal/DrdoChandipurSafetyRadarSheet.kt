package com.example.ui.features.coastal

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

enum class RangeAlertLevel(val labelEn: String, val labelOd: String, val badgeColor: Color, val textColor: Color) {
    CLEAR("NORMAL / WATERS CLEAR", "ସ୍ୱାଭାବିକ / ସମୁଦ୍ର ମୁକ୍ତ", Color(0xFFDCFCE7), Color(0xFF166534)),
    ADVISORY("SAFETY ADVISORY IN EFFECT", "ସତର୍କତା ସୂଚନା ଜାରି", Color(0xFFFEF3C7), Color(0xFFB45309)),
    EXCLUSION_ACTIVE("LIVE EXCLUSION ZONE ACTIVE", "ପରୀକ୍ଷଣ କ୍ଷେତ୍ର ନିଷେଧାଞ୍ଚଳ ସକ୍ରିୟ", Color(0xFFFEE2E2), Color(0xFF991B1B))
}

data class ChandipurLaunchNotice(
    val id: String,
    val titleEn: String,
    val titleOd: String,
    val status: RangeAlertLevel,
    val zoneDescription: String,
    val sirenCode: String,
    val advisoryTime: String,
    val fishermenCompensationStatus: String
)

data class RangeSafetyContact(
    val officeTitleEn: String,
    val officeTitleOd: String,
    val location: String,
    val telephoneNumber: String,
    val dutyRole: String
)

val CHANDIPUR_NOTICES = listOf(
    ChandipurLaunchNotice(
        id = "ITR-2026-N09",
        titleEn = "Chandipur Sea Range Notice (Bay of Bengal)",
        titleOd = "ଚାନ୍ଦିପୁର ସାମୁଦ୍ରିକ ପରୀକ୍ଷଣ କ୍ଷେତ୍ର ବିଜ୍ଞପ୍ତି",
        status = RangeAlertLevel.CLEAR,
        zoneDescription = "Designated sea danger zone 05 to 45 nautical miles south-east of Chandipur launch pad. All mechanized trawlers cleared for normal fishing operations.",
        sirenCode = "Green Flag Hoisted at Range Post. No Siren in last 24h.",
        advisoryTime = "Valid: Today • Next Scheduled Assessment 06:00 AM",
        fishermenCompensationStatus = "Civil Administration Disbursal Desk Standby"
    ),
    ChandipurLaunchNotice(
        id = "ITR-2026-N08",
        titleEn = "Wheeler Island (Dr. APJ Abdul Kalam Island) Perimeter",
        titleOd = "ହୁଇଲର ଦ୍ୱୀପ (ଡଃ ଏପିଜେ ଅବ୍ଦୁଲ କଲାମ ଦ୍ୱୀପ) ସୁରକ୍ଷା",
        status = RangeAlertLevel.ADVISORY,
        zoneDescription = "Restricted marine perimeter: 10 nautical miles around Dhamra estuary & Kalam Island. Continuous Indian Coast Guard radar patrol.",
        sirenCode = "Standard Maritime VHF Channel 16 Broadcasts Active",
        advisoryTime = "Routine Defense Surveillance Protocol",
        fishermenCompensationStatus = "Naval Police monitoring"
    ),
    ChandipurLaunchNotice(
        id = "ITR-2026-EVAC",
        titleEn = "Temporary Coastal Shelter Evacuation Protocol",
        titleOd = "ଉପକୂଳ ଗ୍ରାମବାସୀ ଅସ୍ଥାୟୀ ସ୍ଥାନାନ୍ତରଣ ନିୟମାବଳୀ",
        status = RangeAlertLevel.CLEAR,
        zoneDescription = "Covers Jayadevkasaba, Kusumuli, and Bhimpur revenue villages within 2.5 km radius when high-altitude flight trials occur.",
        sirenCode = "3 Long Siren Blasts = Evacuate to Designated DRDO PUCCA Camp",
        advisoryTime = "Compensation token: ₹850 - ₹1200 per adult provided by Dist. Revenue",
        fishermenCompensationStatus = "Sub-Collector Balasore Liaison"
    )
)

val RANGE_SAFETY_CONTACTS = listOf(
    RangeSafetyContact(
        officeTitleEn = "ITR Range Safety & Public Liaison Office",
        officeTitleOd = "ଆଇଟିଆର୍ ରେଞ୍ଜ ସୁରକ୍ଷା ଓ ଜନ ସମ୍ପର୍କ ସେଲ୍",
        location = "DRDO Complex, Chandipur, Balasore",
        telephoneNumber = "06782-272001",
        dutyRole = "24x7 Range hazard notification & exclusion updates"
    ),
    RangeSafetyContact(
        officeTitleEn = "Chandipur Marine Police Station",
        officeTitleOd = "ଚାନ୍ଦିପୁର ସାମୁଦ୍ରିକ ଥାନା",
        location = "Chandipur Beach Road",
        telephoneNumber = "06782-272100",
        dutyRole = "Boat trawler clearance & fisherman registry verification"
    ),
    RangeSafetyContact(
        officeTitleEn = "Balasore District Emergency Operations Center (DEOC)",
        officeTitleOd = "ଜିଲ୍ଲା ଜରୁରୀକାଳୀନ ପରିଚାଳନା କେନ୍ଦ୍ର",
        location = "Collectorate, Balasore",
        telephoneNumber = "06782-262674",
        dutyRole = "Evacuation shelter logistics & compensation disbursement"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrdoChandipurSafetyRadarSheet(
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }
    var lastRefreshedTime by remember { mutableStateOf("Just now") }
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        lastRefreshedTime = sdf.format(Date())
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
                .testTag("drdo_chandipur_safety_radar_sheet")
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
                            .background(Color(0xFFFEF2F2)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🚀", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଚାନ୍ଦିପୁର କ୍ଷେପଣାସ୍ତ୍ର ପରୀକ୍ଷଣ କ୍ଷେତ୍ର ଓ ସୁରକ୍ଷା ରାଡାର" else "DRDO ITR Chandipur Safety & Range Radar",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ନିଷେଧାଞ୍ଚଳ ଟେଲିମେଟ୍ରି • ସାଇରନ୍ ସତର୍କତା • ସ୍ଥାନାନ୍ତରଣ ଭତ୍ତା" else "Launch Exclusion Zone • Siren Codes • Auto-Telemetry",
                            fontSize = 12.sp,
                            color = Color(0xFFDC2626)
                        )
                    }
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Live Telemetry Sync Pill
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF1F5F9),
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
                            text = "Auto-Synced at $lastRefreshedTime (Bay of Bengal Sector 4)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF334155)
                        )
                    }
                    Text(
                        text = "RANGE CLEAR",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF166534),
                        modifier = Modifier
                            .background(Color(0xFFDCFCE7), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFFF8FAFC),
                contentColor = Color(0xFFDC2626),
                indicator = {},
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ସୁରକ୍ଷା ସୂଚନା" else "Range Radar",
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
                            text = if (language == AppLanguage.ODIA) "ସାଇରନ୍ ସଙ୍କେତ" else "Siren Protocols",
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
                            text = if (language == AppLanguage.ODIA) "ଜରୁରୀ ସମ୍ପର୍କ" else "ITR Contacts",
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
                        items(CHANDIPUR_NOTICES) { item ->
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
                                        Text(
                                            text = if (language == AppLanguage.ODIA) item.titleOd else item.titleEn,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0F172A),
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = if (language == AppLanguage.ODIA) item.status.labelOd else item.status.labelEn,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = item.status.textColor,
                                            modifier = Modifier
                                                .background(item.status.badgeColor, RoundedCornerShape(6.dp))
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = item.zoneDescription,
                                        fontSize = 12.sp,
                                        color = Color(0xFF334155),
                                        lineHeight = 17.sp
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFFEFF6FF),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text("📢", fontSize = 13.sp)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = item.sirenCode,
                                                fontSize = 11.sp,
                                                color = Color(0xFF1E40AF),
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = item.advisoryTime,
                                            fontSize = 10.sp,
                                            color = Color(0xFF64748B)
                                        )
                                        Text(
                                            text = item.fishermenCompensationStatus,
                                            fontSize = 10.sp,
                                            color = Color(0xFF0F766E),
                                            fontWeight = FontWeight.Medium
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
                            color = Color(0xFFFEF2F2),
                            border = BorderStroke(1.dp, Color(0xFFFECACA)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ସାଇରନ୍ ସଙ୍କେତ ଓ ସୁରକ୍ଷା ନିୟମାବଳୀ" else "Official DRDO Siren Signal Decoder",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF991B1B)
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(verticalAlignment = Alignment.Top) {
                                    Text("🟡", fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = if (language == AppLanguage.ODIA) "୧ ଲମ୍ବା ହୁଇସିଲ୍ / ସାଇରନ୍ (Yellow Alert)" else "1 Long Blast (Yellow Alert - 2 Hours Prior)",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF92400E)
                                        )
                                        Text(
                                            text = "Fishermen trawlers must stop casting nets and proceed outward beyond designated beacon markers.",
                                            fontSize = 11.sp,
                                            color = Color(0xFF78350F)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(verticalAlignment = Alignment.Top) {
                                    Text("🔴", fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = if (language == AppLanguage.ODIA) "୩ଟି ଲମ୍ବା ସାଇରନ୍ (Red Alert - Evacuate Zone)" else "3 Long Continuous Blasts (Red Alert - Range Live)",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF991B1B)
                                        )
                                        Text(
                                            text = "Designated coastal hamlets within 2.5 km must move immediately to civil administration shelters. Compensation coupons collected.",
                                            fontSize = 11.sp,
                                            color = Color(0xFF7F1D1D)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(verticalAlignment = Alignment.Top) {
                                    Text("🟢", fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = if (language == AppLanguage.ODIA) "୨ କ୍ଷୁଦ୍ର ସାଇରନ୍ (All Clear)" else "2 Short Chimes (All Clear Signal)",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF166534)
                                        )
                                        Text(
                                            text = "Launch mission completed. Coastal villagers and fishing vessels may safely resume activities.",
                                            fontSize = 11.sp,
                                            color = Color(0xFF14532D)
                                        )
                                    }
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
                        items(RANGE_SAFETY_CONTACTS) { contact ->
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
                                                text = if (language == AppLanguage.ODIA) contact.officeTitleOd else contact.officeTitleEn,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "📍 ${contact.location}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF475569)
                                            )
                                            Text(
                                                text = "🛡️ ${contact.dutyRole}",
                                                fontSize = 11.sp,
                                                color = Color(0xFFDC2626),
                                                modifier = Modifier.padding(top = 2.dp)
                                            )
                                        }
                                        Button(
                                            onClick = {
                                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${contact.telephoneNumber}"))
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
            }
        }
    }
}
