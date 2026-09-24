package com.example.ui.features.nature

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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

data class SafariGateDetail(
    val gateName: String,
    val gateNameOd: String,
    val entryTimings: String,
    val locationDistance: String,
    val vehicleTypesAllowed: String,
    val entryFeePerHead: String,
    val rangerContact: String
)

data class WatchtowerSpot(
    val spotName: String,
    val spotNameOd: String,
    val bestObservationHours: String,
    val keyFaunaSpotted: String,
    val safetyGuidelines: String
)

val KULDIHA_SAFARI_GATES = listOf(
    SafariGateDetail(
        gateName = "Nilagiri Checkpost (Main Entry)",
        gateNameOd = "ନୀଳଗିରି ଚେକପୋଷ୍ଟ (ମୁଖ୍ୟ ପ୍ରବେଶ ପଥ)",
        entryTimings = "06:00 AM - 04:30 PM (Closes at Sunset)",
        locationDistance = "32 km from Balasore Railway Station",
        vehicleTypesAllowed = "4x4 Gypsy, High-clearance SUV, Authorized forest jeeps",
        entryFeePerHead = "₹40 per adult / ₹100 vehicle permit",
        rangerContact = "06782-233221"
    ),
    SafariGateDetail(
        gateName = "Gohirabhincha Eco-Gate",
        gateNameOd = "ଗୋହିରାଭିଞ୍ଚା ନେଚର ଗେଟ୍",
        entryTimings = "06:30 AM - 04:00 PM",
        locationDistance = "40 km from Balasore via Mitrapur",
        vehicleTypesAllowed = "Registered safari gypsies & forest department vehicles",
        entryFeePerHead = "₹40 per adult",
        rangerContact = "9437298510"
    ),
    SafariGateDetail(
        gateName = "Rissia Dam Checkpost",
        gateNameOd = "ରିଷିଆ ଡ୍ୟାମ୍ ଚେକପୋଷ୍ଟ",
        entryTimings = "07:00 AM - 05:00 PM (Camp guests 24h pass)",
        locationDistance = "45 km inside Kuldiha dense canopy",
        vehicleTypesAllowed = "Strictly eco-cottage confirmed guests & day safaris",
        entryFeePerHead = "Included with OFDC cottage pass",
        rangerContact = "06782-262228"
    )
)

