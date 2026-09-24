package com.example.ui.features.artisans

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

data class ArtisanCraftItem(
    val craftName: String,
    val craftNameOd: String,
    val clusterLocation: String,
    val rawMaterial: String,
    val typicalItems: String,
    val priceRange: String,
    val cooperativeName: String,
    val contactPhone: String
)

val NILAGIRI_ARTISAN_CRAFTS = listOf(
    ArtisanCraftItem(
        craftName = "Nilagiri Green Stone Utensils (Pathara Kunda)",
        craftNameOd = "ନୀଳଗିରି ସବୁଜ ପଥର କୁଣ୍ଡ ଓ ବାସନ",
        clusterLocation = "Baunsabania & Mitrapur, Nilagiri",
        rawMaterial = "Green Serpentine Chlorite Stone",
        typicalItems = "Cooking Handi, curd pots, medicinal grinding mortars (Kundi & Khala)",
        priceRange = "₹350 - ₹2,500",
        cooperativeName = "Nilagiri Shilpi Audyogika Samabaya",
        contactPhone = "9437881023"
    ),
    ArtisanCraftItem(
        craftName = "Temple Sculptures & Idols",
        craftNameOd = "ମନ୍ଦିର ମୂର୍ତ୍ତି ଓ ପଥର ଖୋଦେଇ କଳା",
        clusterLocation = "Nilagiri Town & Srijang",
        rawMaterial = "Hard Grey Granite & Sandstone",
        typicalItems = "Jagannath deities, Konark style dancing apsaras, garden fountains",
        priceRange = "₹1,500 - ₹45,000",
        cooperativeName = "Utkal Stone Carvers Guild",
        contactPhone = "9861340912"
    ),
    ArtisanCraftItem(
        craftName = "Balasore Dokra Brass Casting",
        craftNameOd = "ବାଲେଶ୍ୱର ଢୋକ୍ରା ପିତ୍ତଳ କାରୁକାର୍ଯ୍ୟ",
        clusterLocation = "Kuldiha Foothills & Khantapara",
        rawMaterial = "Lost-wax cast bell metal & brass",
        typicalItems = "Tribal figurines, elephant oil lamps, peacock diyas, wall medallions",
        priceRange = "₹450 - ₹6,000",
        cooperativeName = "Kuldiha Dokra Artisan Sangha",
        contactPhone = "9938127456"
    ),
    ArtisanCraftItem(
        craftName = "Traditional Golden Grass & Cane Weaving",
        craftNameOd = "କାଇଁଚ ଘାସ ଓ ବେତ ବୁଣା ହସ୍ତତନ୍ତ",
        clusterLocation = "Bahanaga & Remuna Craft Cluster",
        rawMaterial = "Wild riparian Golden Grass (Kaincha)",
        typicalItems = "Storage baskets, dining mats, laptop sleeves, eco jewelry boxes",
        priceRange = "₹120 - ₹800",
        cooperativeName = "Bahanaga Mahila Vikas Samiti",
        contactPhone = "9439054128"
    )
)

/**
 * Nilagiri Stone Craft & Dokra Artisan Showcase.
 * Fair-trade direct-to-artisan registry for Nilagiri green chlorite stone cookware,
 * temple carvings, and Dokra metalcraft with verified cooperative contacts.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NilagiriStoneArtisanSheet(
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .testTag("nilagiri_stone_artisan_sheet")
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
                            .background(Color(0xFFE2E8F0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🏺", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ନୀଳଗିରି ପଥର ଓ ଢୋକ୍ରା ହସ୍ତଶିଳ୍ପ" else "Nilagiri Stone & Dokra Craft",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ସବୁଜ ପଥର ବାସନ • ଶିଳ୍ପୀ ସମବାୟ ସିଧାସଳଖ ସଂଯୋଗ" else "Green Chlorite Cookware • Direct Artisan Connect",
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
                // GI Heritage Info Card
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                        border = BorderStroke(1.dp, Color(0xFFCBD5E1))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "✨ Centuries-Old Non-Toxic Cookware Heritage",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BentoSlate800
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Nilagiri green chlorite stone utensils retain food nutrients, require zero oil seasoning, and remain completely non-reactive to sour gravies (Ambila & Dahi). Sourced directly from local tribal cooperatives.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = BentoSlate600,
                                    lineHeight = 18.sp
                                )
                            )
                        }
                    }
                }

                // Crafts Cards
                items(NILAGIRI_ARTISAN_CRAFTS) { craft ->
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
                                        text = craft.cooperativeName,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(0xFF0F766E),
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        text = if (language == AppLanguage.ODIA) craft.craftNameOd else craft.craftName,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = BentoSlate900
                                        )
                                    )
                                    Text(
                                        text = "📍 ${craft.clusterLocation}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = BentoSlate500,
                                            fontSize = 11.sp
                                        )
                                    )
                                }

                                Surface(
                                    color = Color(0xFFF8FAFC),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                                ) {
                                    Text(
                                        text = craft.priceRange,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFF0F766E)
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "🪨 Material: ${craft.rawMaterial}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = BentoSlate700
                                )
                            )
                            Text(
                                text = "🛍️ Items: ${craft.typicalItems}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = BentoSlate600,
                                    fontSize = 12.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Middleman-free pricing",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF15803D),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${craft.contactPhone}"))
                                        context.startActivity(intent)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = OceanBlue),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Contact Artisan", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}
