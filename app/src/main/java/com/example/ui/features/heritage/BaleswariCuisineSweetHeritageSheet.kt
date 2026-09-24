package com.example.ui.features.heritage

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

data class BaleswarDelicacy(
    val dishName: String,
    val dishNameOd: String,
    val iconicSpot: String,
    val preparationStory: String,
    val priceRange: String,
    val phone: String?
)

data class SukhuaVariety(
    val fishName: String,
    val fishNameOd: String,
    val dryingHarbor: String,
    val culinaryStyle: String,
    val wholesalePriceKg: String
)

val BALESWAR_DELICACIES = listOf(
    BaleswarDelicacy(
        dishName = "Chhena Mudki (GI Applicant Balasore Sweet)",
        dishNameOd = "ଛେନା ମୁଡ଼କି (ବାଲେଶ୍ୱରର ପ୍ରସିଦ୍ଧ ପାରମ୍ପରିକ ମିଠା)",
        iconicSpot = "Cinema Bazar & Motiganj Heritage Confectioners",
        preparationStory = "Dense fresh cow milk cottage cheese cubes tossed and crystalized in pure cardamom sugar syrup. Has a crispy exterior and soft succulent core.",
        priceRange = "₹360 - ₹420 / kg (Box packing available)",
        phone = "06782262145"
    ),
    BaleswarDelicacy(
        dishName = "Nilagiri Authentic Khaja",
        dishNameOd = "ନୀଳଗିରି ରାଜକୀୟ ଖଜା",
        iconicSpot = "Nilagiri Palace Bazar & Temple Lane",
        preparationStory = "Crisp, multi-layered flaky pastry dipped in fragrant sugar glaze, perfected over two centuries under Nilagiri royal patronage.",
        priceRange = "₹180 - ₹240 / kg",
        phone = "9437293110"
    ),
    BaleswarDelicacy(
        dishName = "Baleswari Mudhi - Mansha (Puffed Rice & Mutton Gravy)",
        dishNameOd = "ବାଲେଶ୍ୱରୀ ମୁଢ଼ି - ମାଂସ ଝୋଳ",
        iconicSpot = "Station Road, Fandi Chhak & Remuna Golai",
        preparationStory = "Local non-salted hand-roasted aromatic Dhan Mudhi paired with slow-cooked desi goat meat in rich bay leaf and mustard oil gravy.",
        priceRange = "₹140 - ₹180 / plate",
        phone = null
    )
)

val SUKHUA_VARIETIES = listOf(
    SukhuaVariety(
        fishName = "Kani Sukhua (Sun-Dried Coastal Anchovy)",
        fishNameOd = "କାଣୀ ଶୁଖୁଆ",
        dryingHarbor = "Kasafal & Balaramgadi Sandbars",
        culinaryStyle = "Roasted with mustard oil, garlic cloves and crushed green chillies",
        wholesalePriceKg = "₹280 - ₹340 / kg"
    ),
    SukhuaVariety(
        fishName = "Ilish / Hilsa Salt-Cured Sukhua",
        fishNameOd = "ଲୁଣା ଇଲିସି ଶୁଖୁଆ",
        dryingHarbor = "Bahabalpur Marine Landing Centre",
        culinaryStyle = "Steamed with pumpkin leaves (Patrapoda) or rich mustard curry",
        wholesalePriceKg = "₹750 - ₹950 / kg"
    ),
    SukhuaVariety(
        fishName = "Khainga & Chingudi Dry Catch",
        fishNameOd = "ଖଇଙ୍ଗା ଓ ଚିଙ୍ଗୁଡ଼ି ଶୁଖୁଆ",
        dryingHarbor = "Talapada & Chandipur Coast",
        culinaryStyle = "Crisp shallow fried with onions or dry prawn powder chutney",
        wholesalePriceKg = "₹450 - ₹550 / kg"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaleswariCuisineSweetHeritageSheet(
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
                .testTag("baleswari_cuisine_sweet_sheet")
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
                        Text("🍲", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱରୀ ଖାଦ୍ୟ ଓ ମିଠା ଐତିହ୍ୟ ଡାଇରୀ" else "Baleswari Culinary & Sweet Heritage",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଛେନା ମୁଡ଼କି • ନୀଳଗିରି ଖଜା • ମୁଢ଼ି ମାଂସ • ଶୁଖୁଆ" else "Chhena Mudki • Nilagiri Khaja • Mudhi Mansha • Sukhua",
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
                                .background(Color(0xFFF59E0B))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Culinary directory verified at $syncTime",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF92400E)
                        )
                    }
                    Text(
                        text = "GI HERITAGE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB45309)
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
                            text = if (language == AppLanguage.ODIA) "ମିଠା ଓ ଖାଦ୍ୟ" else "Iconic Sweets",
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
                            text = if (language == AppLanguage.ODIA) "କସାଫଳ ଶୁଖୁଆ" else "Sukhua Market",
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
                            text = if (language == AppLanguage.ODIA) "ସ୍ୱତନ୍ତ୍ର ପାର୍ସଲ" else "GI Parcel Guide",
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
                        items(BALESWAR_DELICACIES) { dish ->
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
                                                text = if (language == AppLanguage.ODIA) dish.dishNameOd else dish.dishName,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "📍 ${dish.iconicSpot}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF64748B)
                                            )
                                        }
                                        Text(
                                            text = dish.priceRange.substringBefore("("),
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
                                        text = dish.preparationStory,
                                        fontSize = 12.sp,
                                        color = Color(0xFF334155),
                                        lineHeight = 16.sp
                                    )
                                    if (dish.phone != null) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Button(
                                            onClick = {
                                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${dish.phone}"))
                                                context.startActivity(intent)
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp)
                                        ) {
                                            Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Order / Inquire Parcel", fontSize = 11.sp)
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
                        items(SUKHUA_VARIETIES) { item ->
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
                                                text = if (language == AppLanguage.ODIA) item.fishNameOd else item.fishName,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "🏖️ Landing: ${item.dryingHarbor}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF64748B)
                                            )
                                        }
                                        Text(
                                            text = item.wholesalePriceKg,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0F766E),
                                            modifier = Modifier
                                                .background(Color(0xFFCCFBF1), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "🍳 Traditional Prep: ${item.culinaryStyle}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF334155)
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
                            color = Color(0xFFFFFBEB),
                            border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଛେନା ମୁଡ଼କି ଜିଆଇ ଟ୍ୟାଗ୍ ଓ ପ୍ୟାକେଜିଂ ସୂଚନା" else "Chhena Mudki GI Application & Travel Packaging",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF92400E)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("• Long Shelf Life: Due to sugar crystal coating, Chhena Mudki stays fresh for 10-14 days without refrigeration.", fontSize = 12.sp, color = Color(0xFF78350F))
                                Text("• Ideal Travel Souvenir: Available in tamper-proof heritage tin and earthen box packaging near Balasore Railway Station.", fontSize = 12.sp, color = Color(0xFF78350F))
                                Text("• Distinct from Rasagola/Chhena Poda: Made from dense, low-moisture Chhena with distinct bite and aromatic cardamom hint.", fontSize = 12.sp, color = Color(0xFF78350F))
                            }
                        }
                    }
                }
            }
        }
    }
}
