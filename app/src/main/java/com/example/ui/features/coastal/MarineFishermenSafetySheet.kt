package com.example.ui.features.coastal

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

data class MarineHarborTelemetry(
    val harborName: String,
    val harborNameOd: String,
    val seaCondition: String, // "Moderate (Yellow)", "Calm (Green)", "Rough (Red)"
    val waveHeightMeters: String,
    val windSpeedKnots: String,
    val barometricPressureHpa: String,
    val fishingSafetyRecommendation: String,
    val isSafeToSail: Boolean
)

data class MarinePoliceContact(
    val stationName: String,
    val stationNameOd: String,
    val officerInChargePhone: String,
    val landlinePhone: String,
    val jurisdictionCoast: String
)

val BALASORE_HARBOR_TELEMETRY = listOf(
    MarineHarborTelemetry(
        harborName = "Balaramgadi Fishing Harbor",
        harborNameOd = "ବଳରାମଗଡ଼ି ମତ୍ସ୍ୟ ବନ୍ଦର",
        seaCondition = "Calm to Moderate",
        waveHeightMeters = "1.1m - 1.5m",
        windSpeedKnots = "12 - 16 kts (SSE)",
        barometricPressureHpa = "1009 hPa",
        fishingSafetyRecommendation = "Safe for mechanized trawlers and motorized fiber boats.",
        isSafeToSail = true
    ),
    MarineHarborTelemetry(
        harborName = "Kasafal Sea Mouth & Landing",
        harborNameOd = "କାସାଫାଳ ମୁହାଣ ଓ ଅବତରଣ କେନ୍ଦ୍ର",
        seaCondition = "Slight Swell",
        waveHeightMeters = "0.9m - 1.3m",
        windSpeedKnots = "10 - 14 kts (South)",
        barometricPressureHpa = "1010 hPa",
        fishingSafetyRecommendation = "Good visibility. Sandbar entry clear on mid-to-high tide.",
        isSafeToSail = true
    ),
    MarineHarborTelemetry(
        harborName = "Bahabalpur Artisanal Coast",
        harborNameOd = "ବାହାବଳପୁର ପାରମ୍ପରିକ ଉପକୂଳ",
        seaCondition = "Normal Inshore",
        waveHeightMeters = "0.8m - 1.2m",
        windSpeedKnots = "11 - 15 kts",
        barometricPressureHpa = "1009 hPa",
        fishingSafetyRecommendation = "Shore-seine and country gillnetting safe.",
        isSafeToSail = true
    )
)

