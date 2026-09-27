package com.example.ui.features.agro

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
fun SubarnarekhaCropAdvisorySheet(
    language: AppLanguage,
    dailyPulse: DailyBalasorePulse,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedCropFilter by remember { mutableStateOf("All") }

    val cropAdvisories = listOf(
        CropGuidance(
            cropNameEn = "Betel Vine (Baleswari Paan Baraja)",
            cropNameOd = "ବାଲେଶ୍ୱରୀ ପାନ ବରଜ",
            sector = "Bhograi & Baliapal",
            currentRisk = dailyPulse.betelBlightRisk,
            recommendedAction = "Apply Bordeaux Mixture (1%) spray at vine collar; ensure aerated thatched bamboo roofing to drain humid vapor.",
            fertilizerDosage = "Mustard oil-cake slurry fermented for 4 days + Neem de-oiled cake.",
            category = "Paan"
        ),
        CropGuidance(
            cropNameEn = "Swarna / Pooja Lowland Paddy",
            cropNameOd = "ସ୍ୱର୍ଣ୍ଣ ଓ ପୂଜା ଧାନ",
            sector = "Jaleswar & Basta Alluvial Plain",
            currentRisk = "Brown Plant Hopper (BPH) Watch in waterlogged tracts",
            recommendedAction = "Maintain 'alleyways' (1 ft gap every 10 rows) for light penetration; apply Neem oil spray (1500 ppm).",
            fertilizerDosage = "Balanced NPK (80:40:40 kg/ha) + Zinc Sulphate 25 kg/ha basal.",
            category = "Paddy"
        ),
        CropGuidance(
            cropNameEn = "Remuna Pointed Gourd (Potala)",
            cropNameOd = "ରେମୁଣା ପୋଟଳ ଚାଷ",
            sector = "Budhabalanga Riverbed Belt",
            currentRisk = "Fruit fly infestation in flowering phase",
            recommendedAction = "Install pheromone traps (cue-lure) 10 traps/acre; avoid stagnant irrigation after sunset.",
            fertilizerDosage = "Well-decomposed cow dung manure + Trichoderma viride enriched compost.",
            category = "Vegetables"
        ),
        CropGuidance(
            cropNameEn = "Bhograi Coastal Raw Cashew (Kaju)",
            cropNameOd = "ଭୋଗରାଇ ଉପକୂଳ କାଜୁ ବଗିଚା",
            sector = "Digha-Bhograi Sandy Ridges",
            currentRisk = "Tea mosquito bug shoot blight",
            recommendedAction = "Prune diseased dead twigs post-harvest; spray Lambda cyhalothrin during fresh flushing stage.",
            fertilizerDosage = "Rock phosphate 500g + MOP 750g per mature tree in annular trenches.",
            category = "Plantation"
        )
    )

    val filteredAdvisories = remember(cropAdvisories, selectedCropFilter) {
        if (selectedCropFilter == "All") cropAdvisories
        else cropAdvisories.filter { it.category == selectedCropFilter }
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
                            text = if (language == AppLanguage.ODIA) "ସୁବର୍ଣ୍ଣରେଖା କୃଷି ଓ ପାନ ବରଜ ପରାମର୍ଶ 🌾" else "Crop Advisory & Soil Health Radar 🌾",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ମାଟି ଲବଣାକ୍ତତା, ଫସଲ ରୋଗ ନିୟନ୍ତ୍ରଣ ଓ କୃଷି ବିଜ୍ଞାନ କେନ୍ଦ୍ର ପରାମର୍ଶ" else "Subarnarekha Basin Soil Salinity & Betel Vine Guidance",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            }

            // Real-time Soil & Basin Telemetry
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF14532D)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("SOIL & RIVER BASIN TELEMETRY", color = Color(0xFF86EFAC), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Badge(containerColor = Color(0xFF15803D)) {
                                Text("KVK BALASORE SYNC", color = Color.White, fontSize = 10.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = dailyPulse.soilSalinityStatus,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Betel Vine Health: ${dailyPulse.betelBlightRisk}",
                            color = Color(0xFFBBF7D0),
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1551"))
                                    context.startActivity(dialIntent)
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A))
                            ) {
                                Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Kisan Call 1551")
                            }

                            Button(
                                onClick = {
                                    val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+916782260233"))
                                    context.startActivity(dialIntent)
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF166534))
                            ) {
                                Icon(Icons.Default.SupportAgent, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("KVK Balasore")
                            }
                        }
                    }
                }
            }

            // Category Filter
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("All", "Paan", "Paddy", "Vegetables", "Plantation").forEach { filter ->
                        FilterChip(
                            selected = selectedCropFilter == filter,
                            onClick = { selectedCropFilter = filter },
                            label = { Text(filter, fontSize = 11.sp) }
                        )
                    }
                }
            }

            // Crop Guidance Cards
            items(filteredAdvisories) { guidance ->
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
                                text = if (language == AppLanguage.ODIA) guidance.cropNameOd else guidance.cropNameEn,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Badge(containerColor = MaterialTheme.colorScheme.secondaryContainer) {
                                Text(guidance.sector, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Risk: ${guidance.currentRisk}", fontSize = 12.sp, color = Color(0xFFDC2626), fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Treatment: ${guidance.recommendedAction}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, lineHeight = 16.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Organic Nutrition: ${guidance.fertilizerDosage}", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

private data class CropGuidance(
    val cropNameEn: String,
    val cropNameOd: String,
    val sector: String,
    val currentRisk: String,
    val recommendedAction: String,
    val fertilizerDosage: String,
    val category: String
)
