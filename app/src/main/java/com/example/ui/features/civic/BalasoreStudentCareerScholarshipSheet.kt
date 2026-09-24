package com.example.ui.features.civic

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

data class OdishaScholarshipScheme(
    val schemeName: String,
    val schemeNameOd: String,
    val financialAssistance: String,
    val targetGroup: String,
    val eligibilityCriteria: String,
    val portalUrl: String
)

data class BalasoreCareerCenter(
    val centerName: String,
    val centerNameOd: String,
    val address: String,
    val contactPhone: String,
    val keyServices: String
)

val ODISHA_SCHOLARSHIP_SCHEMES = listOf(
    OdishaScholarshipScheme(
        schemeName = "e-Medhabruti (Higher Education Merit)",
        schemeNameOd = "ଇ-ମେଧାବୃତ୍ତି ସ୍କଲାରସିପ୍",
        financialAssistance = "₹10,000 / year (UG) & ₹20,000 / year (PG & Tech)",
        targetGroup = "+3 Degree, Engineering, Medical, & PG students",
        eligibilityCriteria = "Minimum 60% in +2 / Degree. Family income under ₹6 Lakh/yr.",
        portalUrl = "https://scholarship.odisha.gov.in"
    ),
    OdishaScholarshipScheme(
        schemeName = "Sudakshya Scheme for Girls (ITI/Diploma)",
        schemeNameOd = "ସୁଦକ୍ଷା ବାଳିକା ବୈଷୟିକ ଯୋଜନା",
        financialAssistance = "Full Course Fee Waiver + ₹3,000/yr maintenance",
        targetGroup = "Female students enrolled in Govt/Pvt ITIs and Polytechnics",
        eligibilityCriteria = "Girl student native to Odisha pursuing technical trades.",
        portalUrl = "https://scholarship.odisha.gov.in"
    ),
    OdishaScholarshipScheme(
        schemeName = "PRERANA Post-Matric Scholarship",
        schemeNameOd = "ପ୍ରେରଣା ମାଟ୍ରିକୋତ୍ତର ବୃତ୍ତି (SC/ST/OBC)",
        financialAssistance = "Full Tuition Fee + Monthly Hosteller/Day Scholar stipend",
        targetGroup = "SC, ST, OBC/SEBC and EBC students",
        eligibilityCriteria = "Enrolled in recognized post-matric course. Annual income limits apply.",
        portalUrl = "https://scholarship.odisha.gov.in"
    ),
    OdishaScholarshipScheme(
        schemeName = "Kalia Chhatra Brutti (Farmer's Children)",
        schemeNameOd = "କାଳିଆ ଛାତ୍ର ବୃତ୍ତି ଯୋଜନା",
        financialAssistance = "100% Technical/Professional education expense coverage",
        targetGroup = "Children of registered KALIA farmer beneficiaries",
        eligibilityCriteria = "Admission into Govt MBBS, BDS, B.Tech, MBA, MCA, ITI, or Diploma.",
        portalUrl = "https://scholarship.odisha.gov.in"
    )
)

val BALASORE_CAREER_CENTERS = listOf(
    BalasoreCareerCenter(
        centerName = "District Employment Exchange & Model Career Center",
        centerNameOd = "ଜିଲ୍ଲା ନିୟୋଜନ କାର୍ଯ୍ୟାଳୟ",
        address = "Near ITI Square, Balasore Town",
        contactPhone = "06782-262055",
        keyServices = "National Career Service (NCS) registration, Rozgar Melas & Skill counseling"
    ),
    BalasoreCareerCenter(
        centerName = "Fakir Mohan University Career Counseling Cell",
        centerNameOd = "ଫକୀର ମୋହନ ବିଶ୍ୱବିଦ୍ୟାଳୟ କ୍ୟାରିୟର ସେଲ୍",
        address = "Nuapadhi Campus, Balasore",
        contactPhone = "06782-275787",
        keyServices = "UGC-NET/GATE guidance, campus placement drives, and civil service seminars"
    ),
    BalasoreCareerCenter(
        centerName = "District Central Library & Reading Room",
        centerNameOd = "ଜିଲ୍ଲା କେନ୍ଦ୍ରୀୟ ପାଠାଗାର",
        address = "Near Zilla Parishad Bhavan, Balasore",
        contactPhone = "06782-262100",
        keyServices = "Free competitive exam books (UPSC/OPSC/SSC/Banking) & quiet study hall"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalasoreStudentCareerScholarshipSheet(
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
                .testTag("balasore_student_scholarship_sheet")
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
                        Text("🎓", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଛାତ୍ରବୃତ୍ତି ଓ କ୍ୟାରିୟର ସହାୟତା ଡେସ୍କ" else "Scholarships & Career Guidance",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ମେଧାବୃତ୍ତି • କାଳିଆ ବୃତ୍ତି • ଜିଲ୍ଲା ନିୟୋଜନ କାର୍ଯ୍ୟାଳୟ" else "Odisha State Scholarship Portal • Model Career Center",
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
                            text = if (language == AppLanguage.ODIA) "ଛାତ୍ରବୃତ୍ତି ସୂଚୀ" else "Scholarships",
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
                            text = if (language == AppLanguage.ODIA) "ନିୟୋଜନ କେନ୍ଦ୍ର" else "Career Centers",
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
                        items(ODISHA_SCHOLARSHIP_SCHEMES) { item ->
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
                                        Text(
                                            text = if (language == AppLanguage.ODIA) item.schemeNameOd else item.schemeName,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0F172A),
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = item.financialAssistance,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF166534),
                                            modifier = Modifier
                                                .background(Color(0xFFDCFCE7), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "🎯 Target: ${item.targetGroup}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF0284C7),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "📋 Eligibility: ${item.eligibilityCriteria}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF475569)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedButton(
                                        onClick = {
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.portalUrl))
                                            context.startActivity(intent)
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth(),
                                        contentPadding = PaddingValues(vertical = 6.dp)
                                    ) {
                                        Text("Apply on Odisha State Scholarship Portal ↗", fontSize = 11.sp)
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
                        items(BALASORE_CAREER_CENTERS) { center ->
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
                                                text = if (language == AppLanguage.ODIA) center.centerNameOd else center.centerName,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "📍 ${center.address}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF475569)
                                            )
                                            Text(
                                                text = "💼 Services: ${center.keyServices}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF0284C7),
                                                modifier = Modifier.padding(top = 2.dp)
                                            )
                                        }
                                        Button(
                                            onClick = {
                                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${center.contactPhone}"))
                                                context.startActivity(intent)
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Contact", fontSize = 11.sp)
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
