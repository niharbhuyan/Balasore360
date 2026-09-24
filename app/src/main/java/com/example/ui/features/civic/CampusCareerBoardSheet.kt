package com.example.ui.features.civic

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import com.example.ui.theme.*

data class CampusNotice(
    val id: String,
    val titleEn: String,
    val titleOd: String,
    val institution: String, // FM University, FM Auto College, BCET, DRDO Apprenticeship
    val category: String, // Exams, Jobs & Apprenticeship, Shuttle, Scholarship
    val datePosted: String,
    val summary: String,
    val externalUrl: String = "https://fmuniversity.nic.in",
    val isUrgent: Boolean = false
)

data class ShuttleBusSlot(
    val slotTime: String,
    val origin: String,
    val destination: String,
    val busNumber: String,
    val viaStops: String
)

val CAMPUS_NOTICES = listOf(
    CampusNotice(
        id = "notice_fmu_sem4_exam",
        titleEn = "FMU UG & PG Semester IV Examination Form Fill-up Schedule",
        titleOd = "ଫକୀର ମୋହନ ବିଶ୍ୱବିଦ୍ୟାଳୟ ୪ର୍ଥ ସେମିଷ୍ଟାର ଫର୍ମ ପୂରଣ ବିଜ୍ଞପ୍ତି",
        institution = "Fakir Mohan University (FMU)",
        category = "Exams",
        datePosted = "Today",
        summary = "Online regular & back paper registration opens without fine till 15th of next month. Admit cards issued via student portal.",
        externalUrl = "https://fmuniversity.nic.in/examinations",
        isUrgent = true
    ),
    CampusNotice(
        id = "notice_drdo_apprentice",
        titleEn = "DRDO ITR Chandipur Trade & Graduate Apprentice Walk-in 2026",
        titleOd = "ଡିଆରଡିଓ ଆଇଟିଆର୍ ଚାନ୍ଦିପୁର ଶିକ୍ଷାନବିସ (Apprentice) ନିଯୁକ୍ତି",
        institution = "DRDO ITR Chandipur",
        category = "Jobs & Apprenticeship",
        datePosted = "Yesterday",
        summary = "Stipend up to ₹9,000/month for B.Tech / Diploma / ITI holders in Electronics, Computer Science, and Mechanical disciplines.",
        externalUrl = "https://drdo.gov.in/careers",
        isUrgent = true
    ),
    CampusNotice(
        id = "notice_nocci_placement",
        titleEn = "Balasore NOCCI Plastic & Engineering Park Mega Job Drive",
        titleOd = "ନୋସି (NOCCI) ବାଲେଶ୍ୱର ନିଯୁକ୍ତି ମେଳା ବିଜ୍ଞପ୍ତି",
        institution = "NOCCI Balasore & Govt ITI",
        category = "Jobs & Apprenticeship",
        datePosted = "2 days ago",
        summary = "Over 45 manufacturing and polymer industries in Balasore industrial corridor recruiting 350+ fresh technicians and supervisors.",
        externalUrl = "https://balasore.nic.in"
    ),
    CampusNotice(
        id = "notice_medhabruti_scholarship",
        titleEn = "Odisha e-Medhabruti & PRERANA Scholarship Verification",
        titleOd = "ଇ-ମେଧାବୃତ୍ତି ଓ ପ୍ରେରଣା ଛାତ୍ରବୃତ୍ତି କାଗଜପତ୍ର ଯାଞ୍ଚ",
        institution = "FM Autonomous College & FMU",
        category = "Scholarship",
        datePosted = "3 days ago",
        summary = "Students eligible for Junior, Senior & Technical scholarships must submit verified bank passbooks and Aadhaar seeding at Dean's office.",
        externalUrl = "https://scholarship.odisha.gov.in"
    )
)

val FMU_SHUTTLE_SCHEDULE = listOf(
    ShuttleBusSlot("07:30 AM", "Sahadevkhunta Bus Stand", "FMU New Campus Nuapadhi", "Bus #1 (North Route)", "Phandi Chhak ➔ ITI ➔ Kuruda Bypass"),
    ShuttleBusSlot("08:15 AM", "Balasore Railway Station", "FMU New Campus Nuapadhi", "Bus #2 (Express)", "Station Circle ➔ Motiganj ➔ Nuapadhi"),
    ShuttleBusSlot("09:00 AM", "Vyasa Vihar Old Campus", "FMU New Campus Nuapadhi", "Bus #3 (Inter-Campus)", "Old Campus ➔ Remuna Golei ➔ New Campus"),
    ShuttleBusSlot("01:30 PM", "FMU New Campus", "Balasore Station & Town", "Bus #1 (Return)", "Nuapadhi ➔ Kuruda ➔ Station"),
    ShuttleBusSlot("04:30 PM", "FMU New Campus", "Sahadevkhunta Bus Stand", "Bus #2 (Evening Peak)", "New Campus ➔ Phandi Chhak ➔ Bus Stand")
)

