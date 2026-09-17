package com.example.ui.features.emergency

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
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Schedule
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

data class HealthcareFacility(
    val id: String,
    val name: String,
    val odiaName: String,
    val type: String, // Blood Bank, Dialysis, Volunteer
    val location: String,
    val phoneNumber: String,
    val timing: String,
    val bloodGroupsAvailable: List<String>,
    val latitude: Double,
    val longitude: Double,
    val is24x7: Boolean = false
)

/**
 * Emergency Blood Donor & Dialysis Center Directory component.
 */
@Composable
fun BloodAndDialysisDirectorySheet(
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedFilter by remember { mutableStateOf("All") }

    val facilities = remember {
        listOf(
            HealthcareFacility(
                id = "med_1",
                name = "Balasore DHH Blood Bank (Dist Headquarters)",
                odiaName = "ଜିଲ୍ଲା ମୁଖ୍ୟ ଡାକ୍ତରଖାନା ରକ୍ତ ଭଣ୍ଡାର",
                type = "Blood Bank",
                location = "DHH Campus, Hospital Road, Balasore",
                phoneNumber = "06782262024",
                timing = "24x7 Round-the-Clock Emergency",
                bloodGroupsAvailable = listOf("A+", "B+", "O+", "AB+", "O-", "A-"),
                latitude = 21.4934,
                longitude = 86.9325,
                is24x7 = true
            ),
            HealthcareFacility(
                id = "med_2",
                name = "Fakir Mohan Medical College Blood Centre",
                odiaName = "ଫକୀର ମୋହନ ମେଡିକାଲ କଲେଜ ରକ୍ତ କେନ୍ଦ୍ର",
                type = "Blood Bank",
                location = "FMMCH Campus, Remuna, Balasore",
                phoneNumber = "06782255001",
                timing = "24x7 Emergency Transfusion Unit",
                bloodGroupsAvailable = listOf("A+", "B+", "O+", "AB+", "B-", "AB-"),
                latitude = 21.5284,
                longitude = 86.8647,
                is24x7 = true
            ),
            HealthcareFacility(
                id = "med_3",
                name = "Indian Red Cross Society Balasore Branch",
                odiaName = "ଭାରତୀୟ ରେଡକ୍ରସ ସୋସାଇଟି ବାଲେଶ୍ୱର",
                type = "Volunteer",
                location = "Near Collectorate, Cinema Chhak, Balasore",
                phoneNumber = "06782262112",
                timing = "09:00 AM - 08:00 PM (Emergency on-call)",
                bloodGroupsAvailable = listOf("O+", "A+", "B+", "Rare Groups Network"),
                latitude = 21.4970,
                longitude = 86.9340,
                is24x7 = false
            ),
            HealthcareFacility(
                id = "med_4",
                name = "DHH Dialysis & Nephrology Unit",
                odiaName = "ଡି.ଏଚ୍.ଏଚ୍. ଡାୟାଲିସିସ୍ କେନ୍ଦ୍ର",
                type = "Dialysis",
                location = "Super Specialty Wing, DHH Balasore",
                phoneNumber = "06782262025",
                timing = "3 Shifts (06:00 AM - 10:00 PM)",
                bloodGroupsAvailable = emptyList(),
                latitude = 21.4934,
                longitude = 86.9325,
                is24x7 = false
            ),
            HealthcareFacility(
                id = "med_5",
                name = "Apollo Clinic Dialysis & Kidney Care",
                odiaName = "ଆପୋଲୋ କ୍ଲିନିକ୍ ଡାୟାଲିସିସ୍ ୱିଙ୍ଗ",
                type = "Dialysis",
                location = "OT Road, Near Railway Overbridge, Balasore",
                phoneNumber = "06782240500",
                timing = "07:00 AM - 09:00 PM",
                bloodGroupsAvailable = emptyList(),
                latitude = 21.5010,
                longitude = 86.9280,
                is24x7 = false
            ),
            HealthcareFacility(
                id = "med_6",
                name = "Balasore Voluntary Rare Blood Network",
                odiaName = "ଦୁର୍ଲ୍ଲଭ ରକ୍ତଦାତା ସ୍ୱେଚ୍ଛାସେବୀ ନେଟୱାର୍କ",
                type = "Volunteer",
                location = "District-wide Verified Emergency Network",
                phoneNumber = "9437012345",
                timing = "24x7 Emergency Call Desk",
                bloodGroupsAvailable = listOf("O-", "A-", "B-", "AB-", "Bombay Blood"),
                latitude = 21.4934,
                longitude = 86.9325,
                is24x7 = true
            )
        )
    }

    val filterOptions = listOf("All", "Blood Bank", "Dialysis", "Volunteer", "24x7")
    val filteredFacilities = when (selectedFilter) {
        "All" -> facilities
        "24x7" -> facilities.filter { it.is24x7 }
        else -> facilities.filter { it.type.equals(selectedFilter, ignoreCase = true) }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("blood_and_dialysis_directory_sheet")
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
                                listOf(Color(0xFFDC2626), Color(0xFFB91C1C))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Bloodtype,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Blood Bank & Dialysis Centers",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "ଜରୁରୀକାଳୀନ ରକ୍ତ ଭଣ୍ଡାର ଓ ଡାୟାଲିସିସ୍ ନିର୍ଦ୍ଦେଶିକା",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFDC2626),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFFEE2E2)
            ) {
                Text(
                    text = "Emergency 24x7",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF991B1B)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Filter Options
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(filterOptions) { opt ->
                FilterChip(
                    selected = selectedFilter == opt,
                    onClick = { selectedFilter = opt },
                    label = { Text(opt) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFDC2626),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Facilities List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f, fill = false)
        ) {
            items(filteredFacilities, key = { it.id }) { fac ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = fac.name,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = fac.odiaName,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFFDC2626),
                                        fontSize = 11.sp
                                    )
                                )
                                Text(
                                    text = fac.location,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.sp
                                    )
                                )
                            }

                            if (fac.is24x7) {
                                Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFDCFCE7)) {
                                    Text(
                                        text = "24x7",
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF15803D)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Schedule, null, modifier = Modifier.size(12.dp), tint = Color(0xFF64748B))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(fac.timing, fontSize = 11.sp, color = Color(0xFF64748B))
                        }

                        if (fac.bloodGroupsAvailable.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                fac.bloodGroupsAvailable.forEach { grp ->
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = Color(0xFFFEE2E2)
                                    ) {
                                        Text(
                                            text = grp,
                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF991B1B)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Actions
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                                        data = Uri.parse("tel:${fac.phoneNumber}")
                                    }
                                    context.startActivity(dialIntent)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(36.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp)
                            ) {
                                Icon(Icons.Default.Call, null, modifier = Modifier.size(14.dp), tint = Color.White)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Call Now", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }

                            OutlinedButton(
                                onClick = {
                                    openInGoogleMaps(context, fac.latitude, fac.longitude, fac.name)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(36.dp),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp)
                            ) {
                                Icon(Icons.Default.Navigation, null, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Directions", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Close Emergency Directory", fontWeight = FontWeight.Bold)
        }
    }
}
