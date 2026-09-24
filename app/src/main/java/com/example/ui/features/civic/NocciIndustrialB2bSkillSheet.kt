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

data class NocciEnterprise(
    val unitName: String,
    val sector: String,
    val industrialZone: String,
    val productsManufactured: String,
    val hrContactEmail: String
)

data class ApprenticeOpportunity(
    val jobRole: String,
    val qualification: String,
    val stipend: String,
    val employer: String,
    val deadline: String
)

val NOCCI_ENTERPRISES = listOf(
    NocciEnterprise(
        unitName = "NOCCI Business Park & Expo Centre",
        sector = "Chamber of Commerce & Industrial Facilitation",
        industrialZone = "Chhanpur Industrial Estate, Balasore",
        productsManufactured = "MSME incubation, testing labs, trade conferences, arbitration",
        hrContactEmail = "info@nocci.in"
    ),
    NocciEnterprise(
        unitName = "Emami Paper Mills Ltd.",
        sector = "Paper & Packaging Board Manufacturing",
        industrialZone = "Balgopalpur Industrial Area, Remuna",
        productsManufactured = "Newsprint, high-grade multi-layer packaging board",
        hrContactEmail = "balasore.hr@emamipaper.com"
    ),
    NocciEnterprise(
        unitName = "Central Institute of Petrochemicals Engineering (CIPET)",
        sector = "Polymers & Plastic Engineering Technology",
        industrialZone = "IDCO Somnathpur Industrial Area",
        productsManufactured = "Tool room training, polymer testing, CAD/CAM design incubation",
        hrContactEmail = "balasore@cipet.gov.in"
    ),
    NocciEnterprise(
        unitName = "Balasore Alloys Limited",
        sector = "Ferro-Alloys & Metallurgy",
        industrialZone = "Balgopalpur, Balasore",
        productsManufactured = "High carbon ferro-chrome for stainless steel production",
        hrContactEmail = "corporate@balasorealloys.com"
    )
)

val SKILL_OPPORTUNITIES = listOf(
    ApprenticeOpportunity(
        jobRole = "Graduate & Diploma Apprentice (Chemical & Mechanical)",
        qualification = "Diploma / B.Tech (Mech, Chem, Electrical)",
        stipend = "₹12,500 - ₹15,000 / month",
        employer = "Emami Paper Mills & Polymer Clusters",
        deadline = "Walk-in & NATS Portal"
    ),
    ApprenticeOpportunity(
        jobRole = "Plastic Processing Machine Operator (CNC & Injection Moulding)",
        qualification = "ITI / 10th + CIPET Certificate",
        stipend = "₹10,500 / month + Subsidized Canteen",
        employer = "CIPET Somnathpur Industrial Cluster",
        deadline = "Batch Intake Monthly"
    ),
    ApprenticeOpportunity(
        jobRole = "Quality Testing Lab Technician (Marine Agro Processing)",
        qualification = "B.Sc (Microbiology / Chemistry)",
        stipend = "₹14,000 / month",
        employer = "MPEDA Shrimp Export Processing Units, Balasore",
        deadline = "Immediate Requirement"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NocciIndustrialB2bSkillSheet(
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
                .testTag("nocci_industrial_sheet")
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
                        Text("🏭", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ନୋସି ଶିଳ୍ପ କରିଡର ଓ ଦକ୍ଷତା ନିଯୁକ୍ତି ଡେସ୍କ" else "NOCCI Industrial Corridor & B2B Hub",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଛାନପୁର • ବାଲଗୋପାଳପୁର • CIPET • ଶିକ୍ଷାନବିସ" else "Chhanpur • Balgopalpur • CIPET • Apprenticeships",
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
                                .background(Color(0xFFF59E0B))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "NOCCI & DIC Balasore B2B network synced at $syncTime",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF92400E)
                        )
                    }
                    Text(
                        text = "MSME ACTIVE",
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
                            text = if (language == AppLanguage.ODIA) "ଶିଳ୍ପ ସଂସ୍ଥା" else "Enterprises",
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
                            text = if (language == AppLanguage.ODIA) "ଅପ୍ରେଣ୍ଟିସସିପ୍" else "Apprenticeships",
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
                            text = if (language == AppLanguage.ODIA) "ଡିଆଇସି ସହାୟତା" else "DIC Balasore",
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
                        items(NOCCI_ENTERPRISES) { unit ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = unit.unitName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A)
                                    )
                                    Text(
                                        text = "📍 ${unit.industrialZone}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF64748B)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "⚙️ Sector: ${unit.sector}",
                                        fontSize = 12.sp,
                                        color = Color(0xFF0F766E),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "📦 Scope: ${unit.productsManufactured}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF334155),
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "✉️ ${unit.hrContactEmail}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF2563EB),
                                        fontWeight = FontWeight.SemiBold
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
                        items(SKILL_OPPORTUNITIES) { job ->
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
                                                text = job.jobRole,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "🏢 ${job.employer}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF64748B)
                                            )
                                        }
                                        Text(
                                            text = job.stipend,
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
                                        text = "🎓 Qualification: ${job.qualification}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF334155)
                                    )
                                    Text(
                                        text = "⏳ Timeline: ${job.deadline}",
                                        fontSize = 11.sp,
                                        color = Color(0xFFB45309),
                                        fontWeight = FontWeight.Medium
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
                            color = Color(0xFFF8FAFC),
                            border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଜିଲ୍ଲା ଶିଳ୍ପ କେନ୍ଦ୍ର (DIC) ବାଲେଶ୍ୱର ସୁବିଧା" else "District Industries Centre (DIC) Balasore",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("• PMEGP (Prime Minister Employment Generation) Subsidy: Up to 35% capital grant", fontSize = 12.sp, color = Color(0xFF334155))
                                Text("• Mukhyamantri Karma Tatparata (MUKTA): Urban micro-enterprise support", fontSize = 12.sp, color = Color(0xFF334155))
                                Text("• Single Window Clearance: Industrial land allotment through IDCO Balasore", fontSize = 12.sp, color = Color(0xFF334155))
                                Text("• Office Location: Near Collectorate, Balasore - 756001", fontSize = 12.sp, color = Color(0xFF334155))
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:06782262078"))
                                context.startActivity(intent)
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Call General Manager, DIC Balasore (06782-262078)", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
