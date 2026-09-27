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
import com.example.data.daily.DailyBalasorePulse
import com.example.data.model.AppLanguage
import com.example.ui.theme.*

data class CashewCommoditySpot(
    val commodityNameEn: String,
    val commodityNameOd: String,
    val currentRate: String,
    val unit: String,
    val qualitySpecification: String,
    val dailyTrend: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BhograiCashewGroundnutMandiSheet(
    language: AppLanguage,
    dailyPulse: DailyBalasorePulse,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    val commodities = listOf(
        CashewCommoditySpot(
            commodityNameEn = "Raw Cashew Nut (RCN)",
            commodityNameOd = "କଞ୍ଚା କାଜୁ ବାଦାମ (ଭୋଗରାଇ କ୍ଲଷ୍ଟର)",
            currentRate = "₹${dailyPulse.bhograiRawCashewRateKg}",
            unit = "per kg",
            qualitySpecification = "Out-turn KOR: ${dailyPulse.cashewOutTurnKor} • Moisture < 9%",
            dailyTrend = "▲ +₹2/kg from yesterday"
        ),
        CashewCommoditySpot(
            commodityNameEn = "Kharif Coastal Groundnut (Badam)",
            commodityNameOd = "ବାଲେଶ୍ୱର ଚିନାବାଦାମ (ମଣ୍ଡି ଦର)",
            currentRate = "₹${dailyPulse.bhograiGroundnutRateQuintal}",
            unit = "per quintal",
            qualitySpecification = "Oil content 48% • Jaleswar & Basta PACS tokens",
            dailyTrend = "▲ Steady demand"
        ),
        CashewCommoditySpot(
            commodityNameEn = "Processed Whole White Kernels (W-240)",
            commodityNameOd = "ପ୍ରୋସେସଡ୍ କାଜୁ (W-240 ପ୍ରିମିୟମ୍)",
            currentRate = "₹840",
            unit = "per kg",
            qualitySpecification = "Grade-A Jumbo Whole Kernels (Factory Gate)",
            dailyTrend = "● Stable wholesale"
        ),
        CashewCommoditySpot(
            commodityNameEn = "Cashew Nut Shell Liquid (CNSL)",
            commodityNameOd = "କାଜୁ ଚୋପା ତେଲ (ଶିଳ୍ପ ବ୍ୟବହାର)",
            currentRate = "₹58",
            unit = "per liter",
            qualitySpecification = "Friction material & marine varnish export grade",
            dailyTrend = "▲ High export demand"
        )
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .testTag("bhograi_cashew_mandi_sheet")
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
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFEF3C7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Agriculture,
                            contentDescription = "Cashew Icon",
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଭୋଗରାଇ କାଜୁ ଓ ଚିନାବାଦାମ ମଣ୍ଡି ରାଡାର୍" else "Bhograi Cashew & Groundnut Mandi",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "କଞ୍ଚା କାଜୁ RCN ଦର ଓ ମିଲ୍ ଡାଇରେକ୍ଟୋରି" else "Raw Cashew (RCN) Spot Index & Processing Mills",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = BentoSlate600,
                                fontSize = 11.5.sp
                            )
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("cashew_mandi_close_btn")
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = BentoSlate600)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Highlight Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (language == AppLanguage.ODIA) "ଆଜିର RCN ସ୍ପଟ୍ ଦର (ଭୋଗରାଇ କ୍ଲଷ୍ଟର)" else "Today's RCN Spot Rate (Bhograi)",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF92400E)
                                )
                            )
                            Text(
                                text = "₹${dailyPulse.bhograiRawCashewRateKg} / kg",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFB45309)
                                )
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFEF3C7),
                            border = BorderStroke(1.dp, Color(0xFFFCD34D))
                        ) {
                            Text(
                                text = "KOR 48-50 lbs",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = Color(0xFF78350F),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Groundnut Mandi: ₹${dailyPulse.bhograiGroundnutRateQuintal}/Q • Interstate Trade with Digha/Contai Active",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.5.sp,
                            color = Color(0xFF78350F)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = if (language == AppLanguage.ODIA) "ମଣ୍ଡି ସାମଗ୍ରୀ ସ୍ପଟ୍ ମୂଲ୍ୟ ତାଲିକା" else "Mandi Commodity Spot Rates",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = BentoSlate800
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(commodities) { item ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = BorderStroke(1.dp, BentoSlate200),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (language == AppLanguage.ODIA) item.commodityNameOd else item.commodityNameEn,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.5.sp,
                                            color = BentoSlate900
                                        )
                                    )
                                    Text(
                                        text = item.qualitySpecification,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 11.sp,
                                            color = BentoSlate600
                                        )
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = item.currentRate,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFFB45309)
                                        )
                                    )
                                    Text(
                                        text = item.unit,
                                        fontSize = 10.sp,
                                        color = BentoSlate500
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.dailyTrend,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 10.5.sp,
                                    color = Color(0xFF059669)
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:1551") // Kisan Call Centre
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("kisan_mandi_helpline_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == AppLanguage.ODIA) "କୃଷି ବିଜ୍ଞାନ କେନ୍ଦ୍ର ଓ କିଷାନ୍ ହେଲ୍ପଲାଇନ୍ (୧୫୫୧)" else "Call Kisan Agri Helpline (1551)",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
