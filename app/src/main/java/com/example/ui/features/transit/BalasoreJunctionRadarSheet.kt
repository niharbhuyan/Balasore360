package com.example.ui.features.transit

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

data class TrainSchedule(
    val trainNumber: String,
    val trainName: String,
    val trainNameOd: String,
    val scheduledTime: String,
    val usualPlatform: String,
    val direction: String, // "Towards Howrah", "Towards Bhubaneswar/Chennai"
    val daysOfRun: String,
    val trainType: String // Vande Bharat, Superfast, Express, Mail
)

val BALASORE_KEY_TRAINS = listOf(
    TrainSchedule(
        trainNumber = "20836",
        trainName = "Puri - Howrah Vande Bharat Express",
        trainNameOd = "ପୁରୀ - ହାଓଡ଼ା ବନ୍ଦେ ଭାରତ ଏକ୍ସପ୍ରେସ୍",
        scheduledTime = "09:51 AM",
        usualPlatform = "PF 1",
        direction = "Towards Howrah (Kolkata)",
        daysOfRun = "Daily except Thu",
        trainType = "Vande Bharat"
    ),
    TrainSchedule(
        trainNumber = "12822",
        trainName = "Dhauli Superfast Express",
        trainNameOd = "ଧଉଳି ସୁପରଫାଷ୍ଟ ଏକ୍ସପ୍ରେସ୍",
        scheduledTime = "03:42 PM",
        usualPlatform = "PF 1",
        direction = "Towards Howrah (HWH)",
        daysOfRun = "Daily",
        trainType = "Superfast"
    ),
    TrainSchedule(
        trainNumber = "12821",
        trainName = "Dhauli Superfast Express",
        trainNameOd = "ଧଉଳି ସୁପରଫାଷ୍ଟ ଏକ୍ସପ୍ରେସ୍",
        scheduledTime = "12:10 PM",
        usualPlatform = "PF 2",
        direction = "Towards Bhubaneswar / Puri",
        daysOfRun = "Daily",
        trainType = "Superfast"
    ),
    TrainSchedule(
        trainNumber = "12841",
        trainName = "Coromandel Express",
        trainNameOd = "କରମଣ୍ଡଳ ଏକ୍ସପ୍ରେସ୍",
        scheduledTime = "06:50 PM",
        usualPlatform = "PF 2",
        direction = "Towards Chennai Central (MAS)",
        daysOfRun = "Daily",
        trainType = "Superfast"
    ),
    TrainSchedule(
        trainNumber = "18045",
        trainName = "East Coast Express",
        trainNameOd = "ଇଷ୍ଟ କୋଷ୍ଟ ଏକ୍ସପ୍ରେସ୍",
        scheduledTime = "03:15 PM",
        usualPlatform = "PF 3",
        direction = "Towards Hyderabad (HYB)",
        daysOfRun = "Daily",
        trainType = "Mail/Express"
    ),
    TrainSchedule(
        trainNumber = "12801",
        trainName = "Puri - New Delhi Purushottam Express",
        trainNameOd = "ପୁରୁଷୋତ୍ତମ ଏକ୍ସପ୍ରେସ୍ (ନୂଆଦିଲ୍ଲୀ)",
        scheduledTime = "01:58 AM",
        usualPlatform = "PF 1",
        direction = "Towards New Delhi (NDLS)",
        daysOfRun = "Daily",
        trainType = "Superfast"
    ),
    TrainSchedule(
        trainNumber = "20835",
        trainName = "Howrah - Puri Vande Bharat Express",
        trainNameOd = "ହାଓଡ଼ା - ପୁରୀ ବନ୍ଦେ ଭାରତ ଏକ୍ସପ୍ରେସ୍",
        scheduledTime = "08:52 AM",
        usualPlatform = "PF 2",
        direction = "Towards Puri / Bhubaneswar",
        daysOfRun = "Daily except Thu",
        trainType = "Vande Bharat"
    )
)