/**
 * Balasore Campus & Career Board Sheet.
 * Centralized portal for University & College notices, shuttle bus timetables,
 * DRDO/industrial apprenticeships, and student scholarships.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampusCareerBoardSheet(
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf("All Notices") } // "All Notices", "Jobs & Apprenticeship", "Shuttle Timings", "Exams"

    val tabOptions = listOf("All Notices", "Jobs & Apprenticeship", "Shuttle Timings", "Exams")

    val filteredNotices = remember(selectedTab) {
        if (selectedTab == "All Notices") {
            CAMPUS_NOTICES
        } else {
            CAMPUS_NOTICES.filter { it.category == selectedTab }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .testTag("campus_career_board_sheet")
        ) {
            // Header Bar
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
                        Text(text = "🎓", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର କ୍ୟାମ୍ପସ୍ ଓ କ୍ୟାରିୟର ବୋର୍ଡ" else "Balasore Campus & Career Board",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଏଫଏମୟୁ, ଡିଆରଡିଓ ଶିକ୍ଷାନବିସ ଓ ବିଶ୍ୱବିଦ୍ୟାଳୟ ବସ୍ ସୂଚୀ" else "FMU, DRDO Apprenticeships & Campus Shuttle Hub",
                            style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate500)
                        )
                    }
                }
                IconButton(onClick = onClose) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Navigation Filter Tabs
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.testTag("campus_tabs_row")
            ) {
                items(tabOptions) { tab ->
                    FilterChip(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        label = { Text(tab, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = OceanBlue,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.88f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // If Shuttle Timings selected or on All Notices, show shuttle bus card
                if (selectedTab == "Shuttle Timings" || selectedTab == "All Notices") {
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                            border = BorderStroke(1.dp, Color(0xFFBBF7D0))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = "🚌", fontSize = 18.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (language == AppLanguage.ODIA) "ଏଫଏମୟୁ ନୂଆ କ୍ୟାମ୍ପସ୍ ବସ୍ ସମୟ ସାରଣୀ" else "FMU Campus Shuttle Schedule",
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF166534)
                                            )
                                        )
                                    }
                                    Surface(
                                        color = Color(0xFFDCFCE7),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "RUNNING DAILY",
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 9.sp,
                                                color = Color(0xFF15803D)
                                            )
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                FMU_SHUTTLE_SCHEDULE.forEach { slot ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            color = Color(0xFF15803D),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = slot.slotTime,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "${slot.origin} ➔ ${slot.destination}",
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = BentoSlate900
                                                )
                                            )
                                            Text(
                                                text = "${slot.busNumber} • Via: ${slot.viaStops}",
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = BentoSlate500,
                                                    fontSize = 11.sp
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // If not exclusively Shuttle Timings, show academic/job notices
                if (selectedTab != "Shuttle Timings") {
                    item {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ସଦ୍ୟତମ ଶିକ୍ଷା ଓ ନିଯୁକ୍ତି ବିଜ୍ଞପ୍ତି" else "Latest Academic & Placement Bulletins",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BentoSlate900
                            )
                        )
                    }

                    items(filteredNotices) { notice ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = notice.institution,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0369A1)
                                            )
                                        )
                                        Text(
                                            text = if (language == AppLanguage.ODIA) notice.titleOd else notice.titleEn,
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = BentoSlate900
                                            )
                                        )
                                    }

                                    if (notice.isUrgent) {
                                        Surface(
                                            color = Color(0xFFFEE2E2),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = "URGENT",
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 9.sp,
                                                    color = Color(0xFFDC2626)
                                                )
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = notice.summary,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = BentoSlate600,
                                        lineHeight = 18.sp
                                    )
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "📅 Posted: ${notice.datePosted}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = BentoSlate400,
                                            fontSize = 11.sp
                                        )
                                    )

                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Button(
                                            onClick = {
                                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(notice.externalUrl))
                                                context.startActivity(intent)
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = OceanBlue),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Icon(imageVector = Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(12.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Open Portal", fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Student Helpline Directory
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "📞 Student Desks & Help Contacts",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BentoSlate900
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "• FMU Controller of Examinations: 06782-275902\n• FMU Proctor & Anti-Ragging Cell: 06782-275858\n• District Employment Exchange: 06782-262145\n• DRDO ITR Recruitment Cell: 06782-272144",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = BentoSlate700,
                                    lineHeight = 20.sp
                                )
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}