val MARINE_POLICE_STATIONS = listOf(
    MarinePoliceContact(
        stationName = "Kasafal Marine Police Station",
        stationNameOd = "କାସାଫାଳ ସାମୁଦ୍ରିକ ଥାନା",
        officerInChargePhone = "9438916940",
        landlinePhone = "06782-277100",
        jurisdictionCoast = "Kasafal, Bahabalpur & Panchupada River Confluence"
    ),
    MarinePoliceContact(
        stationName = "Balaramgadi Marine Police Station",
        stationNameOd = "ବଳରାମଗଡ଼ି ସାମୁଦ୍ରିକ ଥାନା",
        officerInChargePhone = "9438916938",
        landlinePhone = "06782-272050",
        jurisdictionCoast = "Budhabalanga Estuary, Chandipur Coast & Off-shore Patrolling"
    ),
    MarinePoliceContact(
        stationName = "Indian Coast Guard Station (ICGS Paradip / Dhamra)",
        stationNameOd = "ଭାରତୀୟ ତଟରକ୍ଷୀ ବାହିନୀ (୧୫୫୪)",
        officerInChargePhone = "1554",
        landlinePhone = "06722-220025",
        jurisdictionCoast = "Entire Northern Bay of Bengal Deep-Sea Search & Rescue"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarineFishermenSafetySheet(
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
                .testTag("marine_fishermen_safety_sheet")
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
                        Text("⚓", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ମତ୍ସ୍ୟଜୀବୀ ସମୁଦ୍ର ସୁରକ୍ଷା ଡେସ୍କ" else "Marine Fishermen Sea-Safety",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ଉପକୂଳ ତଟରକ୍ଷୀ ଓ ପାଣିପାଗ ଟେଲିମେଟ୍ରି" else "NavIC Sea Telemetry • Coast Guard SOS 1554",
                            fontSize = 12.sp,
                            color = Color(0xFF0284C7)
                        )
                    }
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Navigation Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFFF1F5F9),
                contentColor = Color(0xFF0284C7),
                indicator = {},
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ସାମୁଦ୍ରିକ ପାଣିପାଗ" else "Sea State Telemetry",
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
                            text = if (language == AppLanguage.ODIA) "କୋଷ୍ଟାଲ ଥାନା ଓ SOS" else "Marine Police SOS",
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
                            text = if (language == AppLanguage.ODIA) "ମାଛଧରା ନିୟମାବଳୀ" else "Ban & Conservation",
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
                        items(BALASORE_HARBOR_TELEMETRY) { telemetry ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, if (telemetry.isSafeToSail) Color(0xFF86EFAC) else Color(0xFFFCA5A5)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = if (language == AppLanguage.ODIA) telemetry.harborNameOd else telemetry.harborName,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0F172A)
                                        )
                                        Text(
                                            text = if (telemetry.isSafeToSail) "● Green Channel" else "● Caution Advised",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (telemetry.isSafeToSail) Color(0xFF166534) else Color(0xFF991B1B),
                                            modifier = Modifier
                                                .background(if (telemetry.isSafeToSail) Color(0xFFDCFCE7) else Color(0xFFFEE2E2), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .background(Color(0xFFE0F2FE), RoundedCornerShape(8.dp))
                                                .padding(8.dp)
                                        ) {
                                            Column {
                                                Text("🌊 Swell Height", fontSize = 10.sp, color = Color(0xFF0369A1), fontWeight = FontWeight.SemiBold)
                                                Text(telemetry.waveHeightMeters, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF075985))
                                            }
                                        }
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .background(Color(0xFFF0FDF4), RoundedCornerShape(8.dp))
                                                .padding(8.dp)
                                        ) {
                                            Column {
                                                Text("💨 Wind Speed", fontSize = 10.sp, color = Color(0xFF166534), fontWeight = FontWeight.SemiBold)
                                                Text(telemetry.windSpeedKnots, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF14532D))
                                            }
                                        }
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .background(Color(0xFFFEF3C7), RoundedCornerShape(8.dp))
                                                .padding(8.dp)
                                        ) {
                                            Column {
                                                Text("🧭 Barometer", fontSize = 10.sp, color = Color(0xFF92400E), fontWeight = FontWeight.SemiBold)
                                                Text(telemetry.barometricPressureHpa, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF78350F))
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "Advisory: ${telemetry.fishingSafetyRecommendation}",
                                        fontSize = 12.sp,
                                        color = Color(0xFF475569),
                                        lineHeight = 16.sp
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
                        item {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFFEF2F2),
                                border = BorderStroke(1.dp, Color(0xFFFCA5A5)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("🚨", fontSize = 28.sp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = if (language == AppLanguage.ODIA) "ସମୁଦ୍ର ମଝିରେ ଜରୁରୀ ସାହାଯ୍ୟ: ୧୫୫୪" else "Coast Guard Deep-Sea SOS: 1554",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF991B1B)
                                        )
                                        Text(
                                            text = "Toll-free 24x7 maritime search & rescue alert line for Bay of Bengal vessels in distress.",
                                            fontSize = 11.sp,
                                            color = Color(0xFF7F1D1D)
                                        )
                                    }
                                    Button(
                                        onClick = {
                                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1554"))
                                            context.startActivity(intent)
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text("1554", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        items(MARINE_POLICE_STATIONS) { station ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = if (language == AppLanguage.ODIA) station.stationNameOd else station.stationName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A)
                                    )
                                    Text(
                                        text = "📍 Coast Patrol: ${station.jurisdictionCoast}",
                                        fontSize = 12.sp,
                                        color = Color(0xFF475569),
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        OutlinedButton(
                                            onClick = {
                                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${station.officerInChargePhone}"))
                                                context.startActivity(intent)
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f),
                                            contentPadding = PaddingValues(vertical = 6.dp)
                                        ) {
                                            Icon(Icons.Default.Phone, contentDescription = "IIC", modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("IIC Mobile", fontSize = 11.sp)
                                        }
                                        Button(
                                            onClick = {
                                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${station.landlinePhone}"))
                                                context.startActivity(intent)
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                                            modifier = Modifier.weight(1f),
                                            contentPadding = PaddingValues(vertical = 6.dp)
                                        ) {
                                            Icon(Icons.Default.PhoneInTalk, contentDescription = "Landline", modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Station Desk", fontSize = 11.sp)
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
                                    text = if (language == AppLanguage.ODIA) "୬୧ ଦିନିଆ ବାର୍ଷିକ ସାମୁଦ୍ରିକ ମାଛଧରା ନିଷେଧାଦେଶ" else "61-Day Uniform Marine Fishing Ban",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF92400E)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "April 15 to June 14 every year: Breeding and fish-spawning rejuvenation period across the territorial waters of Bay of Bengal.",
                                    fontSize = 12.sp,
                                    color = Color(0xFF78350F),
                                    lineHeight = 17.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("• Traditional non-motorized wooden boats (dongas) exempted for livelihood.", fontSize = 11.sp, color = Color(0xFF78350F))
                                Text("• Mechanized trawlers strictly banned from sailing into open sea.", fontSize = 11.sp, color = Color(0xFF78350F))
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFF0FDF4),
                            border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଅଲିଭ୍ ରିଡଲେ କଇଁଛ ସଂରକ୍ଷଣ କ୍ଷେତ୍ର" else "Olive Ridley Turtle Sea Protection",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF166534)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "November 1 to May 31: 20 km offshore sanctuary zone enforced off Dhamra/Subarnarekha confluence. Mandatory use of Turtle Excluder Devices (TED) on all net gear.",
                                    fontSize = 12.sp,
                                    color = Color(0xFF14532D),
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
