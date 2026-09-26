package com.example.ui.features.agro

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.daily.DailyBalasorePulse
import com.example.data.model.AppLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalaramgadiMandiPriceIndexSheet(
    language: AppLanguage,
    dailyPulse: DailyBalasorePulse,
    onDismiss: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedItemForTrend by remember { mutableStateOf<MandiItem?>(null) }

    val commodities = remember(dailyPulse) {
        listOf(
            MandiItem(
                id = "hilsa_1",
                nameEn = "Subarnarekha Hilsa (Ilish)",
                nameOd = "ସୁବର୍ଣ୍ଣରେଖା ଇଲିଶି ମାଛ",
                category = "Marine Seafood",
                unit = "per kg",
                currentPrice = dailyPulse.hilsaEstuaryRateKg,
                previousPrice = dailyPulse.hilsaEstuaryRateKg - 40,
                priceChangeText = "+₹40/kg 🔺",
                isPriceUp = true,
                freshness = "Freshly landed 05:30 AM at Balaramgadi",
                mandiSource = "Balaramgadi Fish Harbor",
                trend7Days = listOf(880, 900, 920, 910, 940, 960, dailyPulse.hilsaEstuaryRateKg)
            ),
            MandiItem(
                id = "tiger_prawn_1",
                nameEn = "Black Tiger Prawn (Bagha Chingudi)",
                nameOd = "ବାଘ ଚିଙ୍ଗୁଡ଼ି (ବଳରାମଗଡ଼ି)",
                category = "Marine Seafood",
                unit = "per kg",
                currentPrice = dailyPulse.tigerPrawnRateKg,
                previousPrice = dailyPulse.tigerPrawnRateKg + 20,
                priceChangeText = "-₹20/kg 🔻",
                isPriceUp = false,
                freshness = "A-Grade Estuary Catch",
                mandiSource = "Kasafal Landing Ghat",
                trend7Days = listOf(780, 760, 750, 740, 730, 720, dailyPulse.tigerPrawnRateKg)
            ),
            MandiItem(
                id = "vannamei_1",
                nameEn = "Vannamei White Shrimp",
                nameOd = "ଭାନାମି ଧଳା ଚିଙ୍ଗୁଡ଼ି",
                category = "Marine Seafood",
                unit = "per kg",
                currentPrice = dailyPulse.aquacultureVannameiRateKg,
                previousPrice = dailyPulse.aquacultureVannameiRateKg,
                priceChangeText = "Stable 🟢",
                isPriceUp = null,
                freshness = "Saline Aqua Farm Gate (Count 30)",
                mandiSource = "Bhograi Aqua Cluster",
                trend7Days = listOf(360, 370, 365, 380, 375, 380, dailyPulse.aquacultureVannameiRateKg)
            ),
            MandiItem(
                id = "white_pomfret_1",
                nameEn = "Silver / White Pomfret (Chandi Machha)",
                nameOd = "ଚାନ୍ଦି ପାମ୍ପଲେଟ୍ ମାଛ",
                category = "Marine Seafood",
                unit = "per kg",
                currentPrice = 580,
                previousPrice = 560,
                priceChangeText = "+₹20/kg 🔺",
                isPriceUp = true,
                freshness = "Deep sea gillnetted (500g+ size)",
                mandiSource = "Dhamra Port Landing",
                trend7Days = listOf(540, 550, 550, 560, 570, 560, 580)
            ),
            MandiItem(
                id = "mud_crab_1",
                nameEn = "Live Estuary Mud Crab",
                nameOd = "ଜୀବନ୍ତ କାଦୁଆ ପଥୁରିଆ କଙ୍କଡ଼ା",
                category = "Marine Seafood",
                unit = "per kg",
                currentPrice = 520,
                previousPrice = 530,
                priceChangeText = "-₹10/kg 🔻",
                isPriceUp = false,
                freshness = "Live Tied Crabs (Chaumukha)",
                mandiSource = "Bichitrapur Mangrove Depot",
                trend7Days = listOf(550, 540, 530, 540, 530, 520, 520)
            ),
            MandiItem(
                id = "swarna_paddy_1",
                nameEn = "Swarna Paddy (Govt MSP Procurement)",
                nameOd = "ସ୍ୱର୍ଣ୍ଣ ଧାନ ସରକାରୀ ଏମଏସପି",
                category = "Agri Produce",
                unit = "per Quintal",
                currentPrice = dailyPulse.swarnaPaddyMspQuintal,
                previousPrice = dailyPulse.swarnaPaddyMspQuintal,
                priceChangeText = "Govt Fixed MSP",
                isPriceUp = null,
                freshness = "FAQ Quality (14% Moisture Cap)",
                mandiSource = "Balasore Regulated Market (RMC)",
                trend7Days = listOf(2183, 2183, 2183, 2183, 2183, 2183, 2183)
            ),
            MandiItem(
                id = "baleswari_paan_1",
                nameEn = "Baleswari Maghai Betel Leaf (Paan)",
                nameOd = "ବାଲେଶ୍ୱରୀ ମଘଇ ପାନ (୧୦୦ ବିଡ଼ା)",
                category = "Agri Produce",
                unit = "per 100 Leaves (Bida)",
                currentPrice = dailyPulse.baleswariPaanBida100Rate,
                previousPrice = dailyPulse.baleswariPaanBida100Rate - 15,
                priceChangeText = "+₹15/Bida 🔺",
                isPriceUp = true,
                freshness = "Fresh plucked from Bhograi Pan Baraja",
                mandiSource = "Bhograi & Baliapal Mandi",
                trend7Days = listOf(135, 140, 145, 150, 150, 155, dailyPulse.baleswariPaanBida100Rate)
            ),
            MandiItem(
                id = "remuna_potala_1",
                nameEn = "Remuna Pointed Gourd (Potala)",
                nameOd = "ରେମୁଣା ସଦ୍ୟ ପୋଟଳ",
                category = "Agri Produce",
                unit = "per kg",
                currentPrice = 38,
                previousPrice = 42,
                priceChangeText = "-₹4/kg 🔻",
                isPriceUp = false,
                freshness = "Organic riverbank silt harvest",
                mandiSource = "Remuna Sub-Jail Hat",
                trend7Days = listOf(45, 42, 40, 42, 40, 38, 38)
            ),
            MandiItem(
                id = "bhograi_kaju_1",
                nameEn = "Bhograi Farm-Gate Raw Cashew (Kaju)",
                nameOd = "ଭୋଗରାଇ କଞ୍ଚା କାଜୁ ବାଦାମ",
                category = "Agri Produce",
                unit = "per kg",
                currentPrice = 680,
                previousPrice = 670,
                priceChangeText = "+₹10/kg 🔺",
                isPriceUp = true,
                freshness = "Sun-dried whole coastal nuts",
                mandiSource = "Jaleswar RMC Yard",
                trend7Days = listOf(660, 660, 670, 670, 680, 670, 680)
            )
        )
    }

    val filteredItems = remember(commodities, selectedCategory) {
        if (selectedCategory == "All") commodities
        else commodities.filter { it.category == selectedCategory }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ମଣ୍ଡି ଓ ବଳରାମଗଡ଼ି ମତ୍ସ୍ୟ ଦର 🦐" else "Mandi & Fish Landing Price Index 🦐",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବଳରାମଗଡ଼ି, କସାଫାଳ ମତ୍ସ୍ୟ ବନ୍ଦର ଓ RMC କୃଷି ଦୈନିକ ଦର" else "Daily Seafood Landing Rates & RMC Agricultural Spot Prices",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            }

            // Summary Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("TODAY'S MARKET PULSE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Text("Auto-Updated Daily", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            Text("Date: ${dailyPulse.formattedDate}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Badge(containerColor = Color(0xFF16A34A)) {
                            Text("LIVE AUCTIONS ACTIVE", color = Color.White, fontSize = 10.sp, modifier = Modifier.padding(4.dp))
                        }
                    }
                }
            }

            // Filter Chips
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = selectedCategory == "All",
                        onClick = { selectedCategory = "All" },
                        label = { Text("All Commodities") }
                    )
                    FilterChip(
                        selected = selectedCategory == "Marine Seafood",
                        onClick = { selectedCategory = "Marine Seafood" },
                        label = { Text("Seafood 🐟") }
                    )
                    FilterChip(
                        selected = selectedCategory == "Agri Produce",
                        onClick = { selectedCategory = "Agri Produce" },
                        label = { Text("Agri & Spices 🌾") }
                    )
                }
            }

            // 7-Day Trend Chart Preview if item selected
            selectedItemForTrend?.let { item ->
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "7-Day Price Trend: ${item.nameEn}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                IconButton(
                                    onClick = { selectedItemForTrend = null },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = null, tint = Color.White)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                val max = item.trend7Days.maxOrNull() ?: 1
                                item.trend7Days.forEachIndexed { idx, price ->
                                    val barHeight = ((price.toFloat() / max.toFloat()) * 60).dp
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Bottom
                                    ) {
                                        Text("₹$price", color = Color(0xFF94A3B8), fontSize = 9.sp)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Box(
                                            modifier = Modifier
                                                .width(20.dp)
                                                .height(barHeight)
                                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                                .background(if (idx == 6) Color(0xFF38BDF8) else Color(0xFF475569))
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text("D-${6 - idx}", color = Color(0xFF64748B), fontSize = 9.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Commodity Cards
            items(filteredItems) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedItemForTrend = item },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) item.nameOd else item.nameEn,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = item.mandiSource,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "₹${item.currentPrice}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(item.unit, fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "✨ ${item.freshness}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.weight(1f)
                            )
                            Badge(
                                containerColor = when (item.isPriceUp) {
                                    true -> Color(0xFFFEF2F2)
                                    false -> Color(0xFFF0FDF4)
                                    else -> Color(0xFFF8FAFC)
                                }
                            ) {
                                Text(
                                    text = item.priceChangeText,
                                    color = when (item.isPriceUp) {
                                        true -> Color(0xFFDC2626)
                                        false -> Color(0xFF16A34A)
                                        else -> Color(0xFF475569)
                                    },
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(2.dp)
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

private data class MandiItem(
    val id: String,
    val nameEn: String,
    val nameOd: String,
    val category: String,
    val unit: String,
    val currentPrice: Int,
    val previousPrice: Int,
    val priceChangeText: String,
    val isPriceUp: Boolean?,
    val freshness: String,
    val mandiSource: String,
    val trend7Days: List<Int>
)
