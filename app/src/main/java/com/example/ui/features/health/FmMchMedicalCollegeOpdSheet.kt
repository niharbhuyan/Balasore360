package com.example.ui.features.health

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

data class FmMchDepartmentOpd(
    val departmentName: String,
    val departmentNameOd: String,
    val opdRoomNo: String,
    val opdTimings: String,
    val opdDays: String,
    val headOfUnit: String
)

data class BskyHospitalItem(
    val hospitalName: String,
    val hospitalNameOd: String,
    val hospitalAddress: String,
    val bskyHelpdeskContact: String,
    val specialtiesCovered: String
)

val FM_MCH_OPD_ROSTER = listOf(
    FmMchDepartmentOpd(
        departmentName = "General Medicine",
        departmentNameOd = "ସାଧାରଣ ଚିକିତ୍ସା ବିଭାଗ (ମେଡିସିନ୍)",
        opdRoomNo = "Room 102 & 104, OPD Block",
        opdTimings = "09:00 AM - 02:00 PM & 04:00 PM - 06:00 PM",
        opdDays = "Monday to Saturday",
        headOfUnit = "Prof. (Dr.) S. K. Mahapatra & Team"
    ),
    FmMchDepartmentOpd(
        departmentName = "Orthopedics & Joint Care",
        departmentNameOd = "ଅସ୍ଥିଶଲ୍ୟ ଚିକିତ୍ସା (ଅର୍ଥୋପେଡିକ୍)",
        opdRoomNo = "Room 118, Ground Floor",
        opdTimings = "09:00 AM - 02:00 PM",
        opdDays = "Mon, Wed, Fri (Major OT on Tue, Thu)",
        headOfUnit = "Assoc. Prof. (Dr.) P. K. Jena"
    ),
    FmMchDepartmentOpd(
        departmentName = "Pediatrics & Neonatal Care",
        departmentNameOd = "ଶିଶୁରୋଗ ବିଭାଗ",
        opdRoomNo = "Room 112, 1st Floor",
        opdTimings = "09:00 AM - 02:00 PM",
        opdDays = "Monday to Saturday (SNCU 24x7)",
        headOfUnit = "Dr. R. N. Das (HOD Pediatrics)"
    ),
    FmMchDepartmentOpd(
        departmentName = "Cardiology & Chest Clinic",
        departmentNameOd = "ହୃଦରୋଗ ଓ ଛାତି ରୋଗ ବିଭାଗ",
        opdRoomNo = "Room 205, Super Specialty Wing",
        opdTimings = "09:30 AM - 01:30 PM",
        opdDays = "Mon, Wed, Fri (ECG & ECHO available)",
        headOfUnit = "Cardiology Specialist Consultant"
    ),
    FmMchDepartmentOpd(
        departmentName = "Obstetrics & Gynecology (OBGYN)",
        departmentNameOd = "ସ୍ତ୍ରୀ ଓ ପ୍ରସୂତୀ ରୋଗ ବିଭାଗ",
        opdRoomNo = "Room 108 & ANC Clinic",
        opdTimings = "09:00 AM - 02:00 PM",
        opdDays = "Monday to Saturday (Labor room 24x7)",
        headOfUnit = "Dr. M. Behera & Resident Team"
    ),
    FmMchDepartmentOpd(
        departmentName = "Ophthalmology (Eye Clinic)",
        departmentNameOd = "ନେତ୍ର ଚିକିତ୍ସା ବିଭାଗ (ଚକ୍ଷୁ)",
        opdRoomNo = "Room 214, 2nd Floor",
        opdTimings = "09:00 AM - 01:00 PM",
        opdDays = "Monday to Friday (Cataract surgery Tue/Thu)",
        headOfUnit = "Eye Specialist Unit"
    )
)

