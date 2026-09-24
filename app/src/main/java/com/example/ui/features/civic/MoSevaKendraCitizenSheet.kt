package com.example.ui.features.civic

import android.content.Intent
import android.net.Uri
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

data class MoSevaKendraItem(
    val blockName: String,
    val blockNameOd: String,
    val centerName: String,
    val locationLandmark: String,
    val operatorContact: String,
    val workingHours: String,
    val isVerified: Boolean = true
)

data class CitizenServiceDoc(
    val serviceName: String,
    val serviceNameOd: String,
    val officialGovtFee: String,
    val deliveryTimeDays: String,
    val mandatoryDocs: List<String>,
    val portalUrl: String
)

val BALASORE_MO_SEVA_KENDRA_LIST = listOf(
    MoSevaKendraItem(
        blockName = "Balasore Sadar",
        blockNameOd = "ବାଲେଶ୍ୱର ସଦର",
        centerName = "District Collectorate Mo Seva Desk",
        locationLandmark = "Collectorate Campus, Near Treasury Office",
        operatorContact = "06782-262010",
        workingHours = "10:00 AM - 05:30 PM (Mon-Sat)"
    ),
    MoSevaKendraItem(
        blockName = "Remuna",
        blockNameOd = "ରେମୁଣା",
        centerName = "Remuna Block Tehsil Mo Seva Center",
        locationLandmark = "Near Remuna Block Office, Gopinath Road",
        operatorContact = "06782-252240",
        workingHours = "10:00 AM - 05:00 PM"
    ),
    MoSevaKendraItem(
        blockName = "Nilagiri",
        blockNameOd = "ନୀଳଗିରି",
        centerName = "Nilagiri Sub-Collectorate CSC Desk",
        locationLandmark = "Court Complex Road, Nilagiri Town",
        operatorContact = "06782-233215",
        workingHours = "10:00 AM - 05:00 PM"
    ),
    MoSevaKendraItem(
        blockName = "Soro",
        blockNameOd = "ସୋର",
        centerName = "Soro Municipality CSC Hub",
        locationLandmark = "Near Soro Bus Stand & Block Gate",
        operatorContact = "06788-222030",
        workingHours = "09:30 AM - 05:30 PM"
    ),
    MoSevaKendraItem(
        blockName = "Jaleswar",
        blockNameOd = "ଜଳେଶ୍ୱର",
        centerName = "Jaleswar Tehsil OdishaOne Desk",
        locationLandmark = "Near Jaleswar Railway Station Square",
        operatorContact = "06781-222320",
        workingHours = "10:00 AM - 05:00 PM"
    ),
    MoSevaKendraItem(
        blockName = "Bhograi",
        blockNameOd = "ଭୋଗରାଇ",
        centerName = "Bhograi Block Center (Chandanpur)",
        locationLandmark = "Bhograi Block Office Gate, Chandanpur",
        operatorContact = "06781-230040",
        workingHours = "10:00 AM - 05:00 PM"
    ),
    MoSevaKendraItem(
        blockName = "Bahanaga",
        blockNameOd = "ବାହାନଗା",
        centerName = "Bahanaga Block Mo Seva Citizen Desk",
        locationLandmark = "Near Bahanaga Bazar NH-16 Link",
        operatorContact = "06782-275120",
        workingHours = "10:00 AM - 05:00 PM"
    ),
    MoSevaKendraItem(
        blockName = "Baliapal",
        blockNameOd = "ବାଲିଆପାଳ",
        centerName = "Baliapal Tehsil Citizen Service Center",
        locationLandmark = "Subarnarekha Link Road, Baliapal Market",
        operatorContact = "06781-255018",
        workingHours = "10:00 AM - 05:00 PM"
    )
)