/**
 * Balasore Junction (BLS) Train & Platform Radar.
 * Live platform indicators, key express timetable, station amenities, cloakroom tariffs,
 * and direct GRP/RPF emergency contacts.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalasoreJunctionRadarSheet(
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var selectedFilter by remember { mutableStateOf("All Trains") }
    val filterOptions = listOf("All Trains", "Vande Bharat", "Towards Howrah", "Towards Puri/South")

    val filteredTrains = remember(selectedFilter) {
        when (selectedFilter) {
            "Vande Bharat" -> BALASORE_KEY_TRAINS.filter { it.trainType == "Vande Bharat" }
            "Towards Howrah" -> BALASORE_KEY_TRAINS.filter { it.direction.contains("Howrah", ignoreCase = true) }
            "Towards Puri/South" -> BALASORE_KEY_TRAINS.filter { !it.direction.contains("Howrah", ignoreCase = true) }
            else -> BALASORE_KEY_TRAINS
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
                .testTag("balasore_junction_radar_sheet")
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
                            .background(Color(0xFFE0E7FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🚆", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ଜଙ୍କସନ ଟ୍ରେନ ଓ ପ୍ଲାଟଫର୍ମ ରାଡାର" else "Balasore Junction (BLS) Radar",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "କୋଡ୍: BLS • ଦକ୍ଷିଣ ପୂର୍ବ ରେଳବାଇ (SER)" else "Station Code: BLS • South Eastern Railway",
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
                // Station Quick Status & Emergency
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF16A34A))
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "STATION SIGNALS NORMAL",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF16A34A)
                                        )
                                    )
                                }
                                Surface(
                                    color = Color(0xFFE2E8F0),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "4 PLATFORMS",
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Major stop on Howrah–Chennai mainline with Escalator at PF 1 & 2, Foot Overbridge, IRCTC Refreshment Room & 24h GRP Outpost.",
                                style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate600)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:139"))
                                        context.startActivity(intent)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = OceanBlue),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(imageVector = Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("RailMadad (139)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:06782262844"))
                                        context.startActivity(intent)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(imageVector = Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("RPF Balasore", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // Filter Tabs
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(filterOptions) { filter ->
                            FilterChip(
                                selected = selectedFilter == filter,
                                onClick = { selectedFilter = filter },
                                label = { Text(filter, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = OceanBlue,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // Train Timetable Cards
                items(filteredTrains) { train ->
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
                                        text = "#${train.trainNumber} • ${train.trainType}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(0xFF4338CA),
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        text = if (language == AppLanguage.ODIA) train.trainNameOd else train.trainName,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = BentoSlate900
                                        )
                                    )
                                    Text(
                                        text = "🧭 ${train.direction}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = BentoSlate500,
                                            fontSize = 11.sp
                                        )
                                    )
                                }

                                Surface(
                                    color = Color(0xFFEFF6FF),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, Color(0xFFBFDBFE))
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = train.usualPlatform,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFF1D4ED8),
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = train.scheduledTime,
                                            fontWeight = FontWeight.Bold,
                                            color = BentoSlate700,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "🗓️ Runs: ${train.daysOfRun}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = BentoSlate600,
                                        fontSize = 11.sp
                                    )
                                )

                                Button(
                                    onClick = {
                                        val intent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://enquiry.indianrail.gov.in/mntes/")
                                        )
                                        context.startActivity(intent)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F766E)),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text("Live NTES Track", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // Station Amenities Guide
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                        border = BorderStroke(1.dp, Color(0xFFFDE68A))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "🏢 Station Passenger Facilities",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF92400E)
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "• Cloakroom & Luggage: PF 1 near main exit (₹20 per 24 hours with locked bag).\n• IRCTC Retiring Rooms: AC & Non-AC Dormitories on 1st Floor PF 1.\n• Battery Buggy / Wheelchair: Contact Deputy Station Superintendent PF 1.\n• Prepaid Toto / Auto Stand: Right outside North Gate 1.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF78350F),
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
