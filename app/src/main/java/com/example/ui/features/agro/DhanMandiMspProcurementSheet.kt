package com.example.ui.features.agro

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

data class PacsMandiLocation(
    val mandiName: String,
    val mandiNameOd: String,
    val block: String,
    val tokenActiveCount: Int,
    val moistureTolerance: String,
    val dailyProcurementQuintals: String,
    val secretaryPhone: String
)

data class PaddyPriceBenchmark(
    val varietyName: String,
    val varietyNameOd: String,
    val mspGovernmentRate: String,
    val privateMarketAverage: String,
    val harvestSeason: String
)

val PACS_MANDIS = listOf(
    PacsMandiLocation(
        mandiName = "Jaleswar RMC Central Paddy Yard",
        mandiNameOd = "ଜଳେଶ୍ୱର ନିୟନ୍ତ୍ରିତ ବଜାର କମିଟି (RMC) ମଣ୍ଡି",
        block = "Jaleswar / Laxmannath Border",
        tokenActiveCount = 142,
        moistureTolerance = "Max 17.0% (FAQ Quality standard)",
        dailyProcurementQuintals = "4,200 Quintals / day",
        secretaryPhone = "06781222140"
    ),
    PacsMandiLocation(
        mandiName = "Soro PACS Procurement Center",
        mandiNameOd = "ସୋର ପ୍ରାଥମିକ କୃଷି ସମବାୟ ସମିତି (PACS) କେନ୍ଦ୍ର",
        block = "Soro Sub-division",
        tokenActiveCount = 98,
        moistureTolerance = "Max 17.0% (Automated moisture meter)",
        dailyProcurementQuintals = "3,100 Quintals / day",
        secretaryPhone = "06788220055"
    ),
    PacsMandiLocation(
        mandiName = "Basta Regional Farmers Cooperative Mandi",
        mandiNameOd = "ବସ୍ତା ଆଞ୍ଚଳିକ କୃଷକ ସମବାୟ ମଣ୍ଡି",
        block = "Basta Block",
        tokenActiveCount = 85,
        moistureTolerance = "Max 17.0% (Direct DBT credit to bank)",
        dailyProcurementQuintals = "2,650 Quintals / day",
        secretaryPhone = "06781254320"
    ),
    PacsMandiLocation(
        mandiName = "Simulia Agro Hub & Grain Storage",
        mandiNameOd = "ସିମୁଳିଆ କୃଷି ଗୋଦାମ ଓ ଧାନ କ୍ରୟ କେନ୍ଦ୍ର",
        block = "Simulia Block",
        tokenActiveCount = 110,
        moistureTolerance = "Max 17.0% (Gunny bag stock available)",
        dailyProcurementQuintals = "3,400 Quintals / day",
        secretaryPhone = "06788234120"
    )
)

