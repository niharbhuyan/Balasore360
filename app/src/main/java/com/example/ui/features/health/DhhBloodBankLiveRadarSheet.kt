package com.example.ui.features.health

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

enum class BloodStockLevel(val labelEn: String, val labelOd: String, val color: Color, val bgColor: Color) {
    AMPLE("AMPLE STOCK", "ପର୍ଯ୍ୟାପ୍ତ ମହଜୁଦ", Color(0xFF166534), Color(0xFFDCFCE7)),
    MODERATE("MODERATE", "ମଧ୍ୟମ ସ୍ଥିତି", Color(0xFFB45309), Color(0xFFFEF3C7)),
    CRITICAL_SHORTAGE("CRITICAL / NEED DONORS", "ଜରୁରୀ ଆବଶ୍ୟକ", Color(0xFF991B1B), Color(0xFFFEE2E2))
}

data class BloodGroupStock(
    val bloodGroup: String,
    val unitsAvailable: Int,
    val componentUnits: String,
    val status: BloodStockLevel,
    val lastDonationTime: String
)

data class BloodBankCenter(
    val facilityName: String,
    val facilityNameOd: String,
    val address: String,
    val phone24x7: String,
    val isApheresisPlateletAvailable: Boolean
)

val BALASORE_BLOOD_STOCKS = listOf(
    BloodGroupStock("O +ve", 38, "Whole: 22 • PRBC: 12 • FFP: 4", BloodStockLevel.AMPLE, "Updated 15 mins ago"),
    BloodGroupStock("A +ve", 19, "Whole: 10 • PRBC: 6 • FFP: 3", BloodStockLevel.AMPLE, "Updated 30 mins ago"),
    BloodGroupStock("B +ve", 24, "Whole: 14 • PRBC: 7 • FFP: 3", BloodStockLevel.AMPLE, "Updated 10 mins ago"),
    BloodGroupStock("AB +ve", 9, "Whole: 5 • PRBC: 3 • FFP: 1", BloodStockLevel.MODERATE, "Updated 45 mins ago"),
    BloodGroupStock("O -ve (Rare)", 3, "Whole: 2 • PRBC: 1", BloodStockLevel.CRITICAL_SHORTAGE, "Voluntary Donors Alerted"),
    BloodGroupStock("B -ve (Rare)", 2, "Whole: 1 • PRBC: 1", BloodStockLevel.CRITICAL_SHORTAGE, "Call Donor Desk"),
    BloodGroupStock("A -ve (Rare)", 4, "Whole: 3 • PRBC: 1", BloodStockLevel.MODERATE, "Updated 1 hour ago"),
    BloodGroupStock("Single Donor Platelets (SDP)", 6, "Apheresis Kit Available at DHH", BloodStockLevel.AMPLE, "Emergency Ready")
)

