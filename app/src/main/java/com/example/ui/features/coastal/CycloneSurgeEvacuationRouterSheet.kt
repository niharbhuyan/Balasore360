package com.example.ui.features.coastal

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.daily.DailyBalasorePulse
import com.example.data.model.AppLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CycloneSurgeEvacuationRouterSheet(
    language: AppLanguage,
    dailyPulse: DailyBalasorePulse,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedStartLocation by remember { mutableStateOf("Chandipur Beach Sector") }
    var selectedShelterIndex by remember { mutableIntStateOf(0) }
    var showRouteDirections by remember { mutableStateOf(false) }

    val shelters = remember {
        listOf(
            CycloneShelterItem(
                nameEn = "Chandipur Coastal Multipurpose Shelter",
                nameOd = "ଚାନ୍ଦିପୁର ବହୁମୁଖୀ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳ",
                block = "Balasore Sadar",
                capacity = 1200,
                currentOccupants = 190,
                generatorOperational = true,
                solarWaterPump = true,
                foodPacketsStock = 1800,
                wardenName = "Niranjan Das (Revenue Inspector)",
                wardenPhone = "+919437155201",
                distanceKm = 1.2f,
                elevationMeters = 7.8f
            ),
            CycloneShelterItem(
                nameEn = "Balaramgadi Marine High School MCS",
                nameOd = "ବଳରାମଗଡ଼ି ହାଇସ୍କୁଲ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳ",
                block = "Remuna Block / Port Sector",
                capacity = 850,
                currentOccupants = 240,
                generatorOperational = true,
                solarWaterPump = true,
                foodPacketsStock = 1200,
                wardenName = "Bikash Mohanty (Nodal Teacher)",
                wardenPhone = "+919861244302",
                distanceKm = 3.4f,
                elevationMeters = 8.5f
            ),
            CycloneShelterItem(
                nameEn = "Talasari Pantha Nivas MCS",
                nameOd = "ତାଳସାରୀ ପାନ୍ଥନିବାସ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳ",
                block = "Bhograi Block",
                capacity = 1000,
                currentOccupants = 120,
                generatorOperational = true,
                solarWaterPump = true,
                foodPacketsStock = 1500,
                wardenName = "Sunil Behera (BDO Staff)",
                wardenPhone = "+919438012345",
                distanceKm = 8.6f,
                elevationMeters = 9.2f
            ),
            CycloneShelterItem(
                nameEn = "Chaumukha Sea Dike MCS",
                nameOd = "ଚୌମୁଖା ସମୁଦ୍ର ବନ୍ଧ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳ",
                block = "Baliapal Block",
                capacity = 600,
                currentOccupants = 85,
                generatorOperational = true,
                solarWaterPump = false,
                foodPacketsStock = 800,
                wardenName = "Kailash Giri (OSDMA Volunteer)",
                wardenPhone = "+919777123987",
                distanceKm = 12.0f,
                elevationMeters = 6.4f
            ),
            CycloneShelterItem(
                nameEn = "Soro Coastal High School MCS",
                nameOd = "ସୋରୋ ଉପକୂଳ ହାଇସ୍କୁଲ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳ",
                block = "Soro Sub-division",
                capacity = 950,
                currentOccupants = 140,
                generatorOperational = true,
                solarWaterPump = true,
                foodPacketsStock = 1400,
                wardenName = "Ramesh Chandra Nayak",
                wardenPhone = "+919437299100",
                distanceKm = 18.5f,
                elevationMeters = 11.2f
            )
        )
    }

    val inundationZones = listOf(
        SurgeZone("Talasari Estuary & Subarnarekha Mouth", "+2.2m surge", "HIGH INUNDATION RISK 🔴", Color(0xFFFEF2F2)),
        SurgeZone("Balaramgadi Fish Harbor & Mudflats", "+1.8m surge", "MODERATE SURGE OVERFLOW 🟠", Color(0xFFFFFBEB)),
        SurgeZone("Chandipur Intertidal Beach Flats", "+0.9m surge", "GUARDED ABSORPTION 🟢", Color(0xFFF0FDF4)),
        SurgeZone("Bhograi Sluice Gate Creeks", "+1.6m surge", "BACKWATER SWELL WATCH 🟡", Color(0xFFFFF7ED))
    )

    val evacuationSteps = listOf(
        "1. Leave low-lying mudflats immediately; follow high embankment ridges towards PWD concrete roads.",
        "2. ⚠️ HAZARD AVOIDANCE: DO NOT use the Remuna & Bahanaga railway underpasses (waterlogging > 4 ft).",
        "3. ⚠️ HAZARD AVOIDANCE: Avoid earthen river dike along Budhabalanga mouth due to soft silt breaches.",
        "4. Follow the green fluorescent reflective arrows installed on State Highway 19 directly to the Multipurpose Shelter.",
        "5. Register your family at the Warden desk for assigned dry berths, hot food ration, and clean potable water."
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
            // Title Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବାତ୍ୟା ଜୁଆର ଓ ଆଶ୍ରୟସ୍ଥଳ ରାଉଟର୍ 🗺️" else "Cyclone Surge & MCS Evacuation Router 🗺️",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଉପକୂଳ ଜୁଆର ପ୍ଲାବନ ମ୍ୟାପ୍ ଓ ୬୪ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳ ମାର୍ଗଦର୍ଶନ" else "Projected Inundation Zones & 64 MCS Turn-by-Turn Safe Router",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            }

            // Live Surge Risk Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "COASTAL SURGE STATUS",
                                color = Color(0xFF38BDF8),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Badge(containerColor = Color(0xFF0284C7)) {
                                Text("64 SHELTERS OPERATIONAL", color = Color.White, fontSize = 10.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = dailyPulse.coastalSurgeRiskLevel,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Projected Sea Surge: ${dailyPulse.projectedSurgeHeightMeters}m | Avg Shelter Occupancy: ${dailyPulse.shelterOccupancyPercent}%",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { showRouteDirections = !showRouteDirections },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7))
                            ) {
                                Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (showRouteDirections) "Hide Route" else "Show Safe Route")
                            }

                            Button(
                                onClick = {
                                    val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1077"))
                                    context.startActivity(dialIntent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                            ) {
                                Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("OSDMA 1077")
                            }
                        }
                    }
                }
            }

            // Evacuation Turn-by-Turn Directions (Hazard Avoidance)
            if (showRouteDirections) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                        border = BorderStroke(1.dp, Color(0xFFF59E0B)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFB45309), modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Safe Evacuation Path & Hazard Warnings",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF92400E)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            evacuationSteps.forEach { step ->
                                Text(
                                    text = step,
                                    fontSize = 12.sp,
                                    color = Color(0xFF78350F),
                                    lineHeight = 17.sp,
                                    modifier = Modifier.padding(vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Projected Inundation Zones
            item {
                Text(
                    text = if (language == AppLanguage.ODIA) "ଉପକୂଳ ଜୁଆର ପ୍ଲାବନ ଆକଳନ (INCOIS)" else "Projected Coastal Inundation Zones",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            items(inundationZones) { zone ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = zone.bgColor),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(zone.name, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text(zone.projectedSurge, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(zone.riskLevel, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }

            // Multipurpose Cyclone Shelters Directory
            item {
                Text(
                    text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ଜିଲ୍ଲା ବହୁମୁଖୀ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳ (MCS)" else "Vetted Multipurpose Cyclone Shelters",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            items(shelters) { shelter ->
                val isSelected = shelters.indexOf(shelter) == selectedShelterIndex
                val occupancyPercent = (shelter.currentOccupants * 100) / shelter.capacity

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedShelterIndex = shelters.indexOf(shelter) },
                    border = BorderStroke(
                        if (isSelected) 2.dp else 1.dp,
                        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (language == AppLanguage.ODIA) shelter.nameOd else shelter.nameEn,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Badge(containerColor = if (occupancyPercent > 70) Color(0xFFEF4444) else Color(0xFF22C55E)) {
                                Text("$occupancyPercent% Full", color = Color.White, fontSize = 10.sp)
                            }
                        }

                        Text("${shelter.block} • ${shelter.distanceKm} km away • Elev: ${shelter.elevationMeters}m", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { occupancyPercent / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (occupancyPercent > 70) Color(0xFFEF4444) else MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Capacity: ${shelter.capacity} berths", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                            Text("Ration: ${shelter.foodPacketsStock} pkts", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                            Text(if (shelter.generatorOperational) "Gen: 🟢 Active" else "Gen: 🔴 Offline", fontSize = 11.sp)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Warden: ${shelter.wardenName}", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                            }
                            Button(
                                onClick = {
                                    val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${shelter.wardenPhone}"))
                                    context.startActivity(dialIntent)
                                },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Call Warden", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

private data class CycloneShelterItem(
    val nameEn: String,
    val nameOd: String,
    val block: String,
    val capacity: Int,
    val currentOccupants: Int,
    val generatorOperational: Boolean,
    val solarWaterPump: Boolean,
    val foodPacketsStock: Int,
    val wardenName: String,
    val wardenPhone: String,
    val distanceKm: Float,
    val elevationMeters: Float
)

private data class SurgeZone(
    val name: String,
    val projectedSurge: String,
    val riskLevel: String,
    val bgColor: Color
)
