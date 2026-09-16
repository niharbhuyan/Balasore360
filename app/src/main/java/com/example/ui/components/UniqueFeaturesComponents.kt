package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.HelpOutline
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.UniqueFeatureSheetType

/**
 * Quick access dock for the 6 unique Balasore features.
 */
@Composable
fun UniqueFeaturesPillGrid(
    onFeatureClick: (UniqueFeatureSheetType) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("unique_features_pill_grid"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0F2FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFF0284C7),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Balasore Special Features",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate900
                        )
                    )
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF1F5F9)
                ) {
                    Text(
                        text = "6 LIVE HUBS",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            color = Color(0xFF0284C7)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 2 rows of 3 pills each
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                UniqueFeatureChip(
                    icon = "🦀",
                    label = "Horseshoe Crab",
                    sub = "Marine Radar",
                    color = Color(0xFFFFFBEB),
                    borderColor = Color(0xFFFDE68A),
                    modifier = Modifier.weight(1f),
                    onClick = { onFeatureClick(UniqueFeatureSheetType.HORSESHOE_CRAB) }
                )
                UniqueFeatureChip(
                    icon = "🚀",
                    label = "ITR Missile Trail",
                    sub = "Dr. Kalam & Radar",
                    color = Color(0xFFEFF6FF),
                    borderColor = Color(0xFFBFDBFE),
                    modifier = Modifier.weight(1f),
                    onClick = { onFeatureClick(UniqueFeatureSheetType.DEFENSE_TRAIL) }
                )
                UniqueFeatureChip(
                    icon = "🍲",
                    label = "Amrita Keli",
                    sub = "Bhog & Artisans",
                    color = Color(0xFFFEF2F2),
                    borderColor = Color(0xFFFECACA),
                    modifier = Modifier.weight(1f),
                    onClick = { onFeatureClick(UniqueFeatureSheetType.REMUNA_PRASAD_ARTISANS) }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                UniqueFeatureChip(
                    icon = "🌀",
                    label = "Cyclone Shelter",
                    sub = "Resilience Hub",
                    color = Color(0xFFF0FDF4),
                    borderColor = Color(0xFFBBF7D0),
                    modifier = Modifier.weight(1f),
                    onClick = { onFeatureClick(UniqueFeatureSheetType.CYCLONE_RESILIENCE) }
                )
                UniqueFeatureChip(
                    icon = "🐟",
                    label = "Harbor Catch",
                    sub = "Daily Rate Board",
                    color = Color(0xFFF8FAFC),
                    borderColor = Color(0xFFCBD5E1),
                    modifier = Modifier.weight(1f),
                    onClick = { onFeatureClick(UniqueFeatureSheetType.HARBOR_CATCH_RATES) }
                )
                UniqueFeatureChip(
                    icon = "🐘",
                    label = "Kuldiha Passport",
                    sub = "Wildlife Corridor",
                    color = Color(0xFFFAF5FF),
                    borderColor = Color(0xFFE9D5FF),
                    modifier = Modifier.weight(1f),
                    onClick = { onFeatureClick(UniqueFeatureSheetType.ELEPHANT_PASSPORT) }
                )
            }
        }
    }
}

