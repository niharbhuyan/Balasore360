package com.example.ui.features.heritage

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.daily.DailyBalasorePulse
import com.example.data.model.AppLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaleswariCuisineExplorerSheet(
    language: AppLanguage,
    dailyPulse: DailyBalasorePulse,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    val cuisineItems = listOf(
        DelicacyItem(
            nameEn = "Remuna Amruta Keli Khira Bhog",
            nameOd = "ରେମୁଣା ଅମୃତ କେଳି ଖିରଭୋଗ",
            origin = "Remuna Khirachora Gopinath Temple",
            description = "Legendary condensed milk dessert slowly simmered in clay pots with pure cardamom and saffron.",
            bestShop = "Temple Trust Prasad Counter & Remuna Bazar",
            priceEst = "₹80 - ₹120 per earthen bowl"
        ),
        DelicacyItem(
            nameEn = "Balasore Nabadwip / Kheer Sandesh",
            nameOd = "ବାଲେଶ୍ୱର ନବଦ୍ୱୀପ ସନ୍ଦେଶ",
            origin = "Motiganj & Cinema Chhak Confectioners",
            description = "Silky melt-in-mouth cottage cheese sweet rooted in Bengal-Odisha confluence heritage.",
            bestShop = "Historic Confectioners, Motiganj Bazar",
            priceEst = "₹360 - ₹440 per kg"
        ),
        DelicacyItem(
            nameEn = "Baleswari Mudhi-Mansha",
            nameOd = "ବାଲେଶ୍ୱରୀ ମୁଢ଼ି ମାଂସ",
            origin = "Baripada & Balasore Heritage Dhabas",
            description = "Crisp organic puffed rice served with rich, slow-simmered spicy mutton gravy and chopped onions.",
            bestShop = "Station Road & OT Road Heritage Dhabas",
            priceEst = "₹180 - ₹240 per plate"
        ),
        DelicacyItem(
            nameEn = "Balaramgadi Smoked Hilsa Curry (Ilish Machha)",
            nameOd = "ବଳରାମଗଡ଼ି ଇଲିଶି ଝୋଳ",
            origin = "Budhabalanga Estuary & Marine Harbors",
            description = "Fresh estuary Hilsa cooked with aromatic yellow mustard paste (Besara) and green chilies.",
            bestShop = "Chandipur Sea Front & Kasafal Dhabas",
            priceEst = "₹280 - ₹380 per serving"
        ),
        DelicacyItem(
            nameEn = "Soro Chhena Gaja",
            nameOd = "ସୋରୋ ଛେନା ଗଜା",
            origin = "Soro Town Market",
            description = "Deep caramel crust paneer cake soaked in mild cardamom sugar syrup.",
            bestShop = "Soro NH-16 Confectioneries",
            priceEst = "₹260 - ₹320 per kg"
        )
    )

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
                            text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ଖାଦ୍ୟ ଓ ମିଠା ପରିକ୍ରମା 🥘" else "Baleswari Cuisine & Sweet Trails 🥘",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ରେମୁଣା ଖିରଭୋଗ, ମୁଢ଼ି ମାଂସ, ନବଦ୍ୱୀପ ସନ୍ଦେଶ ଓ ଐତିହ୍ୟ ଦୋକାନ" else "Heritage Sweets, Clay Pot Khira & Iconic Seafood Trails",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            }

            // Spotlight Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF78350F)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("DAILY CULINARY SPOTLIGHT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFDE68A))
                            Badge(containerColor = Color(0xFFF59E0B)) {
                                Text("${dailyPulse.sweetShopsOpenCount} SHOPS OPEN", color = Color(0xFF78350F), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(dailyPulse.cuisineSpotlightItem, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }

            // Cuisine Items
            items(cuisineItems) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (language == AppLanguage.ODIA) item.nameOd else item.nameEn,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Badge(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                                Text(item.priceEst, fontSize = 10.sp, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                        Text("Origin: ${item.origin}", fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(item.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 16.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("📍 Where to find: ${item.bestShop}", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

private data class DelicacyItem(
    val nameEn: String,
    val nameOd: String,
    val origin: String,
    val description: String,
    val bestShop: String,
    val priceEst: String
)
