package com.example.ui.features.heritage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dining
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.openInGoogleMaps

data class FoodTrailItem(
    val id: String,
    val name: String,
    val odiaName: String,
    val category: String, // Prasad, Seafood, Sweets, Chaat
    val description: String,
    val landmarkShop: String,
    val priceEstimate: String,
    val latitude: Double,
    val longitude: Double,
    val tagColor: Long = 0xFFD97706
)

/**
 * Balasore Food Trail (Amrita Keli & Seafood Explorer) component.
 */
@Composable
fun BalasoreFoodTrailSheet(
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("All") }

    val foodItems = remember {
        listOf(
            FoodTrailItem(
                id = "food_1",
                name = "Khirachora Amrita Keli (ଅମୃତ କେଳି)",
                odiaName = "କ୍ଷୀରଚୋରା ଗୋପୀନାଥ ଅମୃତ କେଳି",
                category = "Prasad",
                description = "Legendary 12th-century condensed milk offering. Slow-simmered pure cow milk condensed into earthen pots with pure ghee, sugar, and crushed green cardamom.",
                landmarkShop = "Temple Anandabazar, Khirachora Gopinath Temple, Remuna",
                priceEstimate = "₹60 - ₹120 per earthen pot",
                latitude = 21.5284,
                longitude = 86.8647,
                tagColor = 0xFFD97706
            ),
            FoodTrailItem(
                id = "food_2",
                name = "Balaramgadi Harbor Fresh Fish Fry & Hilsa",
                odiaName = "ବଳରାମଗଡ଼ି ସତେଜ ଇଲିଶି ଓ ଫ୍ରାଏ",
                category = "Seafood",
                description = "Direct ocean catch from Bay of Bengal trawlers. Crisp golden pomfret, jumbo tiger prawns, and rich Hilsa (Ilish) cooked with authentic Odia mustard paste (Besara).",
                landmarkShop = "Maa Tarini Sea Shack, Balaramgadi Fish Harbor Road",
                priceEstimate = "₹180 - ₹350 per plate",
                latitude = 21.4810,
                longitude = 87.0520,
                tagColor = 0xFF0284C7
            ),
            FoodTrailItem(
                id = "food_3",
                name = "Chandipur Bay Mud Crab Curry",
                odiaName = "ଚାନ୍ଦିପୁର କଙ୍କଡ଼ା ତରକାରୀ",
                category = "Seafood",
                description = "Sweet, succulent local mangrove mud crabs simmered in rich tomato ginger garlic gravy with whole garam masala and Odia pancha phutana.",
                landmarkShop = "Sagar Kinare Coastal Kitchen, OTDC Panthanivas Road",
                priceEstimate = "₹220 - ₹320 per plate",
                latitude = 21.4682,
                longitude = 87.0163,
                tagColor = 0xFF0284C7
            ),
            FoodTrailItem(
                id = "food_4",
                name = "Motiganj Dahi Bara Aloo Dum & Ghuguni",
                odiaName = "ମୋତିଗଞ୍ଜ ଦହି ବରା ଆଳୁ ଦମ୍",
                category = "Chaat",
                description = "Balasore's quintessential breakfast and evening chaat: fluffy urad dal fritters soaked in spiced curd water, topped with slow-cooked potato gravy and fresh coriander.",
                landmarkShop = "Babu Dahi Bara Corner, Motiganj Market",
                priceEstimate = "₹40 - ₹60 per plate",
                latitude = 21.4934,
                longitude = 86.9325,
                tagColor = 0xFFEF4444
            ),
            FoodTrailItem(
                id = "food_5",
                name = "Nalamganj Wood-Fired Chenapoda",
                odiaName = "ନଳମଗଞ୍ଜ ପୋଡ଼ ପିଠା ଓ ଛେନାପୋଡ଼",
                category = "Sweets",
                description = "Fresh cottage cheese kneaded with sugar and cardamom, wrapped in Sal leaves and baked slowly over burning coal for that iconic smoky caramelized brown crust.",
                landmarkShop = "Gopal Sweets & Dairy, Station Road, Balasore",
                priceEstimate = "₹280 - ₹340 per kg",
                latitude = 21.4980,
                longitude = 86.9290,
                tagColor = 0xFFD97706
            ),
            FoodTrailItem(
                id = "food_6",
                name = "Jaleswar Spongy Chena Gaja",
                odiaName = "ଜଳେଶ୍ୱର ଛେନା ଗଜା",
                category = "Sweets",
                description = "Rectangular deep-fried cheese bites bathed in aromatic light syrup with a slightly crisp exterior and porous, syrupy melt-in-mouth center.",
                landmarkShop = "Mahavir Sweets, Jaleswar Highway Crossing",
                priceEstimate = "₹220 - ₹260 per kg",
                latitude = 21.8010,
                longitude = 87.2140,
                tagColor = 0xFFD97706
            )
        )
    }

    val categories = listOf("All", "Prasad", "Seafood", "Sweets", "Chaat")
    val filteredItems = if (selectedCategory == "All") {
        foodItems
    } else {
        foodItems.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("balasore_food_trail_sheet")
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
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFFD97706), Color(0xFFB45309))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Restaurant,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Balasore Food Trail",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "ଅମୃତ କେଳି ଓ ସମୁଦ୍ରର ସୁଆଦିଆ ଖାଦ୍ୟ ଯାତ୍ରା",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFD97706),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFFEF3C7)
            ) {
                Text(
                    text = "Culinary Guide",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF92400E)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Categories Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { cat ->
                FilterChip(
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = cat },
                    label = { Text(cat) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFD97706),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Food Items List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f, fill = false)
        ) {
            items(filteredItems, key = { it.id }) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = item.odiaName,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(item.tagColor),
                                        fontSize = 11.sp
                                    )
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(item.tagColor).copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = item.category,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(item.tagColor),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = item.description,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 16.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "📍 ${item.landmarkShop}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFF64748B),
                                        fontSize = 10.sp
                                    )
                                )
                                Text(
                                    text = "💰 ${item.priceEstimate}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFF0F172A),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                            }

                            OutlinedButton(
                                onClick = {
                                    openInGoogleMaps(
                                        context,
                                        item.latitude,
                                        item.longitude,
                                        item.name
                                    )
                                },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Icon(Icons.Default.Navigation, null, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Map", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Done Exploring Food Trail", fontWeight = FontWeight.Bold)
        }
    }
}
