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

data class CourtComplexInfo(
    val courtTitle: String,
    val courtTitleOd: String,
    val location: String,
    val registrarPhone: String,
    val jurisdictionsCovered: String
)

data class LokAdalatCategory(
    val categoryName: String,
    val categoryNameOd: String,
    val mattersSettled: String,
    val benefits: String
)

val BALASORE_COURTS = listOf(
    CourtComplexInfo(
        courtTitle = "District & Sessions Judge Court Complex",
        courtTitleOd = "ଜିଲ୍ଲା ଓ ଦୌରାଜଜ୍ କୋର୍ଟ ପରିସର",
        location = "Court Square, Balasore Town",
        registrarPhone = "06782-262070",
        jurisdictionsCovered = "Civil Appeals, Sessions Trials, MACT, Family Court & Commercial Division"
    ),
    CourtComplexInfo(
        courtTitle = "Sub-Divisional Judicial Magistrate (SDJM) Court Nilagiri",
        courtTitleOd = "ଉପଖଣ୍ଡ ନ୍ୟାୟିକ ମାଜିଷ୍ଟ୍ରେଟ୍ କୋର୍ଟ ନୀଳଗିରି",
        location = "Court Road, Nilagiri Town",
        registrarPhone = "06782-233240",
        jurisdictionsCovered = "Nilagiri, Oupada, & Berhampur criminal & revenue disputes"
    ),
    CourtComplexInfo(
        courtTitle = "SDJM Court Soro",
        courtTitleOd = "ଉପଖଣ୍ଡ ନ୍ୟାୟିକ ମାଜିଷ୍ଟ୍ରେଟ୍ କୋର୍ଟ ସୋରୋ",
        location = "Near Soro College, Soro Town",
        registrarPhone = "06788-222300",
        jurisdictionsCovered = "Soro, Simulia, Khaira & Bahanaga police station limits"
    ),
    CourtComplexInfo(
        courtTitle = "District Legal Services Authority (DLSA) Helpdesk",
        courtTitleOd = "ଜିଲ୍ଲା ଆଇନ ସେବା ପ୍ରାଧିକରଣ (DLSA)",
        location = "ADR Center, Civil Court Campus, Balasore",
        registrarPhone = "06782-263220",
        jurisdictionsCovered = "Free legal aid lawyers, victim compensation & mediation"
    )
)

val LOK_ADALAT_MATTERS = listOf(
    LokAdalatCategory(
        categoryName = "Motor Accident Claims (MACT)",
        categoryNameOd = "ମୋଟର ଦୁର୍ଘଟଣା କ୍ଷତିପୂରଣ (MACT)",
        mattersSettled = "Insurance claims, direct compensation without court fees.",
        benefits = "No appeal against Lok Adalat award; prompt bank account disbursal."
    ),
    LokAdalatCategory(
        categoryName = "Bank Loan Recovery & Cheque Bounce (Sec 138 NI Act)",
        categoryNameOd = "ବ୍ୟାଙ୍କ ଋଣ ଓ ଚେକ୍ ବାଉନ୍ସ ମାମଲା",
        mattersSettled = "SBI, UCO, Gramya Bank OTS (One-Time Settlement) waivers.",
        benefits = "Court fee refund on pre-litigation settlement."
    ),
    LokAdalatCategory(
        categoryName = "Electricity Bill (TPNODL) & Compoundable Criminal",
        categoryNameOd = "ବିଦ୍ୟୁତ୍ ବିଲ୍ ଓ ଆପୋଷ ବୁଝାମଣା ଯୋଗ୍ୟ ମାମଲା",
        mattersSettled = "TPNODL domestic arrears rebate, petty neighbor disputes.",
        benefits = "Immediate amicable closure with zero lingering police record."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalasoreCourtLegalAidSheet(
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }
    var calendarSyncTime by remember { mutableStateOf("Just now") }

    LaunchedEffect(Unit) {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        calendarSyncTime = sdf.format(Date())
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
                .testTag("balasore_court_legal_aid_sheet")
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
                            .background(Color(0xFFEDE9FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("⚖️", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ଜିଲ୍ଲା କୋର୍ଟ ଓ ଆଇନ ସେବା ଡେସ୍କ" else "District Court & DLSA Legal Aid Desk",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ମାଗଣା ଆଇନ ସହାୟତା • ଲୋକ ଅଦାଲତ • କୋର୍ଟ କ୍ୟାଲେଣ୍ଡର" else "Free Legal Aid • Lok Adalat • ADR Mediation Center",
                            fontSize = 12.sp,
                            color = Color(0xFF6D28D9)
                        )
                    }
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Calendar Sync Pill
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF5F3FF),
                border = BorderStroke(1.dp, Color(0xFFDDD6FE)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 7.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF7C3AED))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Cause list calendar active at $calendarSyncTime",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF5B21B6)
                        )
                    }
                    Text(
                        text = "e-Courts LIVE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6D28D9)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFFF8FAFC),
                contentColor = Color(0xFF6D28D9),
                indicator = {},
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "କୋର୍ଟ ପରିସର" else "Courts",
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
                            text = if (language == AppLanguage.ODIA) "ମାଗଣା ଆଇନ ସେବା" else "Free Legal Aid",
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
                            text = if (language == AppLanguage.ODIA) "ଲୋକ ଅଦାଲତ" else "Lok Adalat",
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
                        items(BALASORE_COURTS) { court ->
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
                                                text = if (language == AppLanguage.ODIA) court.courtTitleOd else court.courtTitle,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "📍 ${court.location}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF64748B)
                                            )
                                        }
                                        Button(
                                            onClick = {
                                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${court.registrarPhone}"))
                                                context.startActivity(intent)
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED)),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Contact", fontSize = 11.sp)
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "🏛️ ${court.jurisdictionsCovered}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF475569)
                                    )
                                }
                            }
                        }
                    }
                }
                1 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.82f)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFF5F3FF),
                            border = BorderStroke(1.dp, Color(0xFFDDD6FE)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ମାଗଣା ସରକାରୀ ଓକିଲ ଓ ଆଇନ ସହାୟତା ପାଇବା ପାଇଁ ଯୋଗ୍ୟତା" else "Free Legal Aid Eligibility (DLSA Balasore)",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF5B21B6)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Under Legal Services Authorities Act Sec 12, the following citizens are entitled to 100% free legal representation:\n\n• Women & Children of all income levels\n• Members of Scheduled Castes (SC) and Scheduled Tribes (ST)\n• Persons in custody or detention\n• Industrial workmen & disaster victims\n• Persons with annual income under ₹3 Lakhs.",
                                    fontSize = 12.sp,
                                    color = Color(0xFF4C1D95),
                                    lineHeight = 17.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedButton(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://services.ecourts.gov.in"))
                                        context.startActivity(intent)
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Track Case Status on e-Courts Portal ↗", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
                2 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxHeight(0.82f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(LOK_ADALAT_MATTERS) { item ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = if (language == AppLanguage.ODIA) item.categoryNameOd else item.categoryName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = item.mattersSettled,
                                        fontSize = 12.sp,
                                        color = Color(0xFF475569)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "✨ Benefit: ${item.benefits}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF166534),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
