package com.example.ui.features.emergency

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.data.model.AppLanguage
import com.example.data.model.BloodDonorContact
import com.example.data.model.HospitalBedPulse
import com.example.data.model.RareBloodAlert
import com.example.data.repository.SmartFeaturesRepository

@Composable
fun BloodDonorBedPulseSheet(
    language: AppLanguage = AppLanguage.ENGLISH,
    onClose: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedBloodGroup by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }

    val donors = SmartFeaturesRepository.bloodDonors
    val rareAlerts = SmartFeaturesRepository.rareBloodAlerts
    val beds = SmartFeaturesRepository.hospitalBedPulse

    val bloodGroups = listOf("All", "O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-")

    val filteredDonors = remember(selectedBloodGroup, searchQuery) {
        donors.filter { donor ->
            val matchGroup = selectedBloodGroup == "All" || donor.bloodGroup.equals(selectedBloodGroup, ignoreCase = true)
            val matchQuery = searchQuery.isBlank() ||
                donor.fullName.contains(searchQuery, ignoreCase = true) ||
                donor.blockTown.contains(searchQuery, ignoreCase = true) ||
                donor.bloodGroup.contains(searchQuery, ignoreCase = true)
            matchGroup && matchQuery
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("blood_donor_bed_pulse_sheet"),
        contentPadding = PaddingValues(bottom = 36.dp)
    ) {
        // Hero Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF881337), Color(0xFFBE123C), Color(0xFFE11D48))
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "24x7 BLOOD & ICU BED PULSE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFFECDD3),
                                    fontSize = 10.sp
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF22C55E),
                            modifier = Modifier.size(10.dp)
                        ) {}
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (language == AppLanguage.ODIA)
                            "ବାଲେଶ୍ୱର ରକ୍ତଦାତା ଡାଇରେକ୍ଟୋରି ଓ ICU ବେଡ୍ ପଲ୍ସ"
                        else
                            "Blood Donors & ICU Bed Pulse",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (language == AppLanguage.ODIA)
                            "ସ୍ୱେଚ୍ଛାସେବୀ ରକ୍ତଦାତା ସଂପର୍କ, ବିରଳ ଗ୍ରୁପ୍ SOS ସତର୍କତା, ଏବଂ FMMCH/DHH/ବେସରକାରୀ ହସ୍ପିଟାଲର ଲାଇଭ୍ ଆଇସିୟୁ ଓ ଡାଏଲିସିସ୍ ବେଡ୍ ସ୍ଥିତି।"
                        else
                            "Verified volunteer donors by block, SOS rare blood appeals, and live ICU/Oxygen/Dialysis bed availability across Balasore hospitals.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    )
                }
            }
        }

        // Section: Active SOS Rare Blood Emergency Alerts
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Emergency,
                        contentDescription = "Urgent",
                        tint = Color(0xFFE11D48),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == AppLanguage.ODIA) "ଜରୁରୀକାଳୀନ ବିରଳ ରକ୍ତ ଆବଶ୍ୟକତା (SOS Alerts)" else "Active SOS Rare Blood Appeals",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF9F1239)
                        )
                    )
                }
            }
        }

        items(rareAlerts, key = { it.id }) { alert ->
            RareBloodCard(alert = alert, onCall = {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${alert.contactPhone.replace("-", "")}"))
                context.startActivity(intent)
            })
        }

        // Section: Live Hospital ICU & Dialysis Bed Pulse
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocalHospital,
                        contentDescription = "Hospitals",
                        tint = Color(0xFF0284C7),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == AppLanguage.ODIA) "ଡାକ୍ତରଖାନା ICU, ଅକ୍ସିଜେନ ଓ ଡାଏଲିସିସ୍ ବେଡ୍ ପଲ୍ସ" else "Hospital ICU & Dialysis Bed Pulse",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
                Text(
                    text = "FMMCH Balasore, DHH, Jyoti Hospital & Balasore Lifeline",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                )
            }
        }

        items(beds, key = { it.hospitalId }) { bed ->
            HospitalBedCard(bed = bed, language = language, onCall = {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${bed.emergencyContact.replace("-", "")}"))
                context.startActivity(intent)
            })
        }

        // Section: Voluntary Blood Donor Directory (Filterable)
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
                Text(
                    text = if (language == AppLanguage.ODIA) "ସ୍ୱେଚ୍ଛାସେବୀ ରକ୍ତଦାତା ତାଲିକା" else "Verified Voluntary Donor Directory",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by name, block (e.g., Soro, Remuna, Sadar)...", fontSize = 12.5.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFFE11D48)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE11D48),
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(bloodGroups) { group ->
                        FilterChip(
                            selected = selectedBloodGroup == group,
                            onClick = { selectedBloodGroup = group },
                            label = { Text(group, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFE11D48),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        items(filteredDonors, key = { it.id }) { donor ->
            DonorCard(donor = donor, onCall = {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${donor.contactPhone}"))
                context.startActivity(intent)
            })
        }
    }
}

@Composable
private fun RareBloodCard(alert: RareBloodAlert, onCall: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F2)),
        border = BorderStroke(1.dp, Color(0xFFFECDD3))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFE11D48)
                ) {
                    Text(
                        text = alert.bloodGroup,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFFE4E6)
                ) {
                    Text(
                        text = "${alert.unitsNeeded} Units Required",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFFBE123C),
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.5.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${alert.patientNameOrBed} • ${alert.hospital}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF881337)
                )
            )
            Text(
                text = "Contact: ${alert.contactPerson} (${alert.postedTimeAgo})",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF9F1239),
                    fontSize = 11.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onCall,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Call, contentDescription = "Call", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Call ${alert.contactPhone}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun HospitalBedCard(bed: HospitalBedPulse, language: AppLanguage, onCall: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (language == AppLanguage.ODIA) bed.odiaName else bed.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = "${bed.location} • Updated ${bed.lastUpdatedMinutesAgo}m ago",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 10.5.sp
                        )
                    )
                }

                IconButton(onClick = onCall) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call Hospital",
                        tint = Color(0xFF0284C7)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BedMetricBox(
                    label = "ICU Beds",
                    available = bed.availableIcuBeds,
                    total = bed.totalIcuBeds,
                    color = if (bed.availableIcuBeds > 0) Color(0xFF059669) else Color(0xFFEF4444),
                    modifier = Modifier.weight(1f)
                )
                BedMetricBox(
                    label = "Oxygen Beds",
                    available = bed.availableOxygenBeds,
                    total = bed.totalOxygenBeds,
                    color = Color(0xFF0284C7),
                    modifier = Modifier.weight(1f)
                )
                BedMetricBox(
                    label = "Dialysis",
                    available = bed.availableDialysisSlots,
                    total = bed.dialysisSlotsTotal,
                    color = Color(0xFF7C3AED),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun BedMetricBox(
    label: String,
    available: Int,
    total: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, fontSize = 10.sp, color = color, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "$available / $total",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = if (available > 0) "Available" else "Full",
                fontSize = 9.sp,
                color = color.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun DonorCard(donor: BloodDonorContact, onCall: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFFFF1F2),
                    border = BorderStroke(1.dp, Color(0xFFFECDD3)),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = donor.bloodGroup,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp,
                            color = Color(0xFFE11D48)
                        )
                    }
                }

                Column {
                    Text(
                        text = donor.fullName,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = "${donor.blockTown} • ${donor.availabilityStatus}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 10.5.sp
                        )
                    )
                }
            }

            IconButton(
                onClick = onCall,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE11D48))
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Call Donor",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
