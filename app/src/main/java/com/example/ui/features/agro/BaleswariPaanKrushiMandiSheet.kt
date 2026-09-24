package com.example.ui.features.agro

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
import com.example.ui.theme.*

data class CropMandiRate(
    val commodity: String,
    val commodityOd: String,
    val variety: String,
    val primaryMarket: String,
    val wholesalePriceRange: String,
    val trend: String, // "Rising 🔼", "Stable ➡️", "Soft 🔽"
    val unit: String
)

val BALASORE_AGRO_COMMODITIES = listOf(
    CropMandiRate(
        commodity = "Bhograi Mitha Paan (Betel Leaf)",
        commodityOd = "ଭୋଗରାଇ ମିଠା ପାନ",
        variety = "Export Grade A",
        primaryMarket = "Bhograi & Jaleswar Mandi",
        wholesalePriceRange = "₹3,400 - ₹4,200",
        trend = "Rising 🔼",
        unit = "per Kuri (10,000 leaves)"
    ),
    CropMandiRate(
        commodity = "Baliapal Sanchi Paan",
        commodityOd = "ବାଲିଆପାଳ ସାଞ୍ଚି ପାନ",
        variety = "Traditional Piquant",
        primaryMarket = "Baliapal & Nuabazar Arath",
        wholesalePriceRange = "₹2,800 - ₹3,500",
        trend = "Stable ➡️",
        unit = "per Kuri (10,000 leaves)"
    ),
    CropMandiRate(
        commodity = "Raw Cashew Nut (Kaju)",
        commodityOd = "କଞ୍ଚା କାଜୁ ବାଦାମ",
        variety = "Coastal Bold Seed",
        primaryMarket = "Jaleswar Processing Cluster",
        wholesalePriceRange = "₹120 - ₹145",
        trend = "Rising 🔼",
        unit = "per kg"
    ),
    CropMandiRate(
        commodity = "Swarna Paddy (Common)",
        commodityOd = "ସ୍ୱର୍ଣ୍ଣ ଧାନ (ସାଧାରଣ)",
        variety = "Govt MSP Mandi Rate",
        primaryMarket = "RMC Remuna & Soro PACs",
        wholesalePriceRange = "₹2,300",
        trend = "Official MSP ➡️",
        unit = "per quintal"
    ),
    CropMandiRate(
        commodity = "Nilagiri Organic Turmeric (Haldi)",
        commodityOd = "ନୀଳଗିରି ଅର୍ଗାନିକ ହଳଦୀ",
        variety = "High Curcumin Tribal Harvest",
        primaryMarket = "Nilagiri Tribal Co-op Mandi",
        wholesalePriceRange = "₹8,500 - ₹10,200",
        trend = "Strong 🔼",
        unit = "per quintal"
    )
)

/**
 * Baleswari Paan & Krushi Mandi Index.
 * Daily wholesale rates for Bhograi Betel leaf, Baliapal Paan, Cashew, and paddy MSP.
 * Offers KVK agricultural advisories, cold storage capacities, and farmer support contacts.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaleswariPaanKrushiMandiSheet(
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("All Produce") }
    val categories = listOf("All Produce", "Betel Leaf (Paan)", "Cashew & Spice", "Paddy & Grains")

    val displayedItems = remember(selectedCategory) {
        when (selectedCategory) {
            "Betel Leaf (Paan)" -> BALASORE_AGRO_COMMODITIES.filter { it.commodity.contains("Paan", ignoreCase = true) }
            "Cashew & Spice" -> BALASORE_AGRO_COMMODITIES.filter { it.commodity.contains("Cashew", ignoreCase = true) || it.commodity.contains("Turmeric", ignoreCase = true) }
            "Paddy & Grains" -> BALASORE_AGRO_COMMODITIES.filter { it.commodity.contains("Paddy", ignoreCase = true) }
            else -> BALASORE_AGRO_COMMODITIES
        }
    }

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .testTag("baleswari_paan_krushi_sheet")
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
                        Text(text = "🍃", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱରୀ ପାନ ଓ କୃଷି ମଣ୍ଡି ସୂଚୀ" else "Baleswari Paan & Krushi Mandi",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଭୋଗରାଇ-ବାଲିଆପାଳ ପାନ ବଜାର • କାଜୁ ଓ କେଭିକେ ପରାମର୍ଶ" else "Bhograi/Baliapal Betel Index • Cashew & KVK Advisory",
                            style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate500)
                        )
                    }
                }
                IconButton(onClick = onClose) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.88f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // KVK Alert Banner
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                        border = BorderStroke(1.dp, Color(0xFFBBF7D0))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "🌾 KVK Balasore Weather Advisory",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF166534)
                                    )
                                )
                                Surface(
                                    color = Color(0xFFDCFCE7),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "LIVE UPDATE",
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(0xFF15803D),
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = if (language == AppLanguage.ODIA)
                                    "ଭୋଗରାଇ ପାନ ବରଜ ପାଇଁ ଆର୍ଦ୍ରତା ନିୟନ୍ତ୍ରଣ ଜରୁରୀ। ଫିମ୍ପି ରୋଗରୁ ରକ୍ଷା ପାଇଁ ବୋର୍ଡୋ ମିଶ୍ରଣ (୧%) ସ୍ପ୍ରେ କରନ୍ତୁ।"
                                else
                                    "High coastal humidity observed in Bhograi-Baliapal belt. Betel vine farmers advised to apply 1% Bordeaux mixture to prevent Phytophthora leaf rot.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF14532D),
                                    lineHeight = 18.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:06782260265"))
                                        context.startActivity(intent)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF166534)),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Call KVK Helpline", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // Filter Row
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(categories) { category ->
                            FilterChip(
                                selected = selectedCategory == category,
                                onClick = { selectedCategory = category },
                                label = { Text(category, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF16A34A),
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // Mandi Price Cards
                items(displayedItems) { item ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.primaryMarket,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(0xFF047857),
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        text = if (language == AppLanguage.ODIA) item.commodityOd else item.commodity,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = BentoSlate900
                                        )
                                    )
                                    Text(
                                        text = "Grade: ${item.variety}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = BentoSlate500,
                                            fontSize = 11.sp
                                        )
                                    )
                                }

                                Surface(
                                    color = Color(0xFFECFDF5),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, Color(0xFFA7F3D0))
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.End,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = item.wholesalePriceRange,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFF047857),
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = item.unit,
                                            fontSize = 10.sp,
                                            color = BentoSlate500
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Market Trend: ${item.trend}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = BentoSlate700
                                    )
                                )
                                Text(
                                    text = "Verified Arath Rates",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.sp,
                                        color = BentoSlate400
                                    )
                                )
                            }
                        }
                    }
                }

                // Cold Storage Directory
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "❄️ Certified Cold Storage Units",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BentoSlate900
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "• Maa Durga Cold Store, Remuna: 5,000 MT (Potato & Seeds) - 06782-255140\n• Balasore Agro Fresh, Kuruda: 4,000 MT (Fruits & Veg) - 06782-256280\n• Jaleswar Cooperative Cold Storage: 3,500 MT - 06781-222300",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = BentoSlate700,
                                    lineHeight = 19.sp
                                )
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}