@Composable
fun UniqueFeatureChip(
    icon: String,
    label: String,
    sub: String,
    color: Color,
    borderColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag("feature_chip_${label.lowercase().replace(" ", "_")}"),
        shape = RoundedCornerShape(12.dp),
        color = color,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icon, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = BentoSlate900
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = sub,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 9.sp,
                    color = BentoSlate500
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * 1. Horseshoe Crab Biodiversity & Seabed Guide Modal Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorseshoeCrabSheet(
    sightings: List<HorseshoeCrabSighting>,
    guidelines: List<IntertidalEcoGuideline>,
    onLogSighting: (reporter: String, sector: String, species: String, count: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var showLogDialog by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .testTag("horseshoe_crab_sheet")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🦀", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Horseshoe Crab Radar",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = "Chandipur Living Fossils (450M Yrs)",
                            style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate500)
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.85f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Info Banner
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                        border = BorderStroke(1.dp, Color(0xFFFCD34D))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "🛡️ Living Fossil Conservation Alert",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF92400E)
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Chandipur beach is one of the world's few surviving natural nesting zones for Tachypleus gigas and Carcinoscorpius rotundicauda. Their copper-rich blue blood (LAL) is vital for biomedical sterility testing.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF78350F),
                                    lineHeight = 16.sp
                                )
                            )
                        }
                    }
                }

                // Log a Sighting Action
                item {
                    Button(
                        onClick = { showLogDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("log_sighting_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7))
                    ) {
                        Icon(imageVector = Icons.Default.AddLocationAlt, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Log Living Fossil Sighting (FMU Biology)")
                    }
                }

                // Recent Citizen Sightings Header
                item {
                    Text(
                        text = "Recent Verified Sightings (${sightings.size})",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate900
                        )
                    )
                }

                items(sightings) { sighting ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = sighting.species,
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0369A1)
                                    )
                                )
                                Text(
                                    text = sighting.reportedAgo,
                                    style = MaterialTheme.typography.labelSmall.copy(color = BentoSlate400)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "📍 ${sighting.beachSector} • Count: ${sighting.count}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = BentoSlate700
                                )
                            )
                            Text(
                                text = "Observation: ${sighting.condition}",
                                style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate500)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = Color(0xFF16A34A),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Verified • ${sighting.reporterName}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFF16A34A),
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    }
                }

                // Intertidal Seabed Walking Guidelines
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Seabed Walking Eco-Guidelines",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate900
                        )
                    )
                }

                items(guidelines) { guide ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (guide.isDo) Color(0xFFF0FDF4) else Color(0xFFFEF2F2)
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (guide.isDo) Color(0xFFBBF7D0) else Color(0xFFFECACA)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = if (guide.isDo) "✅" else "🚫",
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = guide.title,
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (guide.isDo) Color(0xFF166534) else Color(0xFF991B1B)
                                    )
                                )
                                Text(
                                    text = guide.odiaTitle,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = BentoSlate500,
                                        fontSize = 10.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = guide.description,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = BentoSlate700,
                                        lineHeight = 15.sp
                                    )
                                )
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }

    if (showLogDialog) {
        var reporterName by remember { mutableStateOf("") }
        var sectorName by remember { mutableStateOf("Chandipur Sandbar (2.5 km)") }
        var speciesName by remember { mutableStateOf("Tachypleus gigas (Indo-Pacific)") }
        var crabCount by remember { mutableStateOf("2") }

        AlertDialog(
            onDismissRequest = { showLogDialog = false },
            title = {
                Text(
                    text = "Log Marine Living Fossil",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = reporterName,
                        onValueChange = { reporterName = it },
                        label = { Text("Your Name / Observer") },
                        placeholder = { Text("e.g. S. Das") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = sectorName,
                        onValueChange = { sectorName = it },
                        label = { Text("Beach Sector") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = crabCount,
                        onValueChange = { crabCount = it },
                        label = { Text("Count Observed") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val count = crabCount.toIntOrNull() ?: 1
                        onLogSighting(reporterName, sectorName, speciesName, count)
                        showLogDialog = false
                    }
                ) {
                    Text("Submit to FMU Bio-Radar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

/**
 * 2. ITR Chandipur Defense Trail & Maritime Exclusion Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefenseTrailSheet(
    milestones: List<DefenseTrailMilestone>,
    exclusionZone: MaritimeExclusionZone,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .testTag("defense_trail_sheet")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🚀", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "ITR Missile City & Kalam Trail",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = "Integrated Test Range & Wheeler Island",
                            style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate500)
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.85f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Live Maritime Exclusion Radar Card
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                        border = BorderStroke(1.dp, Color(0xFF334155))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF22C55E))
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "LIVE COASTAL RADAR",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF4ADE80),
                                            letterSpacing = 1.sp
                                        )
                                    )
                                }
                                Text(
                                    text = "${exclusionZone.radiusNauticalMiles} NM Radius",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFF94A3B8),
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = exclusionZone.zoneName,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "Coordinates: ${exclusionZone.coordinates}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF94A3B8),
                                    fontSize = 11.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Active Hours: ${exclusionZone.activeHours}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF38BDF8),
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Text(
                                text = exclusionZone.sirenAlert,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFFCBD5E1),
                                    fontSize = 11.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Patrol: ${exclusionZone.coastalPatrolStatus}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFF86EFAC),
                                        fontSize = 10.sp
                                    )
                                )
                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:06782272000"))
                                        context.startActivity(intent)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Fishermen Helpline", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }

                // Kalam Defense Trail Header
                item {
                    Text(
                        text = "Dr. APJ Abdul Kalam Defense Trail",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate900
                        )
                    )
                }

                items(milestones) { milestone ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = Color(0xFF0369A1),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = milestone.year,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    )
                                }
                                Text(
                                    text = "Range: ${milestone.rangeKm}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFD97706)
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = milestone.missileName,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BentoSlate900
                                )
                            )
                            Text(
                                text = "📍 ${milestone.testRange}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF0284C7),
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = milestone.significance,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = BentoSlate700,
                                    lineHeight = 16.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFEFF6FF),
                                border = BorderStroke(1.dp, Color(0xFFDBEAFE))
                            ) {
                                Text(
                                    text = milestone.drKalamMemoir,
                                    modifier = Modifier.padding(8.dp),
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF1E40AF),
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}

/**
 * 3. Remuna "Amrita Keli" Prasad & Artisan Showcase Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemunaPrasadArtisansSheet(
    prasadStatus: PrasadStatus,
    artisans: List<BalasoreArtisan>,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .testTag("remuna_prasad_sheet")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🍲", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Remuna Prasad & Artisans",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = "Sacred Amrita Keli & GI Crafts",
                            style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate500)
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.85f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Live Amrita Keli Bhog Batch Card
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                        border = BorderStroke(1.dp, Color(0xFFFDE68A))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = Color(0xFFD97706),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "LIVE BHOG RADAR",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    )
                                }
                                Text(
                                    text = "Batch: ${prasadStatus.nextBatchTime}",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF92400E)
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Sacred Amrita Keli (ଅମୃତ କେଳି କ୍ଷୀର ଭୋଗ)",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF78350F)
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = prasadStatus.batchPreparation,
                                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF92400E))
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "🏺 Available Earthen Kudua: ~${prasadStatus.estimatedKuduAvailable}",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFB45309)
                                    )
                                )
                                Text(
                                    text = "Tokens: OPEN",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF16A34A)
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = prasadStatus.recipeHeritage,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF78350F),
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }

                // Verified Artisan Showcase Header
                item {
                    Text(
                        text = "Direct Balasore Artisan Showcase",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate900
                        )
                    )
                    Text(
                        text = "Connect directly with local craftsmen • Zero middleman commission",
                        style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate500)
                    )
                }

                items(artisans) { artisan ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = artisan.craftTitle,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A)
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                                if (artisan.giTagged) {
                                    Surface(
                                        color = Color(0xFFDCFCE7),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "GI TAGGED",
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 9.sp,
                                                color = Color(0xFF166534)
                                            )
                                        )
                                    }
                                }
                            }
                            Text(
                                text = artisan.odiaCraftTitle,
                                style = MaterialTheme.typography.labelSmall.copy(color = BentoSlate500)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "👤 ${artisan.artisanName} • ${artisan.experienceYears} yrs master craft",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF0369A1)
                                )
                            )
                            Text(
                                text = "📍 ${artisan.villageOrCluster}",
                                style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate600)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = artisan.description,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = BentoSlate700,
                                    lineHeight = 15.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${artisan.phone}"))
                                        context.startActivity(intent)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF166534)),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Call Artisan Direct (${artisan.phone})", fontSize = 12.sp)
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

/**
 * 4. Cyclone Shelter & Coastal Resilience Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CycloneResilienceSheet(
    shelters: List<DetailedCycloneShelter>,
    kitItems: List<EmergencyKitItem>,
    onToggleKitItem: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val checkedCount = kitItems.count { it.isChecked }
    val progressPercent = if (kitItems.isNotEmpty()) checkedCount.toFloat() / kitItems.size else 0f

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .testTag("cyclone_resilience_sheet")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🌀", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Cyclone Shelter & Resilience",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = "Offline-Ready GPS Shelters & Kit Checklist",
                            style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate500)
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.85f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Emergency Kit Readiness Progress Card
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                        border = BorderStroke(1.dp, Color(0xFFBBF7D0))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "🧰 Disaster Emergency Kit Readiness",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF166534)
                                    )
                                )
                                Text(
                                    text = "$checkedCount/${kitItems.size} Packed",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF15803D)
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { progressPercent },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = Color(0xFF16A34A),
                                trackColor = Color(0xFFDCFCE7)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (progressPercent >= 0.8f) "Kit in optimal readiness state for coastal storm warning." else "Ensure all essential items are packed in a waterproof bag.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF14532D),
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }

                // Interactive Checklist Items
                item {
                    Text(
                        text = "Essential Kit Packing Checklist",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate900
                        )
                    )
                }

                items(kitItems) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggleKitItem(item.id) },
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = item.isChecked,
                                onCheckedChange = { onToggleKitItem(item.id) },
                                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF16A34A))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (item.isChecked) Color(0xFF15803D) else BentoSlate900
                                    )
                                )
                                Text(
                                    text = item.odiaName,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = BentoSlate500,
                                        fontSize = 10.sp
                                    )
                                )
                                Text(
                                    text = item.reason,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = BentoSlate600,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }

                // Nearest Multipurpose Cyclone Shelters Header
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Multipurpose Cyclone Shelters (Offline GPS)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate900
                        )
                    )
                    Text(
                        text = "High-ground concrete structures with solar power & rainwater harvesting",
                        style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate500)
                    )
                }

                items(shelters) { shelter ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = shelter.name,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = BentoSlate900
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                                Surface(
                                    color = Color(0xFFE0F2FE),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "${shelter.distanceKm} km",
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0369A1)
                                        )
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Block: ${shelter.block} • Capacity: ${shelter.capacity} People • High Ground: +${shelter.elevationMeters}m",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = BentoSlate600,
                                    fontSize = 11.sp
                                )
                            )
                            if (shelter.solarBackup) {
                                Text(
                                    text = "⚡ Independent Solar Backup & Rainwater Cistern Installed",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFF16A34A),
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Nodal: ${shelter.nodalOfficer}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = BentoSlate500,
                                        fontSize = 11.sp
                                    )
                                )
                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${shelter.phone}"))
                                        context.startActivity(intent)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Call Shelter (${shelter.phone})", fontSize = 11.sp)
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

/**
 * 5. Balaramgadi Harbor Catch & Daily Market Rate Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HarborCatchRatesSheet(
    catchRates: List<HarborCatchRate>,
    landingBells: List<HarborLandingBell>,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .testTag("harbor_catch_rates_sheet")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🐟", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Balaramgadi Harbor Catch & Rates",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = "Fresh Trawler Landings & Wholesale Rates",
                            style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate500)
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.85f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Harbor Landing Bell Cards
                item {
                    Text(
                        text = "🔔 Trawler Fleet Landing Schedule",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate900
                        )
                    )
                }

                items(landingBells) { bell ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                        border = BorderStroke(1.dp, Color(0xFFBFDBFE))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "⚓", fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = bell.fleetName,
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF1E3A8A)
                                        )
                                    )
                                    Surface(
                                        color = if (bell.status == "Docking Now") Color(0xFFDCFCE7) else Color(0xFFFEF3C7),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = bell.status,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 9.sp,
                                                color = if (bell.status == "Docking Now") Color(0xFF166534) else Color(0xFF92400E)
                                            )
                                        )
                                    }
                                }
                                Text(
                                    text = "Harbor: ${bell.harbor} • Expected: ${bell.expectedDockTime}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate600)
                                )
                                Text(
                                    text = bell.catchSummary,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF0284C7),
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        }
                    }
                }

                // Daily Catch Rate Board Header
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Daily Market Rate Board (Wholesale vs Retail)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate900
                        )
                    )
                }

                items(catchRates) { rate ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = rate.fishName,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = BentoSlate900
                                        )
                                    )
                                    Text(
                                        text = rate.odiaName,
                                        style = MaterialTheme.typography.labelSmall.copy(color = BentoSlate500)
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "Wholesale: ${rate.wholesalePriceKg}",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0369A1)
                                        )
                                    )
                                    Text(
                                        text = "Retail: ${rate.retailPriceKg}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF16A34A)
                                        )
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "📍 ${rate.dockLanding} • ${rate.arrivalTime}",
                                style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate600)
                            )
                            Text(
                                text = "Freshness: ${rate.freshnessRating}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF0284C7),
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFF1F5F9)
                            ) {
                                Text(
                                    text = "💡 Buyer's Tip: ${rate.buyingTip}",
                                    modifier = Modifier.padding(8.dp),
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = BentoSlate700,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}

/**
 * 6. Kuldiha Elephant Corridor & Ecotourism Passport Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElephantPassportSheet(
    corridorAlert: ElephantCorridorAlert,
    passportStamps: List<EcoPassportStamp>,
    onToggleStamp: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val stampedCount = passportStamps.count { it.isCheckedIn }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .testTag("elephant_passport_sheet")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🐘", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Kuldiha Corridor & Eco-Passport",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = "Elephant Safety Alerts & Nature Passport",
                            style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate500)
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.85f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Wildlife Corridor Safety Bulletin
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                        border = BorderStroke(1.dp, Color(0xFFFCD34D))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = Color(0xFFD97706),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = corridorAlert.alertLevel,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    )
                                }
                                Text(
                                    text = "Herd Size: ~${corridorAlert.herdCount}",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF92400E)
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = corridorAlert.sector,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF78350F)
                                )
                            )
                            Text(
                                text = "Last Reported: ${corridorAlert.lastSpottedTime}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF92400E),
                                    fontSize = 11.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = corridorAlert.advisoryText,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF78350F),
                                    lineHeight = 16.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "🚗 Speed Limit: ${corridorAlert.highwaySpeedLimitKmh} km/h max",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF92400E)
                                    )
                                )
                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${corridorAlert.forestControlPhone}"))
                                        context.startActivity(intent)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF15803D)),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Forest Control", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }

                // Digital Eco-Passport Header & Progress
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Digital Ecotourism Passport",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BentoSlate900
                                )
                            )
                            Text(
                                text = "Check in to collect Kuldiha Ranger badges",
                                style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate500)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFDCFCE7)
                        ) {
                            Text(
                                text = "$stampedCount/4 STAMPS",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF166534)
                                )
                            )
                        }
                    }
                }

                items(passportStamps) { stamp ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggleStamp(stamp.id) }
                            .testTag("stamp_${stamp.id}"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (stamp.isCheckedIn) Color(0xFFF0FDF4) else Color(0xFFF8FAFC)
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (stamp.isCheckedIn) Color(0xFF86EFAC) else Color(0xFFE2E8F0)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(if (stamp.isCheckedIn) Color(0xFF16A34A) else Color(0xFFE2E8F0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (stamp.isCheckedIn) "✓" else "⛺",
                                    fontSize = 20.sp,
                                    color = if (stamp.isCheckedIn) Color.White else BentoSlate500,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = stamp.title,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (stamp.isCheckedIn) Color(0xFF166534) else BentoSlate900
                                        )
                                    )
                                    Text(
                                        text = stamp.elevation,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = BentoSlate400,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                                Text(
                                    text = stamp.odiaTitle,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = BentoSlate500,
                                        fontSize = 10.sp
                                    )
                                )
                                Text(
                                    text = "📍 ${stamp.location}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate600)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = stamp.keyAttraction,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = BentoSlate700,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}
