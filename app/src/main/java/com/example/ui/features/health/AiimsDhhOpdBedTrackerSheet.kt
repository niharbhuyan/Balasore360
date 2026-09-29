package com.example.ui.features.health

import android.content.Context
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.daily.DailyBalasorePulse
import com.example.data.model.AppLanguage
import com.example.ui.theme.*

data class OpdDoctorChamber(
    val id: String,
    val department: String,
    val departmentOd: String,
    val hospital: String,
    val doctorName: String,
    val timings: String,
    val days: String,
    val roomNumber: String,
    val status: String,
    val isAvailableToday: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiimsDhhOpdBedTrackerSheet(
    language: AppLanguage,
    dailyPulse: DailyBalasorePulse,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val isOdia = language == AppLanguage.ODIA
    var selectedHospitalFilter by remember { mutableStateOf("All") }

    val chambers = remember {
        listOf(
            OpdDoctorChamber(
                id = "doc_1",
                department = "Cardiology & Heart Care",
                departmentOd = "ହୃଦରୋଗ ବିଭାଗ (କାର୍ଡିଓଲୋଜି)",
                hospital = "AIIMS Satellite Centre",
                doctorName = "Dr. Anirban Das, MD (AIIMS)",
                timings = "09:00 AM - 01:30 PM",
                days = "Mon, Wed, Fri",
                roomNumber = "OPD Room 104",
                status = "Chamber Open 🟢",
                isAvailableToday = true
            ),
            OpdDoctorChamber(
                id = "doc_2",
                department = "Neurology & Brain Spine",
                departmentOd = "ସ୍ନାୟୁରୋଗ ବିଭାଗ (ନ୍ୟୁରୋଲୋଜି)",
                hospital = "AIIMS Satellite Centre",
                doctorName = "Dr. Soumya Ranjan Nayak, DM",
                timings = "10:00 AM - 02:00 PM",
                days = "Tue, Thu, Sat",
                roomNumber = "OPD Room 108",
                status = "Chamber Open 🟢",
                isAvailableToday = true
            ),
            OpdDoctorChamber(
                id = "doc_3",
                department = "General Medicine & Dialysis",
                departmentOd = "ସାଧାରଣ ଔଷଧ ଓ ଡାଏଲିସିସ୍",
                hospital = "DHH Balasore Campus",
                doctorName = "Dr. P. K. Mohapatra, Specialist",
                timings = "08:30 AM - 01:00 PM & 04:00 PM - 06:00 PM",
                days = "Monday to Saturday",
                roomNumber = "Chamber 12, Ground Floor",
                status = "Active Consultation 🟢",
                isAvailableToday = true
            ),
            OpdDoctorChamber(
                id = "doc_4",
                department = "Pediatrics & Neonatal Care",
                departmentOd = "ଶିଶୁରୋଗ ବିଭାଗ (ପେଡିଆଟ୍ରିକ୍ସ)",
                hospital = "DHH Balasore Campus",
                doctorName = "Dr. Sunita Tripathy, DCH",
                timings = "09:00 AM - 02:00 PM",
                days = "Daily (24x7 Emergency Covered)",
                roomNumber = "MCH Block Room 04",
                status = "Active Consultation 🟢",
                isAvailableToday = true
            ),
            OpdDoctorChamber(
                id = "doc_5",
                department = "Orthopedics & Joint Replacement",
                departmentOd = "ଅସ୍ଥିଶଲ୍ୟ ଚିକିତ୍ସା (ଅର୍ଥୋପେଡିକ୍)",
                hospital = "AIIMS Satellite Centre",
                doctorName = "Dr. B. C. Panda, MS (Ortho)",
                timings = "09:30 AM - 01:30 PM",
                days = "Mon, Tue, Thu, Fri",
                roomNumber = "Room 112",
                status = "Chamber Open 🟢",
                isAvailableToday = true
            )
        )
    }

    val filteredChambers = remember(selectedHospitalFilter) {
        if (selectedHospitalFilter == "All") chambers
        else chambers.filter { it.hospital.contains(selectedHospitalFilter, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag("aiims_dhh_opd_bed_tracker_sheet")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFEE2E2),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🏥", fontSize = 24.sp)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (isOdia) "AIIMS ଓ DHH ବାଲେଶ୍ୱର OPD ଓ ବେଡ୍ ଟ୍ରାକର୍" else "AIIMS & DHH OPD & Bed Pulse",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = BentoSlate900
                    )
                    Text(
                        text = if (isOdia) "ବାସ୍ତବ ସମୟ ଡାକ୍ତର ତାଲିକା • ଆଇସିୟୁ ବେଡ୍ ଓ ବ୍ଲଡ୍ ବ୍ୟାଙ୍କ" else "Real-time Specialist Chambers • ICU Beds & Blood Grid",
                        style = MaterialTheme.typography.bodySmall,
                        color = BentoSlate500
                    )
                }
            }
            IconButton(
                onClick = onClose,
                modifier = Modifier.testTag("btn_close_aiims_opd_sheet")
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = BentoSlate600)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Live Auto-Updated Metric Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("AIIMS OPD Wait", fontSize = 11.sp, color = Color(0xFF15803D), fontWeight = FontWeight.Bold)
                    Text("~${dailyPulse.aiimsSatelliteCentreQueueWaitMins} mins", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = BentoSlate900)
                    Text("${dailyPulse.aiimsOpdSpecialistsAvailable} Doctors Active", fontSize = 11.sp, color = BentoSlate600)
                }
            }
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                border = BorderStroke(1.dp, Color(0xFFBFDBFE)),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("DHH ICU Vacant", fontSize = 11.sp, color = Color(0xFF1D4ED8), fontWeight = FontWeight.Bold)
                    Text("${dailyPulse.dhhIcuBedsAvailable} Beds Open", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = BentoSlate900)
                    Text("${dailyPulse.dhhBloodUnitsStored} Blood Units", fontSize = 11.sp, color = BentoSlate600)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Emergency Call Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:108") }
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Emergency, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Dial 108 Ambulance", fontSize = 12.sp)
            }

            OutlinedButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:06782262100") }
                    context.startActivity(intent)
                },
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, OceanBlue),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Call, contentDescription = null, tint = OceanBlue, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("AIIMS Helpdesk", fontSize = 12.sp, color = OceanBlue)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Hospital Filter Chips
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("All", "AIIMS Satellite Centre", "DHH Balasore").forEach { hosp ->
                FilterChip(
                    selected = selectedHospitalFilter == hosp,
                    onClick = { selectedHospitalFilter = hosp },
                    label = { Text(if (hosp == "All") "All Centers" else hosp, fontSize = 12.sp) }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // OPD Chambers List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredChambers, key = { it.id }) { chamber ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    border = BorderStroke(1.dp, BentoSlate200),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isOdia) chamber.departmentOd else chamber.department,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = BentoSlate900
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFDCFCE7),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = chamber.status,
                                    fontSize = 11.sp,
                                    color = Color(0xFF15803D),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "👨‍⚕️ ${chamber.doctorName} • ${chamber.hospital}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BentoSlate800
                        )
                        Text(
                            text = "🕒 ${chamber.timings} (${chamber.days})",
                            fontSize = 11.sp,
                            color = BentoSlate600
                        )
                        Text(
                            text = "📍 Location: ${chamber.roomNumber}",
                            fontSize = 11.sp,
                            color = BentoSlate500
                        )
                    }
                }
            }
        }
    }
}
