package com.example.ui.features.civic

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

data class CampusInstitute(
    val instituteName: String,
    val campusLocation: String,
    val topPrograms: String,
    val admissionCutoffInfo: String,
    val admissionWebsite: String
)

data class ScholarshipAlert(
    val schemeTitle: String,
    val beneficiaryGroup: String,
    val financialAssistance: String,
    val applicationStatus: String
)

val BALASORE_CAMPUSES = listOf(
    CampusInstitute(
        instituteName = "Fakir Mohan University (FMU)",
        campusLocation = "New Campus Nuapadhi & Old Campus Mallikashpur",
        topPrograms = "MCA, MBA, M.Sc (Biotechnology, Geology), LL.M, Ph.D",
        admissionCutoffInfo = "Common PG Entrance Test (CPET) Merit List (Hostel seats available)",
        admissionWebsite = "https://fmuniversity.ac.in"
    ),
    CampusInstitute(
        instituteName = "Fakir Mohan Medical College & Hospital (FM MCH)",
        campusLocation = "Remuna, Balasore (State Highway 19)",
        topPrograms = "MBBS (100 Seats/year), MD/MS Clinical Specializations, Paramedical",
        admissionCutoffInfo = "NEET UG / PG All India & State Quota Counseling",
        admissionWebsite = "https://fmmchbalasore.nic.in"
    ),
    CampusInstitute(
        instituteName = "Balasore College of Engineering & Technology (BCET)",
        campusLocation = "Sergarh, Balasore (NH-16)",
        topPrograms = "B.Tech (CSE, AI/ML, Electrical, Mechanical), M.Tech, MCA",
        admissionCutoffInfo = "OJEE & JEE Main Counseling (Campus Placement Drives)",
        admissionWebsite = "https://bcetbalasore.ac.in"
    ),
    CampusInstitute(
        instituteName = "Government ITI Balasore (Centre of Excellence)",
        campusLocation = "Station Road, Balasore",
        topPrograms = "Electrician, Fitter, CNC Operator, Mechanic Motor Vehicle",
        admissionCutoffInfo = "SAMS Odisha 10th Pass Merit Selection",
        admissionWebsite = "https://itibalasore.org"
    )
)

val SCHOLARSHIP_LIST = listOf(
    ScholarshipAlert(
        schemeTitle = "Odisha e-Medhabruti Higher Education Merit Award",
        beneficiaryGroup = "+3 Degree & Post-Graduate Professional Students",
        financialAssistance = "₹10,000 / yr (UG) & ₹20,000 / yr (PG technical)",
        applicationStatus = "Annual Portal Window (Odisha State Scholarship Portal)"
    ),
    ScholarshipAlert(
        schemeTitle = "Sudakshya Scheme for Girls in Technical Education",
        beneficiaryGroup = "Female students enrolled in Govt ITI & Polytechnics",
        financialAssistance = "100% Free Tuition + ₹1,000 / month hostel maintenance allowance",
        applicationStatus = "Continuous SAMS Verification"
    ),
    ScholarshipAlert(
        schemeTitle = "PRERANA Post-Matric Scholarship",
        beneficiaryGroup = "SC / ST / OBC / SEBC Students in Higher Education",
        financialAssistance = "Full Course Fee Reimbursement + Monthly Day-scholar/Hostel stipend",
        applicationStatus = "Annual Renewal Active"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalasoreHigherEducationHubSheet(
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
                .testTag("balasore_higher_education_sheet")
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
                            .background(Color(0xFFEFF6FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🎓", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ଉଚ୍ଚଶିକ୍ଷା ଓ କ୍ୟାମ୍ପସ ଆଡମିଶନ ହବ୍" else "Balasore Higher Education & Admission Hub",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "FMU • ମେଡିକାଲ କଲେଜ • BCET • ସରକାରୀ ଛାତ୍ରବୃତ୍ତି" else "FMU • FM MCH • BCET • Scholarships",
                            fontSize = 12.sp,
                            color = Color(0xFF0284C7)
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
                color = Color(0xFFF0F9FF),
                border = BorderStroke(1.dp, Color(0xFFBAE6FD)),
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
                                .background(Color(0xFF0284C7))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Higher Education Department SAMS verified ($syncTime)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF0369A1)
                        )
                    }
                    Text(
                        text = "ADMISSION READY",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0284C7)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFFF8FAFC),
                contentColor = Color(0xFF0284C7),
                indicator = {},
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "କଲେଜ ଓ ବିଶ୍ୱବିଦ୍ୟାଳୟ" else "Colleges",
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
                            text = if (language == AppLanguage.ODIA) "ଛାତ୍ରବୃତ୍ତି ସୂଚନା" else "Scholarships",
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
                            text = if (language == AppLanguage.ODIA) "କ୍ୟାରିୟର ପରାମର୍ଶ" else "Career Center",
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
                        items(BALASORE_CAMPUSES) { campus ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = campus.instituteName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A)
                                    )
                                    Text(
                                        text = "📍 ${campus.campusLocation}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF64748B)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "📚 Programs: ${campus.topPrograms}",
                                        fontSize = 12.sp,
                                        color = Color(0xFF0F766E),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "🎯 Selection: ${campus.admissionCutoffInfo}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF334155),
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = {
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(campus.admissionWebsite))
                                            context.startActivity(intent)
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp)
                                    ) {
                                        Icon(Icons.Default.Explore, contentDescription = "Website", modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Open Official Portal", fontSize = 11.sp)
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
                        items(SCHOLARSHIP_LIST) { sch ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = sch.schemeTitle,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "👥 Eligibility: ${sch.beneficiaryGroup}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF0284C7)
                                    )
                                    Text(
                                        text = "💰 Amount: ${sch.financialAssistance}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF15803D),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "⏳ Timeline: ${sch.applicationStatus}",
                                        fontSize = 11.sp,
                                        color = Color(0xFFB45309),
                                        modifier = Modifier.padding(top = 2.dp)
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
                            border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ଜିଲ୍ଲା ନିୟୋଜନ କାର୍ଯ୍ୟାଳୟ ଓ ମଡେଲ କ୍ୟାରିୟର କେନ୍ଦ୍ର" else "District Employment Exchange & Model Career Center",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF166534)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("• Free career aptitude testing and mock interview training for graduate jobseekers.", fontSize = 12.sp, color = Color(0xFF14532D))
                                Text("• Bi-weekly Rozgar Mela (Job Fairs) with private recruiters from NOCCI and marine export units.", fontSize = 12.sp, color = Color(0xFF14532D))
                                Text("• Location: Near Zilla School, Balasore - 756001", fontSize = 12.sp, color = Color(0xFF14532D))
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:06782262035"))
                                context.startActivity(intent)
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF15803D)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Call District Employment Officer (06782-262035)", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