val BALASORE_BLOOD_BANKS = listOf(
    BloodBankCenter(
        facilityName = "Balasore DHH Model Blood Bank & Component Separation Unit",
        facilityNameOd = "ବାଲେଶ୍ୱର ଜିଲ୍ଲା ମୁଖ୍ୟ ଚିକିତ୍ସାଳୟ ମଡେଲ ରକ୍ତ ଭଣ୍ଡାର",
        address = "DHH Old Campus, Hospital Road, Balasore",
        phone24x7 = "06782-262024",
        isApheresisPlateletAvailable = true
    ),
    BloodBankCenter(
        facilityName = "Fakir Mohan MCH Blood Bank (Remuna Campus)",
        facilityNameOd = "ଫକୀର ମୋହନ ମେଡିକାଲ କଲେଜ ରକ୍ତ କେନ୍ଦ୍ର",
        address = "FM MCH Complex, Nuapadhi, Remuna",
        phone24x7 = "06782-275800",
        isApheresisPlateletAvailable = true
    ),
    BloodBankCenter(
        facilityName = "Indian Red Cross Society Balasore District Branch",
        facilityNameOd = "ରେଡ କ୍ରସ୍ ସୋସାଇଟି ବାଲେଶ୍ୱର ଶାଖା",
        address = "Collectorate Square, Balasore Town",
        phone24x7 = "06782-262180",
        isApheresisPlateletAvailable = false
    ),
    BloodBankCenter(
        facilityName = "Soro Sub-Divisional Hospital Blood Storage Unit",
        facilityNameOd = "ସୋରୋ ଉପଖଣ୍ଡ ଡାକ୍ତରଖାନା ରକ୍ତ ସଂରକ୍ଷଣ କେନ୍ଦ୍ର",
        address = "Hospital Road, Soro Town",
        phone24x7 = "06788-222120",
        isApheresisPlateletAvailable = false
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DhhBloodBankLiveRadarSheet(
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }
    var lastRefreshTimestamp by remember { mutableStateOf("Just now") }

    LaunchedEffect(Unit) {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        lastRefreshTimestamp = sdf.format(Date())
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
                .testTag("dhh_blood_bank_live_radar_sheet")
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
                        Text("🩸", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ଜିଲ୍ଲା ରକ୍ତ ଭଣ୍ଡାର ଲାଇଭ୍ ଷ୍ଟକ୍" else "District Blood Bank & Component Radar",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଡିଏଚଏଚ୍ • ଏଫଏମ ଏମସିଏଚ • ରେଡକ୍ରସ୍ • ୨୪/୭ ମହଜୁଦ" else "DHH • FM MCH • Red Cross 24x7 Inventory",
                            fontSize = 12.sp,
                            color = Color(0xFFDC2626)
                        )
                    }
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Auto-update Telemetry Banner
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFFEF2F2),
                border = BorderStroke(1.dp, Color(0xFFFECACA)),
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
                                .background(Color(0xFFDC2626))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "e-RaktKosh live sync: $lastRefreshTimestamp",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF991B1B)
                        )
                    }
                    Text(
                        text = "105 UNITS TOTAL",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF991B1B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFFF8FAFC),
                contentColor = Color(0xFFDC2626),
                indicator = {},
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ରକ୍ତ ଗ୍ରୁପ୍ ଷ୍ଟକ୍" else "Blood Inventory",
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
                            text = if (language == AppLanguage.ODIA) "ରକ୍ତ କେନ୍ଦ୍ର ତାଲିକା" else "Blood Banks",
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
                            text = if (language == AppLanguage.ODIA) "ଦାନକାରୀ ହେଲ୍ପଲାଇନ୍" else "Donor Registry",
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
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(BALASORE_BLOOD_STOCKS) { stock ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(38.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFFEE2E2)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = stock.bloodGroup,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = Color(0xFF991B1B)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = "${stock.unitsAvailable} Units in Stock",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = stock.componentUnits,
                                                fontSize = 10.sp,
                                                color = Color(0xFF475569)
                                            )
                                        }
                                    }
                                    Text(
                                        text = if (language == AppLanguage.ODIA) stock.status.labelOd else stock.status.labelEn,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = stock.status.color,
                                        modifier = Modifier
                                            .background(stock.status.bgColor, RoundedCornerShape(6.dp))
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
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
                        items(BALASORE_BLOOD_BANKS) { bank ->
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
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = if (language == AppLanguage.ODIA) bank.facilityNameOd else bank.facilityName,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "📍 ${bank.address}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF475569)
                                            )
                                            if (bank.isApheresisPlateletAvailable) {
                                                Text(
                                                    text = "✨ Single Donor Apheresis Platelets Ready",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Color(0xFF166534),
                                                    modifier = Modifier.padding(top = 2.dp)
                                                )
                                            }
                                        }
                                        Button(
                                            onClick = {
                                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${bank.phone24x7}"))
                                                context.startActivity(intent)
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
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
                2 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.82f)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFFEF2F2),
                            border = BorderStroke(1.dp, Color(0xFFFECACA)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଜରୁରୀ ସ୍ୱେଚ୍ଛାସେବୀ ରକ୍ତଦାତା ରେଜିଷ୍ଟ୍ରି" else "Voluntary Emergency Donor Connect",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF991B1B)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "In case of emergency trauma surgeries or rare negative blood units (-ve), Balasore District Red Cross coordinates with registered youth volunteers across Sadar, Remuna, Soro, and Jaleswar blocks. Donors must be aged 18-65 with Hb > 12.5 g/dL.",
                                    fontSize = 12.sp,
                                    color = Color(0xFF7F1D1D),
                                    lineHeight = 17.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedButton(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:06782-262024"))
                                        context.startActivity(intent)
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.Favorite, contentDescription = "Donate", tint = Color(0xFFDC2626), modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Register as Voluntary Donor (DHH)", fontSize = 12.sp, color = Color(0xFF991B1B))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