val PADDY_PRICES = listOf(
    PaddyPriceBenchmark(
        varietyName = "Common Paddy (MSP Benchmark)",
        varietyNameOd = "ସାଧାରଣ ଧାନ (ସରକାରୀ ସର୍ବନିମ୍ନ ସହାୟକ ମୂଲ୍ୟ)",
        mspGovernmentRate = "₹2,300 / Quintal (+ State Input Subsidy)",
        privateMarketAverage = "₹2,100 - ₹2,180 / Quintal",
        harvestSeason = "Kharif & Rabi Procurement Cycle"
    ),
    PaddyPriceBenchmark(
        varietyName = "Grade-A Super Fine Paddy",
        varietyNameOd = "ଗ୍ରେଡ୍-ଏ ଉନ୍ନତମାନର ସରୁ ଧାନ",
        mspGovernmentRate = "₹2,320 / Quintal",
        privateMarketAverage = "₹2,250 - ₹2,350 / Quintal",
        harvestSeason = "Swarna, Pooja & MTU-1010 varieties"
    ),
    PaddyPriceBenchmark(
        varietyName = "Aromatic Gobindobhog & Kalajeera",
        varietyNameOd = "ବାସ୍ନା ଯୁକ୍ତ ଗୋବିନ୍ଦଭୋଗ ଓ କଳାଜିରା",
        mspGovernmentRate = "Non-MSP (Speciality Heritage Rice)",
        privateMarketAverage = "₹4,800 - ₹5,400 / Quintal (Premium Export)",
        harvestSeason = "Bhograi & Jaleswar River Basin"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DhanMandiMspProcurementSheet(
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
                .testTag("dhan_mandi_msp_sheet")
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
                        Text("🌾", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଧାନ ମଣ୍ଡି ଓ ସରକାରୀ ଏମଏସପି କ୍ରୟ ରାଡାର" else "Balasore Dhan Mandi & MSP Procurement",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଜଳେଶ୍ୱର • ସୋର • ବସ୍ତା • ସିମୁଳିଆ PACS ଟୋକନ୍" else "PACS Mandi Tokens • Moisture Limits • KVK Desk",
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
                                .background(Color(0xFF16A34A))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Food Supplies & Consumer Welfare live sync ($syncTime)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF92400E)
                        )
                    }
                    Text(
                        text = "MANDI OPEN",
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
                contentColor = Color(0xFFB45309),
                indicator = {},
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ମଣ୍ଡି ସ୍ଥିତି" else "PACS Mandis",
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
                            text = if (language == AppLanguage.ODIA) "ସହାୟକ ମୂଲ୍ୟ (MSP)" else "MSP Rates",
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
                            text = if (language == AppLanguage.ODIA) "କେଭିକେ ସହାୟତା" else "KVK Balasore",
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
                        items(PACS_MANDIS) { mandi ->
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
                                                text = if (language == AppLanguage.ODIA) mandi.mandiNameOd else mandi.mandiName,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "📍 Block: ${mandi.block}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF64748B)
                                            )
                                        }
                                        Text(
                                            text = "${mandi.tokenActiveCount} Tokens Active",
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
                                        text = "💧 Quality: ${mandi.moistureTolerance}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF0F766E),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "⚖️ Daily Intake: ${mandi.dailyProcurementQuintals}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF334155),
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = {
                                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${mandi.secretaryPhone}"))
                                            context.startActivity(intent)
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp)
                                    ) {
                                        Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Call Mandi Secretary", fontSize = 11.sp)
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
                        items(PADDY_PRICES) { item ->
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
                                                text = if (language == AppLanguage.ODIA) item.varietyNameOd else item.varietyName,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = item.harvestSeason,
                                                fontSize = 11.sp,
                                                color = Color(0xFF64748B)
                                            )
                                        }
                                        Text(
                                            text = item.mspGovernmentRate.substringBefore("("),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFB45309),
                                            modifier = Modifier
                                                .background(Color(0xFFFEF3C7), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "🛒 Private Local Market: ${item.privateMarketAverage}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF334155)
                                    )
                                    Text(
                                        text = "🏦 Direct Benefit Transfer: Direct to registered Aadhaar-linked bank accounts within 48-72 hours of token weighment.",
                                        fontSize = 11.sp,
                                        color = Color(0xFF15803D),
                                        fontWeight = FontWeight.Medium,
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
                                    text = if (language == AppLanguage.ODIA) "କୃଷି ବିଜ୍ଞାନ କେନ୍ଦ୍ର (KVK) ବାଲେଶ୍ୱର ପରାମର୍ଶ ଡେସ୍କ" else "Krishi Vigyan Kendra (KVK) Balasore Tele-Clinic",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF166534)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("• Moisture Management: Sun-dry paddy to achieve <17% moisture content to prevent token rejection.", fontSize = 12.sp, color = Color(0xFF14532D))
                                Text("• Salinity Soil Care: Gypsum treatment and salt-tolerant CR-Dhan 409 & Luna Sankhi seed guidance for coastal belt.", fontSize = 12.sp, color = Color(0xFF14532D))
                                Text("• Brown Plant Hopper (BPH) Alert: Free bio-fungicide sprays available at Block Agriculture Offices.", fontSize = 12.sp, color = Color(0xFF14532D))
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:06782240180"))
                                context.startActivity(intent)
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF15803D)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Call KVK Balasore Agronomist (06782-240180)", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