val BSKY_BALASORE_HOSPITALS = listOf(
    BskyHospitalItem(
        hospitalName = "Fakir Mohan MCH (Remuna Main Campus)",
        hospitalNameOd = "ଫକୀର ମୋହନ ମେଡିକାଲ କଲେଜ୍ ଓ ହସ୍ପିଟାଲ୍",
        hospitalAddress = "Remuna, Balasore (NH-16 By-pass)",
        bskyHelpdeskContact = "06782-262024",
        specialtiesCovered = "All government tertiary procedures 100% cashless"
    ),
    BskyHospitalItem(
        hospitalName = "Jyoti Hospital (Super Specialty)",
        hospitalNameOd = "ଜ୍ୟୋତି ହସ୍ପିଟାଲ୍",
        hospitalAddress = "Kuruda, Balasore",
        bskyHelpdeskContact = "06782-256070",
        specialtiesCovered = "Cardiology, Urology, Neuro-trauma & Ortho Surgery"
    ),
    BskyHospitalItem(
        hospitalName = "Balasore Nursing Home & Scan Center",
        hospitalNameOd = "ବାଲେଶ୍ୱର ନର୍ସିଂହୋମ୍",
        hospitalAddress = "Vivekananda Marg, Balasore",
        bskyHelpdeskContact = "06782-263445",
        specialtiesCovered = "General Surgery, Laparoscopy & Maternity"
    ),
    BskyHospitalItem(
        hospitalName = "St. Vincent's Hospital",
        hospitalNameOd = "ସେଣ୍ଟ ଭିନସେଣ୍ଟ ହସ୍ପିଟାଲ୍",
        hospitalAddress = "Station Road, Balasore",
        bskyHelpdeskContact = "06782-262142",
        specialtiesCovered = "General Medicine, Dialysis & Palliative Care"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FmMchMedicalCollegeOpdSheet(
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
                .testTag("fm_mch_medical_opd_sheet")
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
                        Text("🏥", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଫକୀର ମୋହନ ମେଡିକାଲ OPD ରୋଷ୍ଟର" else "FM MCH Medical College OPD",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ରେମୁଣା ମୁଖ୍ୟ କ୍ୟାମ୍ପସ • BSKY କ୍ୟାସଲେସ ହେଲ୍ପଡେସ୍କ" else "Remuna Campus • e-Sanjeevani & BSKY Desk",
                            fontSize = 12.sp,
                            color = Color(0xFFDC2626)
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
                contentColor = Color(0xFFDC2626),
                indicator = {},
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "OPD ବିଭାଗ ସୂଚୀ" else "OPD Departments",
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
                            text = if (language == AppLanguage.ODIA) "BSKY ତାଲିକାଭୁକ୍ତ" else "BSKY Hospitals",
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
                            text = if (language == AppLanguage.ODIA) "e-ସଞ୍ଜୀବନୀ" else "e-Sanjeevani",
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
                        items(FM_MCH_OPD_ROSTER) { dept ->
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
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = if (language == AppLanguage.ODIA) dept.departmentNameOd else dept.departmentName,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0F172A)
                                        )
                                        Text(
                                            text = dept.opdRoomNo,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFDC2626),
                                            modifier = Modifier
                                                .background(Color(0xFFFEE2E2), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "🕒 Timings: ${dept.opdTimings}",
                                        fontSize = 12.sp,
                                        color = Color(0xFF0284C7),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "📅 Schedule: ${dept.opdDays}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF475569)
                                    )
                                    Text(
                                        text = "👨‍⚕️ Faculty Unit: ${dept.headOfUnit}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF64748B)
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
                        items(BSKY_BALASORE_HOSPITALS) { hosp ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
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
                                                text = if (language == AppLanguage.ODIA) hosp.hospitalNameOd else hosp.hospitalName,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "📍 ${hosp.hospitalAddress}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF475569)
                                            )
                                            Text(
                                                text = "🩺 ${hosp.specialtiesCovered}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF166534),
                                                modifier = Modifier.padding(top = 2.dp)
                                            )
                                        }
                                        Button(
                                            onClick = {
                                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${hosp.bskyHelpdeskContact}"))
                                                context.startActivity(intent)
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Helpdesk", fontSize = 11.sp)
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
                            color = Color(0xFFEFF6FF),
                            border = BorderStroke(1.dp, Color(0xFF93C5FD)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "e-ସଞ୍ଜୀବନୀ ମାଗଣା ଡାକ୍ତର ପରାମର୍ଶ" else "e-Sanjeevani Free Teleconsultation",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E40AF)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Government of India & Odisha Health Dept online OPD service. Consult specialist MBBS/MD doctors via video call from home at zero cost. Prescription generated online can be shown at local PHC/CHC for free medicines.",
                                    fontSize = 12.sp,
                                    color = Color(0xFF1E3A8A),
                                    lineHeight = 17.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://esanjeevani.mohfw.gov.in"))
                                        context.startActivity(intent)
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D4ED8)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.VideoCall, contentDescription = "Consult")
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Open e-Sanjeevani Patient Portal", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
