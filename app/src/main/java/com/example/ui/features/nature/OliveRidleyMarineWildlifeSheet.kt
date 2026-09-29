package com.example.ui.features.nature

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.daily.DailyBalasorePulse
import com.example.data.model.AppLanguage
import com.example.ui.theme.*

data class MarineSightingRecord(
    val id: String,
    val species: String,
    val speciesOd: String,
    val location: String,
    val locationOd: String,
    val count: Int,
    val status: String,
    val reportedTime: String,
    val iconEmoji: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OliveRidleyMarineWildlifeSheet(
    language: AppLanguage,
    dailyPulse: DailyBalasorePulse,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val isOdia = language == AppLanguage.ODIA

    var sightingsList by remember {
        mutableStateOf(
            listOf(
                MarineSightingRecord(
                    id = "s_1",
                    species = "Olive Ridley Sea Turtle (Lepidochelys olivacea)",
                    speciesOd = "ଅଲିଭ୍ ରିଡଲେ ସମୁଦ୍ର କଇଁଛ",
                    location = "Kasafal Beach Estuary",
                    locationOd = "କାସାଫାଳ ବେଳାଭୂମି ମୁହାଣ",
                    count = 4,
                    status = "Active Nesting Observed • Forest Guard Tagged",
                    reportedTime = "2 hours ago",
                    iconEmoji = "🐢"
                ),
                MarineSightingRecord(
                    id = "s_2",
                    species = "Horseshoe Crab (Tachypleus gigas)",
                    speciesOd = "ଜୀବନ୍ତ ଜୀବାଶ୍ମ ରାଜକଙ୍କଡ଼ା",
                    location = "Chandipur Receded Seabed (3.2 km out)",
                    locationOd = "ଚାନ୍ଦିପୁର ସମୁଦ୍ର ଶଯ୍ୟା",
                    count = 8,
                    status = "Mating Pair in Intertidal Mudflats",
                    reportedTime = "Today morning",
                    iconEmoji = "🦀"
                ),
                MarineSightingRecord(
                    id = "s_3",
                    species = "Irrawaddy Dolphin (Orcaella brevirostris)",
                    speciesOd = "ଇରାୱାଡି ଡଲଫିନ୍",
                    location = "Subarnarekha Estuary (Talasari)",
                    locationOd = "ସୁବର୍ଣ୍ଣରେଖା ନଦୀ ମୁହାଣ",
                    count = 2,
                    status = "Breaching at High Tide Entrance",
                    reportedTime = "Yesterday evening",
                    iconEmoji = "🐬"
                )
            )
        )
    }

    var showReportDialog by remember { mutableStateOf(false) }
    var reporterName by remember { mutableStateOf("") }
    var selectedSpecies by remember { mutableStateOf("Olive Ridley Sea Turtle") }
    var selectedBeach by remember { mutableStateOf("Kasafal Beach") }
    var countInput by remember { mutableStateOf("1") }
    var notesInput by remember { mutableStateOf("") }

    val beaches = listOf("Kasafal Beach", "Talasari Estuary", "Chandipur Intertidal", "Dagara Beach", "Balaramgadi Mouth")
    val speciesOptions = listOf("Olive Ridley Sea Turtle", "Horseshoe Crab", "Coastal Dolphin", "Sea Snake / Marine Bird")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag("olive_ridley_marine_wildlife_sheet")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFE0F2FE),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🐢", fontSize = 24.sp)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (isOdia) "ଅଲିଭ୍ ରିଡଲେ ଓ ସାମୁଦ୍ରିକ ଜୀବ ସଂରକ୍ଷଣ" else "Olive Ridley & Marine Life Sentinel",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = BentoSlate900
                    )
                    Text(
                        text = if (isOdia) "ସ୍ୱୟଂକ୍ରିୟ ଲାଇଭ୍ ଟେଲିମେଟ୍ରି • ବାଲେଶ୍ୱର ବନ୍ୟପ୍ରାଣୀ ବିଭାଗ" else "Live Auto-Updated Telemetry • Balasore Wildlife Division",
                        style = MaterialTheme.typography.bodySmall,
                        color = BentoSlate500
                    )
                }
            }
            IconButton(
                onClick = onClose,
                modifier = Modifier.testTag("btn_close_olive_ridley_sheet")
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = BentoSlate600)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Auto-Updated Telemetry Banner
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
            border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF22C55E),
                            modifier = Modifier.size(10.dp)
                        ) {}
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isOdia) "ସ୍ୱୟଂକ୍ରିୟ ନବୀକୃତ ସାମୁଦ୍ରିକ ପଲ୍ସ" else "AUTO-UPDATED TELEMETRY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF15803D)
                        )
                    }
                    Text(
                        text = "${dailyPulse.oliveRidleyNestingCount} nests tracked",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF15803D)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = dailyPulse.oliveRidleyBeachSightingStatus,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BentoSlate800
                )
                Text(
                    text = if (isOdia) "ରାତ୍ରି କାଳୀନ ବନ ବିଭାଗ ପାଟ୍ରୋଲିଂ ଓ ହ୍ୟାଚେରୀ ସୁରକ୍ଷା ବାଲେଶ୍ୱର ଉପକୂଳରେ ସକ୍ରିୟ ରହିଛି।" else "Night nesting protection, artificial beach hatcheries & mechanized trawler exclusion zone active.",
                    fontSize = 12.sp,
                    color = BentoSlate600
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Action Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { showReportDialog = true },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OceanBlue),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("btn_log_wildlife_sighting")
            ) {
                Icon(Icons.Default.AddLocationAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isOdia) "ଜୀବ ଦେଖିଲି (ରିପୋର୍ଟ)" else "Log Sighting", fontSize = 13.sp)
            }

            OutlinedButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:06782262144")
                    }
                    context.startActivity(intent)
                },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFF0284C7)),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("btn_call_wildlife_helpline")
            ) {
                Icon(Icons.Default.Call, contentDescription = null, tint = Color(0xFF0284C7), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isOdia) "ବନ ବିଭାଗ କଲ୍" else "Forest Hotline", color = Color(0xFF0284C7), fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isOdia) "ସାମ୍ପ୍ରତିକ ସାମୁଦ୍ରିକ ଜୀବ ସୂଚୀ (ସଦ୍ୟତମ ଲଗ୍)" else "Recent Marine Sightings & Community Logs",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = BentoSlate800
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(sightingsList, key = { it.id }) { item ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                    border = BorderStroke(1.dp, BentoSlate200),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFE0F2FE),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(item.iconEmoji, fontSize = 20.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isOdia) item.speciesOd else item.species,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = BentoSlate900
                            )
                            Text(
                                text = "📍 ${if (isOdia) item.locationOd else item.location} • Count: ${item.count}",
                                fontSize = 12.sp,
                                color = BentoSlate600
                            )
                            Text(
                                text = "Status: ${item.status}",
                                fontSize = 11.sp,
                                color = Color(0xFF0284C7)
                            )
                        }
                        Text(
                            text = item.reportedTime,
                            fontSize = 11.sp,
                            color = BentoSlate400
                        )
                    }
                }
            }
        }
    }

    if (showReportDialog) {
        AlertDialog(
            onDismissRequest = { showReportDialog = false },
            title = {
                Text(
                    text = if (isOdia) "ସାମୁଦ୍ରିକ ଜୀବ ଦେଖିବା ରିପୋର୍ଟ କରନ୍ତୁ" else "Report Marine Life Sighting",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = if (isOdia) "ବନ୍ୟପ୍ରାଣୀ ସଂରକ୍ଷଣ ପାଇଁ ତଥ୍ୟ ପ୍ରଦାନ କରନ୍ତୁ:" else "Submit wildlife sighting for research and rescue:",
                        fontSize = 12.sp,
                        color = BentoSlate600
                    )
                    OutlinedTextField(
                        value = reporterName,
                        onValueChange = { reporterName = it },
                        label = { Text("Your Name / Contact") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = countInput,
                        onValueChange = { countInput = it },
                        label = { Text("Estimated Count") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = notesInput,
                        onValueChange = { notesInput = it },
                        label = { Text("Observations (Nesting, Injured, Swimming)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val newRecord = MarineSightingRecord(
                            id = "s_${System.currentTimeMillis()}",
                            species = selectedSpecies,
                            speciesOd = selectedSpecies,
                            location = selectedBeach,
                            locationOd = selectedBeach,
                            count = countInput.toIntOrNull() ?: 1,
                            status = if (notesInput.isNotBlank()) notesInput else "Reported by citizen sentinel",
                            reportedTime = "Just now",
                            iconEmoji = "🐢"
                        )
                        sightingsList = listOf(newRecord) + sightingsList
                        showReportDialog = false
                        Toast.makeText(context, "Sighting successfully logged! Forest team notified.", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = OceanBlue)
                ) {
                    Text("Submit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReportDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