val CITIZEN_SERVICE_DOCS = listOf(
    CitizenServiceDoc(
        serviceName = "Resident / Domicile Certificate",
        serviceNameOd = "ବାସିନ୍ଦା ପ୍ରମାଣପତ୍ର",
        officialGovtFee = "₹30 (Govt User Fee)",
        deliveryTimeDays = "15 Working Days",
        mandatoryDocs = listOf("Aadhaar Card copy", "Land ROR (Pattadar / ROR page)", "Electricity Bill / Holding Tax", "Passport photo"),
        portalUrl = "https://edistrict.odisha.gov.in"
    ),
    CitizenServiceDoc(
        serviceName = "Income Certificate",
        serviceNameOd = "ଆୟ ପ୍ରମାଣପତ୍ର",
        officialGovtFee = "₹30 (Govt User Fee)",
        deliveryTimeDays = "15 Working Days",
        mandatoryDocs = listOf("Salary slip / Agriculture declaration", "Aadhaar Card", "Land revenue receipt (Khajana)", "Affidavit"),
        portalUrl = "https://edistrict.odisha.gov.in"
    ),
    CitizenServiceDoc(
        serviceName = "Caste Certificate (SEBC/OBC/SC/ST)",
        serviceNameOd = "ଜାତି ପ୍ରମାଣପତ୍ର",
        officialGovtFee = "₹30 (Govt User Fee)",
        deliveryTimeDays = "30 Working Days",
        mandatoryDocs = listOf("Pre-1950/1960 Land ROR or Father's caste certificate", "Aadhaar Card", "School leaving certificate"),
        portalUrl = "https://edistrict.odisha.gov.in"
    ),
    CitizenServiceDoc(
        serviceName = "Bhulekh Land Record (Khatiyan / ROR)",
        serviceNameOd = "ଭୂଲେଖ ଜମି ପଟ୍ଟା ଯାଞ୍ଚ",
        officialGovtFee = "Free online / ₹50 certified copy",
        deliveryTimeDays = "Instant view / 7 days certified",
        mandatoryDocs = listOf("District: Balasore", "Tehsil & RI Circle name", "Village (Mauza) name", "Khata Number or Plot Number"),
        portalUrl = "http://bhulekh.ori.nic.in"
    ),
    CitizenServiceDoc(
        serviceName = "Legal Heir Certificate",
        serviceNameOd = "ଉତ୍ତରାଧିକାରୀ ପ୍ରମାଣପତ୍ର",
        officialGovtFee = "₹30 (Govt User Fee)",
        deliveryTimeDays = "30 Working Days",
        mandatoryDocs = listOf("Death certificate of deceased", "Legal Heir affidavit in front of Executive Magistrate", "Family tree & Aadhaar"),
        portalUrl = "https://edistrict.odisha.gov.in"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoSevaKendraCitizenSheet(
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }
    var selectedBlockFilter by remember { mutableStateOf("All") }

    val blocks = remember { listOf("All", "Balasore Sadar", "Remuna", "Nilagiri", "Soro", "Jaleswar", "Bhograi", "Bahanaga", "Baliapal") }

    val filteredCenters = remember(selectedBlockFilter) {
        if (selectedBlockFilter == "All") BALASORE_MO_SEVA_KENDRA_LIST
        else BALASORE_MO_SEVA_KENDRA_LIST.filter { it.blockName.contains(selectedBlockFilter, ignoreCase = true) }
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
                .testTag("mo_seva_kendra_sheet")
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
                        Text("🌾", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ମୋ ସେବା କେନ୍ଦ୍ର ଓ ଇ-ପ୍ରଶାସନ" else "Mo Seva Kendra Citizen Desk",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ସରକାରୀ ନିର୍ଦ୍ଧାରିତ ଦେୟ ଓ ପ୍ରମାଣପତ୍ର ତାଲିକା" else "e-District Odisha • Verified Fee Caps & Blocks",
                            fontSize = 12.sp,
                            color = Color(0xFF0369A1)
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
                contentColor = Color(0xFF0369A1),
                indicator = {},
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ମୋ ସେବା କେନ୍ଦ୍ର" else "Centers Directory",
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
                            text = if (language == AppLanguage.ODIA) "ପ୍ରମାଣପତ୍ର ଓ ଦେୟ" else "Govt Fees & Docs",
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
                            text = if (language == AppLanguage.ODIA) "ଅଭିଯୋଗ ଡେସ୍କ" else "Anti-Overcharge",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            when (selectedTab) {
                0 -> {
                    // Block Filter Chips
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(blocks) { block ->
                            val isSelected = selectedBlockFilter == block
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedBlockFilter = block },
                                label = { Text(block, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF0284C7),
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxHeight(0.82f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(filteredCenters) { center ->
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
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = if (language == AppLanguage.ODIA) center.blockNameOd else center.blockName,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF0284C7),
                                                    modifier = Modifier
                                                        .background(Color(0xFFE0F2FE), RoundedCornerShape(6.dp))
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text("✓ Verified", fontSize = 10.sp, color = Color(0xFF16A34A), fontWeight = FontWeight.Bold)
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = center.centerName,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "📍 ${center.locationLandmark}",
                                                fontSize = 12.sp,
                                                color = Color(0xFF475569)
                                            )
                                            Text(
                                                text = "🕒 ${center.workingHours}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF64748B)
                                            )
                                        }
                                        Button(
                                            onClick = {
                                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${center.operatorContact}"))
                                                context.startActivity(intent)
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Call", fontSize = 11.sp)
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
                        items(CITIZEN_SERVICE_DOCS) { doc ->
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
                                            text = if (language == AppLanguage.ODIA) doc.serviceNameOd else doc.serviceName,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0F172A)
                                        )
                                        Text(
                                            text = doc.officialGovtFee,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF166534),
                                            modifier = Modifier
                                                .background(Color(0xFFDCFCE7), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                    Text(
                                        text = "⏱️ Official SLA: ${doc.deliveryTimeDays}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF0369A1),
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                    Text(
                                        text = if (language == AppLanguage.ODIA) "ଆବଶ୍ୟକ କାଗଜପତ୍ର:" else "Mandatory Documents:",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF334155)
                                    )
                                    doc.mandatoryDocs.forEach { item ->
                                        Text(
                                            text = "• $item",
                                            fontSize = 11.sp,
                                            color = Color(0xFF475569)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedButton(
                                        onClick = {
                                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(doc.portalUrl))
                                            context.startActivity(browserIntent)
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth(),
                                        contentPadding = PaddingValues(vertical = 6.dp)
                                    ) {
                                        Text("Open Official Portal (${doc.portalUrl.removePrefix("https://").removePrefix("http://")})", fontSize = 11.sp)
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
                            color = Color(0xFFFEF2F2),
                            border = BorderStroke(1.dp, Color(0xFFFCA5A5)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଅଧିକ ଫିସ୍ ନେଲେ ଅଭିଯୋଗ କରନ୍ତୁ" else "Zero Tolerance to Overcharging",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF991B1B)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (language == AppLanguage.ODIA)
                                        "ସରକାରୀ ନିୟମ ଅନୁସାରେ ମୋ ସେବା କେନ୍ଦ୍ର କେବଳ ଧାର୍ଯ୍ୟ ₹୩୦ ୟୁଜର୍ ଫିସ୍ ନେଇପାରିବେ। ଯଦି କୌଣସି ସେବା କେନ୍ଦ୍ର ଅତିରିକ୍ତ ଦାବି କରନ୍ତି, ତେବେ ତୁରନ୍ତ ଟୋଲ୍ ଫ୍ରି ନମ୍ବର ୧୮୦୦-୩୪୫-୬୭୭୦ ରେ ଅଭିଯୋଗ ଦାଖଲ କରନ୍ତୁ।"
                                    else
                                        "As per Odisha Govt regulations, authorized Mo Seva Kendras / CSCs are legally restricted to charging the ₹30 statutory user fee for e-District certificates. If any private operator demands extra, report immediately.",
                                    fontSize = 12.sp,
                                    color = Color(0xFF7F1D1D),
                                    lineHeight = 18.sp
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:18003456770"))
                                        context.startActivity(intent)
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.Phone, contentDescription = "Call")
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Call Odisha e-District Helpline (1800-345-6770)", fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFF8FAFC),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଅନଲାଇନ୍ ଅଭିଯୋଗ ପୋର୍ଟାଲ" else "State Grievance Desks",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("• Jana Sunani Odisha (Toll Free: 155335)", fontSize = 12.sp, color = Color(0xFF334155))
                                Text("• District Collectorate Grievance Cell (Every Monday)", fontSize = 12.sp, color = Color(0xFF334155))
                                Text("• Mo Sarkar Feedback System Linkage", fontSize = 12.sp, color = Color(0xFF334155))
                            }
                        }
                    }
                }
            }
        }
    }
}
