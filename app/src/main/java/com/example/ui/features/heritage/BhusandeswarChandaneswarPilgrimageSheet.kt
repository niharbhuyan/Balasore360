package com.example.ui.features.heritage

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

data class ShivaPeethaItem(
    val shrineName: String,
    val shrineNameOd: String,
    val locationDistance: String,
    val historicalSignificance: String,
    val dailyDarshanTimings: String,
    val priestDeskPhone: String
)

data class PilgrimageShuttleRoute(
    val originDestination: String,
    val transitMode: String,
    val frequencySchedule: String,
    val oneWayFare: String
)

val SHIVA_PEETHAS = listOf(
    ShivaPeethaItem(
        shrineName = "Baba Bhusandeswar Temple (Asia's Largest Lingam)",
        shrineNameOd = "ବାବା ଭୂଷଣ୍ଡେଶ୍ୱର ମନ୍ଦିର (ଏସିଆର ବୃହତ୍ତମ ଶିବଲିଙ୍ଗ)",
        locationDistance = "Bhusandapur village, Bhograi (Near Subarnarekha River)",
        historicalSignificance = "Massive monolithic black granite Shiva Linga (12 ft high x 14 ft circumference) dating back to the Treta Yuga Ravana legend. Only one-third is visible above ground.",
        dailyDarshanTimings = "05:30 AM - 12:30 PM & 03:30 PM - 08:30 PM",
        priestDeskPhone = "9437821901"
    ),
    ShivaPeethaItem(
        shrineName = "Baba Chandaneswar Peetha (Famous Chadak Mela)",
        shrineNameOd = "ବାବା ଚନ୍ଦନେଶ୍ୱର ପୀଠ (ପ୍ରସିଦ୍ଧ ଚଡ଼କ ଓ ଗାଜନ ମେଳା)",
        locationDistance = "Chandaneswar, Bhograi (8 km from Digha / 38 km from Jaleswar)",
        historicalSignificance = "Historic svayambhu Shiva shrine revered across Odisha and Bengal. Centre of the miraculous Maha Vishuba Sankranti Chadak Mela where thousands of Bhoktas undertake austere penance.",
        dailyDarshanTimings = "05:00 AM - 01:00 PM & 03:00 PM - 09:30 PM",
        priestDeskPhone = "9437190442"
    )
)

val PILGRIM_SHUTTLES = listOf(
    PilgrimageShuttleRoute(
        originDestination = "Jaleswar Railway Station ⇄ Chandaneswar & Bhusandeswar",
        transitMode = "Mo Bus Corridor & Direct OSRTC Shuttles",
        frequencySchedule = "Every 35 mins from Station Bus Stand (06:00 AM to 08:00 PM)",
        oneWayFare = "₹45 - ₹60 / passenger"
    ),
    PilgrimageShuttleRoute(
        originDestination = "Digha Border / New Digha ⇄ Chandaneswar Temple",
        transitMode = "Battery E-Rickshaw (Toto) & Auto-Rickshaws",
        frequencySchedule = "Continuous 24x7 cross-border connectivity",
        oneWayFare = "₹20 - ₹30 / passenger (Shared Toto)"
    ),
    PilgrimageShuttleRoute(
        originDestination = "Balasore Sahadevkhunta ⇄ Chandaneswar Via Basta",
        transitMode = "Regular Express Private Bus Fleet",
        frequencySchedule = "Hourly departures from Bay 4",
        oneWayFare = "₹95 / passenger"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BhusandeswarChandaneswarPilgrimageSheet(
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
                .testTag("bhusandeswar_chandaneswar_sheet")
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
                        Text("🛕", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଭୂଷଣ୍ଡେଶ୍ୱର ଓ ଚନ୍ଦନେଶ୍ୱର ତୀର୍ଥ ରାଡାର" else "Bhusandeswar & Chandaneswar Pilgrimage",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଏସିଆର ବୃହତ୍ତମ ଶିବଲିଙ୍ଗ • ଚଡ଼କ ମେଳା • ତୀର୍ଥ ଶଟଲ" else "Monolith Black Stone • Chadak Mela • Shuttles",
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
                                .background(Color(0xFFD97706))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Temple Endowment & Darshan schedule synced ($syncTime)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF92400E)
                        )
                    }
                    Text(
                        text = "DARSHAN OPEN",
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
                contentColor = Color(0xFFB45309),
                indicator = {},
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଶିବ ପୀଠ" else "Holy Shrines",
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
                            text = if (language == AppLanguage.ODIA) "ତୀର୍ଥ ଶଟଲ" else "Pilgrim Transit",
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
                            text = if (language == AppLanguage.ODIA) "ଚଡ଼କ ମେଳା ବିଧି" else "Chadak Mela",
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
                        items(SHIVA_PEETHAS) { peetha ->
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
                                                text = if (language == AppLanguage.ODIA) peetha.shrineNameOd else peetha.shrineName,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "📍 ${peetha.locationDistance}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF64748B)
                                            )
                                        }
                                        Text(
                                            text = "OPEN",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF15803D),
                                            modifier = Modifier
                                                .background(Color(0xFFDCFCE7), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = peetha.historicalSignificance,
                                        fontSize = 11.sp,
                                        color = Color(0xFF334155),
                                        lineHeight = 16.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "🕒 Darshan: ${peetha.dailyDarshanTimings}",
                                        fontSize = 11.sp,
                                        color = Color(0xFFB45309),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = {
                                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${peetha.priestDeskPhone}"))
                                            context.startActivity(intent)
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp)
                                    ) {
                                        Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Call Temple Sevayat Desk", fontSize = 11.sp)
                                    }
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
                        items(PILGRIM_SHUTTLES) { shuttle ->
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
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = shuttle.originDestination,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "🚌 ${shuttle.transitMode}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF0284C7),
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                        Text(
                                            text = shuttle.oneWayFare,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF15803D),
                                            modifier = Modifier
                                                .background(Color(0xFFDCFCE7), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "⏱️ Timings: ${shuttle.frequencySchedule}",
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
                            color = Color(0xFFFFFBEB),
                            border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଚନ୍ଦନେଶ୍ୱର ଚଡ଼କ ଓ ଗାଜନ ପର୍ବ ପରମ୍ପରା" else "Chandaneswar Chadak Mela & Bhokta Traditions",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF92400E)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("• Annual Festival: Celebrated during Chaitra month ending on Pana Sankranti (mid-April).", fontSize = 12.sp, color = Color(0xFF78350F))
                                Text("• Bhokta Vows: Over 50,000 devotees undergo 13 days of rigorous penance with only fruit and milk diet.", fontSize = 12.sp, color = Color(0xFF78350F))
                                Text("• Chadak Wheel & Piercing: Sacred ritual where initiated Bhoktas spin on wooden pillars in deep devotion.", fontSize = 12.sp, color = Color(0xFF78350F))
                            }
                        }
                    }
                }
            }
        }
    }
}
