package com.example.ui.features.coastal

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.WaterDrop
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
import androidx.compose.runtime.LaunchedEffect
import android.media.AudioManager
import android.media.ToneGenerator
import kotlinx.coroutines.delay
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

data class CycloneShelter(
    val id: String,
    val name: String,
    val odiaName: String,
    val block: String,
    val location: String,
    val capacity: Int,
    val contactPerson: String,
    val phoneNumber: String,
    val latitude: Double,
    val longitude: Double,
    val distanceKm: Double,
    val hasGenerator: Boolean = true,
    val hasWaterFilter: Boolean = true,
    val hasMedicalRoom: Boolean = true
)

/**
 * Offline Cyclone Shelter GPS Finder component for disaster preparedness in Balasore district.
 */
@Composable
fun CycloneShelterFinderSheet(
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedBlock by remember { mutableStateOf("All") }
    var isSosStrobeActive by remember { mutableStateOf(false) }
    var isWhistleSounding by remember { mutableStateOf(false) }
    var strobeColorToggle by remember { mutableStateOf(false) }

    val toneGen = remember {
        try {
            ToneGenerator(AudioManager.STREAM_ALARM, 100)
        } catch (_: Exception) {
            null
        }
    }

    LaunchedEffect(isSosStrobeActive) {
        if (isSosStrobeActive) {
            while (isSosStrobeActive) {
                delay(280L)
                strobeColorToggle = !strobeColorToggle
            }
        } else {
            strobeColorToggle = false
        }
    }

    LaunchedEffect(isWhistleSounding) {
        if (isWhistleSounding) {
            while (isWhistleSounding) {
                try {
                    toneGen?.startTone(ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK, 450)
                } catch (_: Exception) {}
                delay(650L)
            }
        } else {
            try {
                toneGen?.stopTone()
            } catch (_: Exception) {}
        }
    }

    val shelters = remember {
        listOf(
            CycloneShelter(
                id = "cs_1",
                name = "Chandipur Coastal Multipurpose Shelter",
                odiaName = "ଚାନ୍ଦିପୁର ବହୁମୁଖୀ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳ",
                block = "Chandipur",
                location = "Near Marine Police Station, Chandipur",
                capacity = 1800,
                contactPerson = "BDO Sadar Control Room",
                phoneNumber = "06782262234",
                latitude = 21.4682,
                longitude = 87.0163,
                distanceKm = 1.2
            ),
            CycloneShelter(
                id = "cs_2",
                name = "Bhograi Coastal Disaster Shelter",
                odiaName = "ଭୋଗରାଇ ଉପକୂଳ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳ",
                block = "Bhograi",
                location = "Talasari Beach Embankment, Bhograi",
                capacity = 2400,
                contactPerson = "Bhograi Tahsildar / Shelter Officer",
                phoneNumber = "06781238210",
                latitude = 21.5892,
                longitude = 87.4582,
                distanceKm = 48.0
            ),
            CycloneShelter(
                id = "cs_3",
                name = "Remuna High School Shelter",
                odiaName = "ରେମୁଣା ଉଚ୍ଚ ବିଦ୍ୟାଳୟ ଆଶ୍ରୟ କେନ୍ଦ୍ର",
                block = "Remuna",
                location = "Near Khirachora Gopinath Temple, Remuna",
                capacity = 1400,
                contactPerson = "Remuna Block Emergency Cell",
                phoneNumber = "06782252115",
                latitude = 21.5284,
                longitude = 86.8647,
                distanceKm = 9.5
            ),
            CycloneShelter(
                id = "cs_4",
                name = "Kasafal Fishermen Disaster Centre",
                odiaName = "କସାଫଳ ମତ୍ସ୍ୟଜୀବୀ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳ",
                block = "Chandipur",
                location = "Kasafal Sea Mouth, Balasore",
                capacity = 1600,
                contactPerson = "Kasafal Marine Cell",
                phoneNumber = "06782260100",
                latitude = 21.5200,
                longitude = 87.1100,
                distanceKm = 14.2
            ),
            CycloneShelter(
                id = "cs_5",
                name = "Bahanaga Relief Multipurpose Center",
                odiaName = "ବାହାନଗା ବିପର୍ଯ୍ୟୟ ରିଲିଫ କେନ୍ଦ୍ର",
                block = "Bahanaga",
                location = "Bahanaga Bazar, NH-16",
                capacity = 1500,
                contactPerson = "Bahanaga Emergency Officer",
                phoneNumber = "06782275420",
                latitude = 21.3150,
                longitude = 86.8050,
                distanceKm = 24.5
            ),
            CycloneShelter(
                id = "cs_6",
                name = "Baliapal Subarnarekha Basin Shelter",
                odiaName = "ବାଲିଆପାଳ ସୁବର୍ଣ୍ଣରେଖା ଅବବାହିକା ଆଶ୍ରୟସ୍ଥଳ",
                block = "Baliapal",
                location = "Chaumukh River Embankment, Baliapal",
                capacity = 2100,
                contactPerson = "Baliapal Block Control Desk",
                phoneNumber = "06781254300",
                latitude = 21.6420,
                longitude = 87.2890,
                distanceKm = 36.8
            ),
            CycloneShelter(
                id = "cs_7",
                name = "Balasore Town DHH Emergency Hall",
                odiaName = "ବାଲେଶ୍ୱର ସହର ଜରୁରୀକାଳୀନ ଭବନ",
                block = "Sadar",
                location = "Near DHH Hospital, Balasore Town",
                capacity = 2500,
                contactPerson = "District Collectorate Helpline (Toll-Free 1077)",
                phoneNumber = "1077",
                latitude = 21.4934,
                longitude = 86.9325,
                distanceKm = 0.8
            )
        )
    }

    val blocks = listOf("All", "Chandipur", "Bhograi", "Remuna", "Bahanaga", "Baliapal", "Sadar")
    val filteredShelters = if (selectedBlock == "All") {
        shelters
    } else {
        shelters.filter { it.block.equals(selectedBlock, ignoreCase = true) }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("cyclone_shelter_finder_sheet")
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
                                listOf(Color(0xFF059669), Color(0xFF047857))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Cyclone Shelters GPS Finder",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "ଜିଲ୍ଲା ବହୁମୁଖୀ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳ ତାଲିକା",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF059669),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFD1FAE5)
            ) {
                Text(
                    text = "${filteredShelters.size} Shelters",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF065F46)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Block Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(blocks) { block ->
                FilterChip(
                    selected = selectedBlock == block,
                    onClick = { selectedBlock = block },
                    label = { Text(block) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF059669),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Shelters List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f, fill = false)
        ) {
            items(filteredShelters, key = { it.id }) { shelter ->
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
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = shelter.name,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = shelter.odiaName,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF059669),
                                        fontSize = 11.sp
                                    )
                                )
                                Text(
                                    text = shelter.location,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.sp
                                    )
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFF1F5F9)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = Color(0xFF0284C7),
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = "${shelter.distanceKm} km",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Amenities & Capacity Badges
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFE0F2FE)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.People,
                                        contentDescription = null,
                                        tint = Color(0xFF0369A1),
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Capacity: ${shelter.capacity}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF0369A1)
                                    )
                                }
                            }

                            if (shelter.hasGenerator) {
                                Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFFEF3C7)) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.ElectricBolt, null, tint = Color(0xFFD97706), modifier = Modifier.size(11.dp))
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text("Power", fontSize = 10.sp, color = Color(0xFF92400E))
                                    }
                                }
                            }

                            if (shelter.hasWaterFilter) {
                                Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFCFFAFE)) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.WaterDrop, null, tint = Color(0xFF0891B2), modifier = Modifier.size(11.dp))
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text("Water", fontSize = 10.sp, color = Color(0xFF155E75))
                                    }
                                }
                            }

                            if (shelter.hasMedicalRoom) {
                                Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFFEE2E2)) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.LocalHospital, null, tint = Color(0xFFDC2626), modifier = Modifier.size(11.dp))
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text("Medical", fontSize = 10.sp, color = Color(0xFF991B1B))
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Action Buttons: Call Officer & Navigate
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                                        data = Uri.parse("tel:${shelter.phoneNumber}")
                                    }
                                    context.startActivity(dialIntent)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(36.dp),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp)
                            ) {
                                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Call Shelter", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }

                            Button(
                                onClick = {
                                    openInGoogleMaps(
                                        context,
                                        shelter.latitude,
                                        shelter.longitude,
                                        shelter.name
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(36.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp)
                            ) {
                                Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.White)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Directions", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
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
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Close Shelter Finder", fontWeight = FontWeight.Bold)
        }
    }
}
