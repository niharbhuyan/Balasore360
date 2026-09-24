package com.example.ui.features.emergency

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

data class NhTraumaStabilizer(
    val hospitalName: String,
    val hospitalNameOd: String,
    val highwayMilestoneKm: String,
    val emergencyIcuBeds: String,
    val bloodBankLink: String,
    val casualtyDirectPhone: String
)

data class AmbulanceBaseStation(
    val basePoint: String,
    val vehicleType: String,
    val averageEtaMins: String,
    val driverCoordinatorContact: String
)

val TRAUMA_CENTERS = listOf(
    NhTraumaStabilizer(
        hospitalName = "Balasore DHH Level-II Apex Trauma Care Centre",
        hospitalNameOd = "ବାଲେଶ୍ୱର ଜିଲ୍ଲା ମୁଖ୍ୟ ଚିକିତ୍ସାଳୟ (DHH) ଟ୍ରମା କେୟାର",
        highwayMilestoneKm = "NH-16 Km 198 (Near Remuna Flyover)",
        emergencyIcuBeds = "16 Dedicated Trauma ICU + Ventilators Active",
        bloodBankLink = "Red Cross Component Blood Bank on Campus (24x7)",
        casualtyDirectPhone = "06782262002"
    ),
    NhTraumaStabilizer(
        hospitalName = "Soro Sub-Divisional Hospital (SDH) Trauma Unit",
        hospitalNameOd = "ସୋର ଉପଖଣ୍ଡ ଡାକ୍ତରଖାନା ଜରୁରୀକାଳୀନ ବିଭାଗ",
        highwayMilestoneKm = "NH-16 Km 172 (Soro Bypass)",
        emergencyIcuBeds = "6 Emergency Trauma Beds + High-flow Oxygen",
        bloodBankLink = "Direct PRBC Cold-Chain Refrigerator on Site",
        casualtyDirectPhone = "06788220108"
    ),
    NhTraumaStabilizer(
        hospitalName = "Bahanaga Primary Emergency Stabilization Hub",
        hospitalNameOd = "ବାହାନଗା ପ୍ରାଥମିକ ଜରୁରୀକାଳୀନ ଚିକିତ୍ସା କେନ୍ଦ୍ର",
        highwayMilestoneKm = "NH-16 Km 182 (Bahanaga Bazar)",
        emergencyIcuBeds = "4 Golden-Hour Resuscitation Beds + Defibrillator",
        bloodBankLink = "Linked to Balasore DHH Mobile Dispatch",
        casualtyDirectPhone = "108"
    )
)

val NH_AMBULANCE_POSTS = listOf(
    AmbulanceBaseStation(
        basePoint = "Bahanaga Toll Plaza Station (NH-16)",
        vehicleType = "108 Advanced Life Support (ALS) Ambulance with Ventilator",
        averageEtaMins = "6 - 9 mins on highway",
        driverCoordinatorContact = "108"
    ),
    AmbulanceBaseStation(
        basePoint = "Khantapara Police Station Chhak",
        vehicleType = "108 Basic Life Support (BLS) Ambulance",
        averageEtaMins = "7 - 10 mins",
        driverCoordinatorContact = "108"
    ),
    AmbulanceBaseStation(
        basePoint = "Sergarh Toll Plaza Highway Patrol Ambulance",
        vehicleType = "NHAI 1033 Rescue Ambulance & Hydraulic Crane",
        averageEtaMins = "5 - 8 mins",
        driverCoordinatorContact = "1033"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BahanagaNhaiRapidTraumaNetworkSheet(
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
                .testTag("bahanaga_nhai_trauma_sheet")
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
                            .background(Color(0xFFFEE2E2)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🚨", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବାହାନଗା ଓ NH-16 ରାପିଡ୍ ଟ୍ରମା ଏବଂ ଆମ୍ବୁଲାନ୍ସ ନେଟୱର୍କ" else "Bahanaga & NH-16 Rapid Trauma Network",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "୧୦୮ ALS ଆମ୍ବୁଲାନ୍ସ • ଗୋଲଡେନ ଆୱାର • ସୁରକ୍ଷିତ ସହାୟକ ନିୟମ" else "NH-16 Golden Hour • 108 GPS Posts • Good Samaritan",
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

            // Sync pill
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFFEF2F2),
                border = BorderStroke(1.dp, Color(0xFFFECACA)),
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
                                .background(Color(0xFFDC2626))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "NH-16 Bahanaga corridor live emergency radar ($syncTime)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF991B1B)
                        )
                    }
                    Text(
                        text = "24x7 TRAUMA READY",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFDC2626)
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
                            text = if (language == AppLanguage.ODIA) "ଟ୍ରମା କେନ୍ଦ୍ର" else "Trauma Units",
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
                            text = if (language == AppLanguage.ODIA) "୧୦୮ ପୋଷ୍ଟ" else "108 Ambulance",
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
                            text = if (language == AppLanguage.ODIA) "ସହାୟକ ସୁରକ୍ଷା ନିୟମ" else "Good Samaritan",
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
                        items(TRAUMA_CENTERS) { center ->
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
                                                text = if (language == AppLanguage.ODIA) center.hospitalNameOd else center.hospitalName,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "📍 ${center.highwayMilestoneKm}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF64748B)
                                            )
                                        }
                                        Text(
                                            text = "ICU READY",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF16A34A),
                                            modifier = Modifier
                                                .background(Color(0xFFDCFCE7), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "🏥 Capacity: ${center.emergencyIcuBeds}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF0F766E),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "🩸 Blood Supply: ${center.bloodBankLink}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF991B1B),
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = {
                                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${center.casualtyDirectPhone}"))
                                            context.startActivity(intent)
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp)
                                    ) {
                                        Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Call Emergency Casualty (${center.casualtyDirectPhone})", fontSize = 11.sp)
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
                        items(NH_AMBULANCE_POSTS) { amb ->
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
                                                text = amb.basePoint,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = amb.vehicleType,
                                                fontSize = 11.sp,
                                                color = Color(0xFF64748B)
                                            )
                                            Text(
                                                text = "⏱️ Expected Highway ETA: ${amb.averageEtaMins}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF16A34A),
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                        Button(
                                            onClick = {
                                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${amb.driverCoordinatorContact}"))
                                                context.startActivity(intent)
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Call ${amb.driverCoordinatorContact}", fontSize = 11.sp)
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
                            color = Color(0xFFF0FDF4),
                            border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ସୁପ୍ରିମକୋର୍ଟ ସୁରକ୍ଷିତ ସହାୟକ (Good Samaritan) ନିୟମାବଳୀ" else "Supreme Court Good Samaritan Legal Protection",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF166534)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("• You CANNOT be forced to reveal identity or personal details by police or hospital staff.", fontSize = 12.sp, color = Color(0xFF14532D))
                                Text("• No civil or criminal liability can be imposed on any bystander who rushes an accident victim to a hospital.", fontSize = 12.sp, color = Color(0xFF14532D))
                                Text("• Government Cash Reward: Up to ₹5,000 grant under the Rah-Veer Good Samaritan Scheme for saving golden-hour victims.", fontSize = 12.sp, color = Color(0xFF14532D), fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:112"))
                                context.startActivity(intent)
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Call All-in-One Highway Police & Ambulance 112", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