val KULDIHA_WATCHTOWERS = listOf(
    WatchtowerSpot(
        spotName = "Jadachua Salt Lick & Waterhole",
        spotNameOd = "ଜଡ଼ାଚୁଆ ଲୁଣ ପଥର ଓ ଜଳାଶୟ",
        bestObservationHours = "06:30 AM - 08:30 AM & 03:30 PM - 05:30 PM",
        keyFaunaSpotted = "Asiatic wild elephants, Gaur (Indian bison), Sambar deer, Barking deer",
        safetyGuidelines = "Stay inside the wooden watchtower. Strict silence required; flash photography prohibited."
    ),
    WatchtowerSpot(
        spotName = "Rissia Reservoir Catchment",
        spotNameOd = "ରିଷିଆ ଜଳଭଣ୍ଡାର",
        bestObservationHours = "Early morning sunrise & late afternoon",
        keyFaunaSpotted = "Malabar Pied Hornbill, Giant Squirrel, Otters, Migratory waterfowl",
        safetyGuidelines = "No walking on dam mudflats without registered EDC local forest guide."
    ),
    WatchtowerSpot(
        spotName = "Purunapani Canopy Stream",
        spotNameOd = "ପୁରୁଣାପାଣି ଝରଣା",
        bestObservationHours = "11:00 AM - 02:00 PM (Midday watering)",
        keyFaunaSpotted = "Elephant herds with calves, Wild boars, Leopard tracks on wet sand",
        safetyGuidelines = "Always give minimum 50-meter right of way to elephant herds crossing tracks."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KuldihaEcoCampSafariSheet(
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
                .testTag("kuldiha_eco_camp_safari_sheet")
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
                            text = if (language == AppLanguage.ODIA) "କୁଲଡିହା ନେଚର କ୍ୟାମ୍ପ ଓ ସଫାରୀ" else "Kuldiha Eco-Camp & Safari Desk",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ରିଷିଆ ନେଚର କ୍ୟାମ୍ପ • ଜଡ଼ାଚୁଆ ହାତୀ ୱାଚଟାୱାର୍" else "OFDC Eco-Tourism • Elephant Sanctuary",
                            fontSize = 12.sp,
                            color = Color(0xFF166534)
                        )
                    }
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFFF1F5F9),
                contentColor = Color(0xFF166534),
                indicator = {},
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ପ୍ରବେଶ ଫାଟକ ଓ ଫିସ୍" else "Safari Gates & Fees",
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
                            text = if (language == AppLanguage.ODIA) "ୱାଚଟାୱାର୍ ସ୍ପଟ୍" else "Watchtowers",
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
                            text = if (language == AppLanguage.ODIA) "କ୍ୟାମ୍ପ ବୁକିଂ ଓ ନିୟମ" else "Eco-Cottages",
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
                        items(KULDIHA_SAFARI_GATES) { gate ->
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
                                                text = if (language == AppLanguage.ODIA) gate.gateNameOd else gate.gateName,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "📍 ${gate.locationDistance}",
                                                fontSize = 12.sp,
                                                color = Color(0xFF475569)
                                            )
                                            Text(
                                                text = "🕒 Timings: ${gate.entryTimings}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF0369A1),
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Text(
                                                text = "🚙 Vehicles: ${gate.vehicleTypesAllowed}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF64748B)
                                            )
                                            Text(
                                                text = "🎟️ Tariff: ${gate.entryFeePerHead}",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF166534)
                                            )
                                        }
                                        Button(
                                            onClick = {
                                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${gate.rangerContact}"))
                                                context.startActivity(intent)
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF166534)),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Range Desk", fontSize = 11.sp)
                                        }
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
                        items(KULDIHA_WATCHTOWERS) { spot ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = if (language == AppLanguage.ODIA) spot.spotNameOd else spot.spotName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A)
                                    )
                                    Text(
                                        text = "⏰ Best Spotting Hours: ${spot.bestObservationHours}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF0284C7),
                                        modifier = Modifier.padding(vertical = 3.dp)
                                    )
                                    Text(
                                        text = "🐾 Species: ${spot.keyFaunaSpotted}",
                                        fontSize = 12.sp,
                                        color = Color(0xFF15803D),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "⚠️ Rules: ${spot.safetyGuidelines}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF64748B)
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
                            color = Color(0xFFF0FDF4),
                            border = BorderStroke(1.dp, Color(0xFF86EFAC)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ରିଷିଆ ନେଚର କ୍ୟାମ୍ପ କଟେଜ୍ ବୁକିଂ" else "Rissia Nature Camp Cottages (OFDC)",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF166534)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Official Odisha Forest Development Corporation luxury tented and masonry eco-cottages overlooking the Rissia reservoir. Includes all organic tribal meals, campfire, and mandatory guided elephant corridor tour.",
                                    fontSize = 12.sp,
                                    color = Color(0xFF14532D),
                                    lineHeight = 17.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ecotourodisha.com"))
                                        context.startActivity(intent)
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF166534)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.OpenInNew, contentDescription = "Book")
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Open EcoTour Odisha Official Portal", fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFFEF2F2),
                            border = BorderStroke(1.dp, Color(0xFFFCA5A5)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଜଙ୍ଗଲ ନିରାପତ୍ତା ସତର୍କତା" else "Sanctuary Code of Conduct",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF991B1B)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("• Plastic carry bags and single-use bottles strictly prohibited inside Kuldiha.", fontSize = 11.sp, color = Color(0xFF7F1D1D))
                                Text("• No alighting from vehicles inside the elephant corridor under any circumstance.", fontSize = 11.sp, color = Color(0xFF7F1D1D))
                                Text("• Drones / UAV aerial cameras strictly banned under Wildlife Protection Act.", fontSize = 11.sp, color = Color(0xFF7F1D1D))
                            }
                        }
                    }
                }
            }
        }
    }
}
