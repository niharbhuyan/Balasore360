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

data class PanMandiRate(
    val varietyName: String,
    val varietyNameOd: String,
    val growingHub: String,
    val pricePerDoli: String, // 1 Doli = 4,000 leaves or 1 Kori = 200 leaves
    val dailyTrend: String,
    val demandMarket: String
)

data class ShrimpAquaRate(
    val shrimpGrade: String,
    val countPerKg: String,
    val farmGatePrice: String,
    val recommendedWaterSalinity: String,
    val pondDoLevel: String
)

val BALASORE_PAN_RATES = listOf(
    PanMandiRate(
        varietyName = "Bhograi Meetha Pan (Export Grade)",
        varietyNameOd = "ଭୋଗରାଇ ମିଠା ପାନ (ରପ୍ତାନୀ ଯୋଗ୍ୟ)",
        growingHub = "Bhograi & Jaleswar River Belts",
        pricePerDoli = "₹6,800 - ₹8,200 per Doli (4,000 leaves)",
        dailyTrend = "▲ +₹250 (High Festive Demand)",
        demandMarket = "Varanasi, Kolkata, Delhi, & Bangladesh"
    ),
    PanMandiRate(
        varietyName = "Baliapal Sanchi Pan (Pungent Aromatic)",
        varietyNameOd = "ବାଲିଆପାଳ ସାଞ୍ଚି ପାନ",
        growingHub = "Baliapal & Langaleswar coastal ridges",
        pricePerDoli = "₹4,200 - ₹5,400 per Doli",
        dailyTrend = "► Stable",
        demandMarket = "Cuttack, Bhubaneswar, & Mumbai"
    ),
    PanMandiRate(
        varietyName = "Goda Pan / Local Chewing Leaf",
        varietyNameOd = "ଗୋଦା ପାନ (ସ୍ଥାନୀୟ ବ୍ୟବହାର)",
        growingHub = "Basta & Bahanaga Panchayats",
        pricePerDoli = "₹180 - ₹240 per Kori (200 leaves)",
        dailyTrend = "► Stable",
        demandMarket = "Balasore Town & Bhadrak Mandis"
    )
)

val BALASORE_SHRIMP_RATES = listOf(
    ShrimpAquaRate(
        shrimpGrade = "Litopenaeus Vannamei (Large)",
        countPerKg = "30 Count (30 pcs/kg)",
        farmGatePrice = "₹420 - ₹445 / kg",
        recommendedWaterSalinity = "12 - 18 ppt (Brackish)",
        pondDoLevel = "DO > 5.5 mg/L (Aerators Active)"
    ),
    ShrimpAquaRate(
        shrimpGrade = "Litopenaeus Vannamei (Medium Export)",
        countPerKg = "40 Count (40 pcs/kg)",
        farmGatePrice = "₹360 - ₹385 / kg",
        recommendedWaterSalinity = "10 - 15 ppt",
        pondDoLevel = "DO > 5.0 mg/L"
    ),
    ShrimpAquaRate(
        shrimpGrade = "Litopenaeus Vannamei (Standard)",
        countPerKg = "50 Count (50 pcs/kg)",
        farmGatePrice = "₹310 - ₹330 / kg",
        recommendedWaterSalinity = "8 - 14 ppt",
        pondDoLevel = "DO > 4.5 mg/L"
    ),
    ShrimpAquaRate(
        shrimpGrade = "Black Tiger (Penaeus Monodon)",
        countPerKg = "20 Count (Jumbo Wild)",
        farmGatePrice = "₹680 - ₹740 / kg",
        recommendedWaterSalinity = "15 - 25 ppt (Estuary)",
        pondDoLevel = "DO > 6.0 mg/L"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanBarajaAquaShrimpSheet(
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }
    var mandiSyncTime by remember { mutableStateOf("Just now") }

    LaunchedEffect(Unit) {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        mandiSyncTime = sdf.format(Date())
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
                .testTag("pan_baraja_aqua_shrimp_sheet")
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
                        Text("🍃", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ପାନ ବରଜ ଓ ଚିଙ୍ଗୁଡ଼ି ଚାଷ ମଣ୍ଡି ଡେସ୍କ" else "Pan Baraja & Shrimp Aquaculture Mandi",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଭୋଗରାଇ ମିଠା ପାନ • ବାଲିଆପାଳ ଭେନାମାଇ • ଦୈନିକ ଦର" else "Bhograi Betel Leaf • Vannamei Shrimp Farm Rates",
                            fontSize = 12.sp,
                            color = Color(0xFF166534)
                        )
                    }
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Live Mandi Sync Pill
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF0FDF4),
                border = BorderStroke(1.dp, Color(0xFF86EFAC)),
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
                                .background(Color(0xFF16A34A))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Mandi rates live at $mandiSyncTime (Bhograi/Baliapal Hub)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF15803D)
                        )
                    }
                    Text(
                        text = "LIVE FARM-GATE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF166534)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFFF8FAFC),
                contentColor = Color(0xFF166534),
                indicator = {},
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ପାନ ବରଜ ଦର" else "Pan Mandi",
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
                            text = if (language == AppLanguage.ODIA) "ଚିଙ୍ଗୁଡ଼ି ଚାଷ ରେଟ୍" else "Shrimp Rates",
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
                            text = if (language == AppLanguage.ODIA) "ସରକାରୀ ସବସିଡି" else "Govt Schemes",
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
                        items(BALASORE_PAN_RATES) { item ->
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
                                            text = if (language == AppLanguage.ODIA) item.varietyNameOd else item.varietyName,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0F172A),
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = item.dailyTrend,
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
                                        text = item.pricePerDoli,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF166534)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "📍 Belts: ${item.growingHub} • Markets: ${item.demandMarket}",
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
                        items(BALASORE_SHRIMP_RATES) { shrimp ->
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
                                                text = shrimp.shrimpGrade,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "Size: ${shrimp.countPerKg}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF0369A1),
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                        Text(
                                            text = shrimp.farmGatePrice,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0F766E),
                                            modifier = Modifier
                                                .background(Color(0xFFCCFBF1), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFFF1F5F9),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "💧 Target Salinity: ${shrimp.recommendedWaterSalinity} • Aeration: ${shrimp.pondDoLevel}",
                                            fontSize = 11.sp,
                                            color = Color(0xFF334155),
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                        )
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
                            color = Color(0xFFF0FDF4),
                            border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଓଡ଼ିଶା ପାନ ବରଜ ଓ PMMSY ଚିଙ୍ଗୁଡ଼ି ଯୋଜନା" else "State Pan Baraja & PMMSY Shrimp Subsidies",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF166534)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "1. Odisha Pan Baraja Assistance: Financial grant up to ₹25,000 per 10-decimal Baraja reconstruction damaged by cyclones or pests (Horticulture Directorate).\n\n2. PMMSY Aquaculture Subsidy: 40% (General) and 60% (SC/ST/Women) subsidy on shrimp pond excavation, paddle-wheel aerators, and solar pumps via Balasore District Fisheries Office (DFO).",
                                    fontSize = 12.sp,
                                    color = Color(0xFF14532D),
                                    lineHeight = 17.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:06782-262115"))
                                        context.startActivity(intent)
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF166534)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.Phone, contentDescription = "DFO", modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Contact Balasore District Fisheries Officer (DFO)", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
