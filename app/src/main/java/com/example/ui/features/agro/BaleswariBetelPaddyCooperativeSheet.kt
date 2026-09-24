package com.example.ui.features.agro

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.daily.DailyBalasorePulse
import com.example.data.model.AppLanguage
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate200
import com.example.ui.theme.BentoSlate600
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate900
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark

data class AgroCooperativeCrop(
    val id: String,
    val cropNameEn: String,
    val cropNameOd: String,
    val category: String,
    val primaryPocket: String,
    val wholesalePriceToday: String,
    val demandTrend: String,
    val soilSalinityTolerance: String,
    val cooperativeSupport: String
)

val DEFAULT_AGRO_CROPS = listOf(
    AgroCooperativeCrop(
        id = "crop_1",
        cropNameEn = "Baleswari Maghai & Sanchi Pan (Betel Leaf)",
        cropNameOd = "ବାଲେଶ୍ୱରୀ ମଘଇ ଓ ସାଞ୍ଚି ପାନ",
        category = "Cash Crop / Pan Baraja",
        primaryPocket = "Bhograi & Baliapal Coastal Belt",
        wholesalePriceToday = "₹12,400 per Dholi (10,000 leaves)",
        demandTrend = "High Demand (Varanasi & Kolkata Export)",
        soilSalinityTolerance = "Medium (Requires shade Baraja)",
        cooperativeSupport = "Odisha Agro Industries Cold Logistics Subsidy"
    ),
    AgroCooperativeCrop(
        id = "crop_2",
        cropNameEn = "Luna Sankhi (Saline Inundation Resistant Paddy)",
        cropNameOd = "ଲୁଣା ଶଙ୍ଖି ଲବଣ-ସହନଶୀଳ ଧାନ",
        category = "Coastal Saline Heirloom",
        primaryPocket = "Kasafal & Basta Estuary Margins",
        wholesalePriceToday = "₹2,360 per Quintal (MSP)",
        demandTrend = "Stable MSP Procurement via PACS",
        soilSalinityTolerance = "High (Up to 8.5 dS/m salinity)",
        cooperativeSupport = "Certified Seed Kits via Balasore Krishi Vigyan Kendra"
    ),
    AgroCooperativeCrop(
        id = "crop_3",
        cropNameEn = "Kanakchur Aromatic Fine Rice (Muan Special)",
        cropNameOd = "କନକଚୂଡ଼ ସୁଗନ୍ଧିତ ଧାନ (ମୁଆଁ ସ୍ୱତନ୍ତ୍ର)",
        category = "GI Heritage Grain",
        primaryPocket = "Soro & Oupada Valley",
        wholesalePriceToday = "₹4,800 per Quintal",
        demandTrend = "Surging Festive Demand for Puffed Rice",
        soilSalinityTolerance = "Low (Fresh alluvial soil)",
        cooperativeSupport = "Direct Farm-to-Confectioner Market Link"
    ),
    AgroCooperativeCrop(
        id = "crop_4",
        cropNameEn = "Coastal Pointed Gourd (Baleswari Potala)",
        cropNameOd = "ବାଲେଶ୍ୱରୀ ଦେଶୀ ପୋଟଳ",
        category = "Riverbank Horticulture",
        primaryPocket = "Budhabalanga Sandy Loam Riverbeds",
        wholesalePriceToday = "₹42 per kg Wholesale",
        demandTrend = "Brisk Daily Mandi Inflow",
        soilSalinityTolerance = "Low (River alluvial silt)",
        cooperativeSupport = "FPO Direct Pickup at Sahadevkhunta Mandi"
    )
)

/**
 * Mati-Miras Baleswari Betel Leaf & Saline Paddy Cooperative Radar.
 */
@Composable
fun BaleswariBetelPaddyCooperativeSheet(
    dailyPulse: DailyBalasorePulse,
    language: AppLanguage = AppLanguage.ENGLISH,
    onDismiss: () -> Unit
) {
    var lastUpdate by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BentoSlate100)
            .testTag("betel_paddy_cooperative_sheet")
    ) {
        // Hero Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF064E3B), Color(0xFF065F46), EmeraldGreen)
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF34D399).copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, Color(0xFF34D399).copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "AGRO MANDI TELEMETRY • BHOGRAI & BALIAPAL",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp,
                                    fontSize = 9.5.sp,
                                    color = Color(0xFFA7F3D0)
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱରୀ ପାନ ବରଜ ଓ ଲୁଣା ଧାନ ସମବାୟ ରାଡାର୍" else "Baleswari Betel & Saline Paddy Radar",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (language == AppLanguage.ODIA)
                            "ଭୋଗରାଇ-ବାଲିଆପାଳ ପାନ ବରଜ ରପ୍ତାନି ଦର, ଲୁଣା ଶଙ୍ଖି ଧାନ ଏବଂ ସମବାୟ ମଣ୍ଡିର ଦୈନିକ ଦର ଅପଡେଟ୍"
                        else
                            "Real-time betel leaf export rates, saline-tolerant coastal paddy procurement, and PACS cooperative schedules.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFE2E8F0),
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.12f))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Paddy Stage: " + dailyPulse.heirloomPaddyGrowthStage,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { lastUpdate = System.currentTimeMillis() },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        items(DEFAULT_AGRO_CROPS) { crop ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BentoSlate200),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (language == AppLanguage.ODIA) crop.cropNameOd else crop.cropNameEn,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BentoSlate900
                                )
                            )
                            Text(
                                text = "${crop.category} • ${crop.primaryPocket}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF047857),
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = EmeraldGreen.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, EmeraldGreen)
                        ) {
                            Text(
                                text = crop.demandTrend.take(16),
                                color = EmeraldGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = BentoSlate200)
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Wholesale Rate: ${crop.wholesalePriceToday}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = BentoSlate900
                    )
                    Text(
                        text = "Salinity Tolerance: ${crop.soilSalinityTolerance}",
                        fontSize = 11.sp,
                        color = BentoSlate700
                    )
                    Text(
                        text = "Cooperative Scheme: ${crop.cooperativeSupport}",
                        fontSize = 11.sp,
                        color = BentoSlate600
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
