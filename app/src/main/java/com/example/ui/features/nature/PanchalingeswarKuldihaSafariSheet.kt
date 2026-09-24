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

data class ForestGateStatus(
    val gateName: String,
    val gateNameOd: String,
    val entryTimings: String,
    val elephantSightingRisk: String,
    val safariPermitFee: String,
    val isGateOpen: Boolean
)

data class SafariDriverContact(
    val driverName: String,
    val vehicleModel: String,
    val baseStation: String,
    val phone: String,
    val rating: String
)

val KULDIHA_GATES = listOf(
    ForestGateStatus(
        gateName = "Rissia Forest Checkpost (Main Safari Entry)",
        gateNameOd = "ରିସିଆ ଜଙ୍ଗଲ ଚେକପୋଷ୍ଟ୍ (ମୁଖ୍ୟ ସଫାରୀ ଗେଟ୍)",
        entryTimings = "06:00 AM - 04:30 PM (Night drive strictly prohibited)",
        elephantSightingRisk = "HIGH (Herds near Rissia Waterbody at dusk)",
        safariPermitFee = "₹100 Entry + ₹400 Vehicle Permit",
        isGateOpen = true
    ),
    ForestGateStatus(
        gateName = "Gohiraprada Range Gate (Eco-Camp Route)",
        gateNameOd = "ଗୋହିରାପଦା ରେଞ୍ଜ୍ ଗେଟ୍ (ଇକୋ କ୍ୟାମ୍ପ୍ ରୁଟ୍)",
        entryTimings = "06:30 AM - 04:00 PM",
        elephantSightingRisk = "MODERATE (Giant squirrel & deer sightings active)",
        safariPermitFee = "₹100 Entry + Guide Mandatory ₹250",
        isGateOpen = true
    ),
    ForestGateStatus(
        gateName = "Panchalingeswar Hill Base Gate",
        gateNameOd = "ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ପାଦଦେଶ ପ୍ରବେଶ ପଥ",
        entryTimings = "05:00 AM - 08:30 PM",
        elephantSightingRisk = "LOW (Elephant proof solar trench operational)",
        safariPermitFee = "Free Entry (Temple Pilgrimage Trail)",
        isGateOpen = true
    )
)

val SAFARI_DRIVERS = listOf(
    SafariDriverContact(
        driverName = "Gouranga Giri (Kuldiha Wildlife Guide & 4x4)",
        vehicleModel = "Mahindra Bolero 4x4 (6-Seater)",
        baseStation = "Nilagiri Taxi Stand & Rissia Gate",
        phone = "9437291840",
        rating = "4.9 ★ (12 Yrs Forest Exp)"
    ),
    SafariDriverContact(
        driverName = "Rabindra Nayak (Safari & Devkund Transit)",
        vehicleModel = "Force Trax Cruiser (Open Canopy)",
        baseStation = "Panchalingeswar OTDC Panthasala",
        phone = "9861452033",
        rating = "4.8 ★ (Approved Forest Guide)"
    ),
    SafariDriverContact(
        driverName = "Manoj Das (Nilagiri Chhau & Kuldiha Eco)",
        vehicleModel = "Mahindra Thar Jungle Safari",
        baseStation = "Gohiraprada Checkpost",
        phone = "9438319502",
        rating = "4.9 ★ (Elephant Tracker Certified)"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanchalingeswarKuldihaSafariSheet(
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }
    var syncTimestamp by remember { mutableStateOf("Just now") }

    LaunchedEffect(Unit) {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        syncTimestamp = sdf.format(Date())
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
                .testTag("panchalingeswar_kuldiha_sheet")
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
                            .background(Color(0xFFDCFCE7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🐘", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ଓ କୁଲଡିହା ସଫାରୀ କମ୍ପାନିଅନ୍" else "Panchalingeswar & Kuldiha Safari",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ହାତୀ କରିଡର ସତର୍କତା • ଝରଣା ଜଳସ୍ତର • ୪x୪ ଜିପ୍ ଗାଇଡ୍" else "Elephant Corridor Alerts • Spring Flow • 4x4 Jeeps",
                            fontSize = 12.sp,
                            color = Color(0xFF15803D)
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
                color = Color(0xFFF0FDF4),
                border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
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
                                .background(Color(0xFF22C55E))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Kuldiha Forest Sanctuary Radar live ($syncTimestamp)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF166534)
                        )
                    }
                    Text(
                        text = "SAFE ENTRY",
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
                contentColor = Color(0xFF15803D),
                indicator = {},
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଜଙ୍ଗଲ ଗେଟ୍" else "Forest Gates",
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
                            text = if (language == AppLanguage.ODIA) "ପାହାଡ଼ ଝରଣା" else "Spring & Steps",
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
                            text = if (language == AppLanguage.ODIA) "ସଫାରୀ ଡ୍ରାଇଭର" else "Jeep Drivers",
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
                        items(KULDIHA_GATES) { gate ->
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
                                                text = if (language == AppLanguage.ODIA) gate.gateNameOd else gate.gateName,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "🕒 ${gate.entryTimings}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF475569)
                                            )
                                        }
                                        Text(
                                            text = if (gate.isGateOpen) "OPEN" else "CLOSED",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (gate.isGateOpen) Color(0xFF16A34A) else Color(0xFFDC2626),
                                            modifier = Modifier
                                                .background(
                                                    if (gate.isGateOpen) Color(0xFFDCFCE7) else Color(0xFFFEE2E2),
                                                    RoundedCornerShape(6.dp)
                                                )
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "⚠️ Sighting Status: ${gate.elephantSightingRisk}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFFB45309)
                                    )
                                    Text(
                                        text = "🎟️ Tariff: ${gate.safariPermitFee}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF0F766E),
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
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
                                    text = if (language == AppLanguage.ODIA) "ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ଜଳପ୍ରବାହ ଓ ୩୦୦ ପାହାଚ ତଥ୍ୟ" else "Submerged Spring & 300 Rock Steps Climb",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E3A8A)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "The 5 natural stone Shiva Lingas lie submerged under a perennial hill spring on Nilagiri range. Devotees touch the submerged deity by feeling through the cool crystal water. Climbing involves ~315 stone steps with shaded handrails and rest benches.",
                                    fontSize = 12.sp,
                                    color = Color(0xFF1E40AF),
                                    lineHeight = 17.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text("Current Water Flow", fontSize = 11.sp, color = Color(0xFF64748B))
                                        Text("Perennial Moderate (Touch Safe)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F766E))
                                    }
                                    Column {
                                        Text("Best Darshan Hours", fontSize = 11.sp, color = Color(0xFF64748B))
                                        Text("06:00 AM - 11:30 AM", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E3A8A))
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:06782233249"))
                                context.startActivity(intent)
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF15803D)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Call OTDC Panthasala, Panchalingeswar", fontSize = 12.sp)
                        }
                    }
                }
                2 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxHeight(0.82f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(SAFARI_DRIVERS) { driver ->
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
                                                text = driver.driverName,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "🚙 ${driver.vehicleModel} • 📍 ${driver.baseStation}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF475569)
                                            )
                                            Text(
                                                text = driver.rating,
                                                fontSize = 11.sp,
                                                color = Color(0xFFB45309),
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                        Button(
                                            onClick = {
                                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${driver.phone}"))
                                                context.startActivity(intent)
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF15803D)),
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
