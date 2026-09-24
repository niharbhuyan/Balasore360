package com.example.ui.features.health

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

data class NightChemist(
    val name: String,
    val location: String,
    val locationOd: String,
    val phone: String,
    val timing: String, // "Open 24x7", "Open till 2:00 AM"
    val hasOxygenRefill: Boolean = false,
    val isJanAushadhi: Boolean = false
)

val BALASORE_NIGHT_CHEMISTS = listOf(
    NightChemist(
        name = "DHH In-House 24x7 Niramaya Store",
        location = "District Headquarters Hospital Campus, Balasore",
        locationOd = "ଜିଲ୍ଲା ମୁଖ୍ୟ ଚିକିତ୍ସାଳୟ କ୍ୟାମ୍ପସ୍",
        phone = "06782262024",
        timing = "24 Hours (Free Govt Medicines)",
        hasOxygenRefill = true,
        isJanAushadhi = false
    ),
    NightChemist(
        name = "Sanjeevani Medicos 24x7",
        location = "Opposite DHH Main Casualty Gate, FM Circle",
        locationOd = "ଡିଏଚ୍ଏଚ୍ କାଜୁଆଲିଟି ଗେଟ୍ ସମ୍ମୁଖ, ଏଫଏମ ଛକ",
        phone = "9437152010",
        timing = "Open 24x7 All Night",
        hasOxygenRefill = true,
        isJanAushadhi = false
    ),
    NightChemist(
        name = "PM Jan Aushadhi Kendra (Cinema Chhak)",
        location = "Near Tarini Temple, Cinema Chhak, Balasore",
        locationOd = "ସିନେମା ଛକ, ତାରିଣୀ ମନ୍ଦିର ନିକଟ",
        phone = "9861245870",
        timing = "08:00 AM - 11:30 PM",
        hasOxygenRefill = false,
        isJanAushadhi = true
    ),
    NightChemist(
        name = "Station Road Lifeline Medical Store",
        location = "Station Square, Balasore",
        locationOd = "ରେଳ ଷ୍ଟେସନ ଛକ, ବାଲେଶ୍ୱର",
        phone = "9438012398",
        timing = "Open 24 Hours",
        hasOxygenRefill = false,
        isJanAushadhi = false
    ),
    NightChemist(
        name = "Sahadevkhunta Emergency Medical Hall",
        location = "Bus Stand Road, Sahadevkhunta",
        locationOd = "ବସ୍ ଷ୍ଟାଣ୍ଡ ରୋଡ୍, ସହଦେବଖୁଣ୍ଟା",
        phone = "9853214569",
        timing = "06:00 AM - 01:00 AM",
        hasOxygenRefill = true,
        isJanAushadhi = false
    )
)

/**
 * 24x7 Night Chemist & Sanjeevani Emergency Desk.
 * Fast locator for midnight pharmacies, emergency oxygen cylinder suppliers,
 * private cardiac ambulances, and government Jan Aushadhi outlets.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NightChemistSanjeevaniSheet(
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var selectedFilter by remember { mutableStateOf("All Pharmacies") }
    val filters = listOf("All Pharmacies", "Open 24x7", "Oxygen Suppliers", "Jan Aushadhi")

    val displayedChemists = remember(selectedFilter) {
        when (selectedFilter) {
            "Open 24x7" -> BALASORE_NIGHT_CHEMISTS.filter { it.timing.contains("24", ignoreCase = true) }
            "Oxygen Suppliers" -> BALASORE_NIGHT_CHEMISTS.filter { it.hasOxygenRefill }
            "Jan Aushadhi" -> BALASORE_NIGHT_CHEMISTS.filter { it.isJanAushadhi }
            else -> BALASORE_NIGHT_CHEMISTS
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
                .testTag("night_chemist_sanjeevani_sheet")
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
                        Text(text = "💊", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "୨୪x୭ ରାତ୍ରିକାଳୀନ ଔଷଧାଳୟ ଓ ଅକ୍ସିଜେନ" else "24x7 Night Chemist & Oxygen",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଜରୁରୀକାଳୀନ ଔଷଧ ଦୋକାନ • ସଞ୍ଜୀବନୀ ଅମ୍ଳଜାନ ସେବା" else "Midnight Pharmacies • Sanjeevani Oxygen Desk",
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
                // Emergency SOS Buttons
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:108"))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.LocalHospital, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("108 Ambulance", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:06782262024"))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = OceanBlue),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("DHH Casualty", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Filter Chips
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(filters) { filter ->
                            FilterChip(
                                selected = selectedFilter == filter,
                                onClick = { selectedFilter = filter },
                                label = { Text(filter, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFFDC2626),
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // Chemist List
                items(displayedChemists) { chemist ->
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
                                        text = chemist.name,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = BentoSlate900
                                        )
                                    )
                                    Text(
                                        text = if (language == AppLanguage.ODIA) chemist.locationOd else chemist.location,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = BentoSlate600,
                                            fontSize = 12.sp
                                        )
                                    )
                                }

                                Surface(
                                    color = if (chemist.timing.contains("24")) Color(0xFFDCFCE7) else Color(0xFFFEF3C7),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = chemist.timing,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (chemist.timing.contains("24")) Color(0xFF15803D) else Color(0xFFB45309),
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (chemist.hasOxygenRefill) {
                                        Surface(
                                            color = Color(0xFFE0F2FE),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = "🫁 O2 Cylinders Available",
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0284C7)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(6.dp))
                                    }
                                    if (chemist.isJanAushadhi) {
                                        Surface(
                                            color = Color(0xFFF3E8FF),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = "🏷️ Generic Rates",
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF9333EA)
                                            )
                                        }
                                    }
                                }

                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${chemist.phone}"))
                                        context.startActivity(intent)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Call Chemist", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // Oxygen Cylinder Refill Agents
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                        border = BorderStroke(1.dp, Color(0xFFBBF7D0))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "🫁 Emergency Oxygen Refill Agencies",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF166534)
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "• Maa Tarini Medical Oxygen, Angargadia: 24h Cylinder Refill & Regulators (9437298711)\n• Balasore Gas Agency, ITI Square: B-Type & Jumbo O2 cylinders home delivery (9861055210)",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF14532D),
                                    lineHeight = 18.sp
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
