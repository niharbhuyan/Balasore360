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

data class HomeCareService(
    val serviceTitle: String,
    val serviceTitleOd: String,
    val providerAgency: String,
    val operationalArea: String,
    val tariffRate: String,
    val contactPhone: String
)

data class DoorstepDiagnosticLab(
    val labName: String,
    val turnaroundHours: String,
    val keyProfilesOffered: String,
    val collectionHotline: String
)

val SENIOR_CARE_SERVICES = listOf(
    HomeCareService(
        serviceTitle = "Certified Geriatric Home Nursing & Attendant",
        serviceTitleOd = "ପ୍ରମାଣିତ ବରିଷ୍ଠ ନାଗରିକ ସେବାକାରୀ ଓ ନର୍ସିଂ",
        providerAgency = "Balasore Red Cross & Seva Setu Caregivers",
        operationalArea = "Balasore Municipality, Gopalgaon, Remuna",
        tariffRate = "₹600 - ₹900 / 12-hr shift (Post-op, mobility & medication)",
        contactPhone = "06782262102"
    ),
    HomeCareService(
        serviceTitle = "Home Physiotherapy & Stroke Rehabilitation",
        serviceTitleOd = "ଘରେ ଫିଜିଓଥେରାପି ଓ ପାରାଲିସିସ୍ ପୁନର୍ବାସ",
        providerAgency = "FM MCH Associated Physical Therapy Guild",
        operationalArea = "Town & Nilagiri Road Corridor",
        tariffRate = "₹350 - ₹500 / session",
        contactPhone = "9437255109"
    ),
    HomeCareService(
        serviceTitle = "Home Oxygen Concentrator & BiPAP Rental",
        serviceTitleOd = "ଅକ୍ସିଜେନ କନସେନଟ୍ରେଟର ଓ ବାୟପ୍ୟାପ୍ ଭଡ଼ା",
        providerAgency = "Sanjeevani Med-Equip Balasore",
        operationalArea = "Full District Doorstep Delivery",
        tariffRate = "₹3,500 - ₹5,000 / month (5L / 10L units with backup cylinder)",
        contactPhone = "9861044320"
    )
)

val DOORSTEP_LABS = listOf(
    DoorstepDiagnosticLab(
        labName = "District Headquarter Hospital (DHH) Home Sample Desk",
        turnaroundHours = "Same Day Online Reports via Nidan Portal",
        keyProfilesOffered = "CBC, HbA1c, Lipid Profile, LFT, KFT (Completely Free under Nidan)",
        collectionHotline = "06782262002"
    ),
    DoorstepDiagnosticLab(
        labName = "Apollo Diagnostics & Lifeline Pathology Balasore",
        turnaroundHours = "Reports within 4 - 6 Hours via WhatsApp",
        keyProfilesOffered = "Senior Comprehensive Master Health Checkup, Vitamin D3 & B12, Cardiac Enzymes",
        collectionHotline = "06782263300"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeniorCitizenSevaSetuSheet(
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
                .testTag("senior_citizen_seva_setu_sheet")
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
                        Text("🏥", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ସେବା ସେତୁ: ବରିଷ୍ଠ ନାଗରିକ ସହାୟତା ଡେସ୍କ" else "Seva Setu: Senior Citizen Care Desk",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଘରୋଇ ନର୍ସିଂ • ରକ୍ତ ପରୀକ୍ଷା • ବରିଷ୍ଠ ପୋଲିସ୍ ସୁରକ୍ଷା" else "Home Nursing • Doorstep Labs • Police Beat Registry",
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

            // Sync pill
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
                                .background(Color(0xFF16A34A))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Senior care network active in Balasore ($syncTime)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF166534)
                        )
                    }
                    Text(
                        text = "GERIATRIC ACTIVE",
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
                            text = if (language == AppLanguage.ODIA) "ଘରୋଇ ସେବା" else "Home Care",
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
                            text = if (language == AppLanguage.ODIA) "ରକ୍ତ ପରୀକ୍ଷା" else "Doorstep Labs",
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
                            text = if (language == AppLanguage.ODIA) "ପୋଲିସ୍ ସହାୟତା" else "Senior Police",
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
                        items(SENIOR_CARE_SERVICES) { srv ->
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
                                                text = if (language == AppLanguage.ODIA) srv.serviceTitleOd else srv.serviceTitle,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "🏢 Provider: ${srv.providerAgency}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF64748B)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "📍 Coverage: ${srv.operationalArea}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF0F766E),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "💳 Tariff: ${srv.tariffRate}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF15803D),
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = {
                                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${srv.contactPhone}"))
                                            context.startActivity(intent)
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp)
                                    ) {
                                        Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Book Service / Call", fontSize = 11.sp)
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
                        items(DOORSTEP_LABS) { lab ->
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
                                                text = lab.labName,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "⏱️ ${lab.turnaroundHours}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF0284C7),
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Text(
                                                text = "🔬 Profiles: ${lab.keyProfilesOffered}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF334155),
                                                modifier = Modifier.padding(top = 2.dp)
                                            )
                                        }
                                        Button(
                                            onClick = {
                                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${lab.collectionHotline}"))
                                                context.startActivity(intent)
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F766E)),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Call Lab", fontSize = 11.sp)
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
                            border = BorderStroke(1.dp, Color(0xFFBFDBFE)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ଜିଲ୍ଲା ପୋଲିସ୍ ବରିଷ୍ଠ ନାଗରିକ ସୁରକ୍ଷା ପଞ୍ଜିକରଣ" else "Balasore Police Senior Citizen Beat Registry",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E40AF)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("• Regular Beat Visits: Local Town / Sahadevkhunta beat police officers visit registered elderly households weekly.", fontSize = 12.sp, color = Color(0xFF1E3A8A))
                                Text("• Cyber & Theft Protection: Special hotline against pension fraud and fake investment schemes.", fontSize = 12.sp, color = Color(0xFF1E3A8A))
                                Text("• Emergency SOS: Direct line to Balasore SP Office Senior Helpdesk.", fontSize = 12.sp, color = Color(0xFF1E3A8A))
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:06782262004"))
                                context.startActivity(intent)
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D4ED8)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Call Balasore Senior Citizen Police Desk (06782-262004)", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
