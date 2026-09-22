package com.example.ui.features.health

import android.content.Context
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
import com.example.data.repository.HealthcareRepository
import com.example.data.repository.PrivateHospital
import com.example.ui.components.openInGoogleMaps

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivateHospitalDirectorySheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val allHospitals = remember { HealthcareRepository.privateHospitalsList }

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }

    val filterOptions = remember {
        listOf(
            "All",
            "Multi-Speciality",
            "Maternity & Nursing",
            "BSKY Accepted",
            "ICU & Ventilator",
            "NICU Baby Care",
            "Dialysis Unit",
            "Blood Storage"
        )
    }

    val filteredHospitals = remember(searchQuery, selectedFilter) {
        allHospitals.filter { hospital ->
            val matchesFilter = when (selectedFilter) {
                "Multi-Speciality" -> hospital.category.contains("Multi-Speciality", ignoreCase = true)
                "Maternity & Nursing" -> hospital.category.contains("Maternity", ignoreCase = true) || hospital.category.contains("Nursing", ignoreCase = true)
                "BSKY Accepted" -> hospital.acceptedSchemes.any { it.contains("BSKY", ignoreCase = true) }
                "ICU & Ventilator" -> hospital.icuBedsCount > 0
                "NICU Baby Care" -> hospital.hasNicuPicU
                "Dialysis Unit" -> hospital.hasDialysisUnit
                "Blood Storage" -> hospital.hasBloodStorageOrBank
                else -> true
            }

            val matchesSearch = searchQuery.isBlank() ||
                    hospital.name.contains(searchQuery, ignoreCase = true) ||
                    hospital.odiaName.contains(searchQuery, ignoreCase = true) ||
                    hospital.address.contains(searchQuery, ignoreCase = true) ||
                    hospital.landmark.contains(searchQuery, ignoreCase = true) ||
                    hospital.specialtiesAvailable.any { it.contains(searchQuery, ignoreCase = true) } ||
                    hospital.keySurgeonsAndDoctors.any { it.contains(searchQuery, ignoreCase = true) } ||
                    hospital.acceptedSchemes.any { it.contains(searchQuery, ignoreCase = true) }

            matchesFilter && matchesSearch
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.94f)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("private_hospital_directory_sheet")
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0F2FE)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.LocalHospital,
                        contentDescription = null,
                        tint = Color(0xFF0284C7),
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Balasore Private Hospitals",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    )
                    Text(
                        text = "ବେସରକାରୀ ଡାକ୍ତରଖାନା ଓ ନର୍ସିଂହୋମ୍ • 24x7 ICU & Surgery",
                        fontSize = 12.sp,
                        color = Color(0xFF0284C7),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            IconButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("close_hospital_sheet_btn")
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Daily Auto-Updating Hospital Roster Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
            border = BorderStroke(1.5.dp, Color(0xFFBBF7D0))
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
                            color = Color(0xFF16A34A),
                            modifier = Modifier.size(8.dp)
                        ) {}
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "LIVE CASUALTY & ADMISSION ROSTER",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF15803D)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFDCFCE7)
                    ) {
                        Text(
                            text = "24x7 TRAUMA READY",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF166534),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "🚨 Jyoti, Life Care & Kalinga Super Speciality active for emergency polytrauma, modular OTs & continuous dialysis.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF14532D)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "💳 BSKY, BSKY Nabin, Ayushman Bharat & cashless insurance desk operating in inpatient wings.",
                    fontSize = 11.sp,
                    color = Color(0xFF166534)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search Input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search hospital, doctor, specialty (e.g. Ortho, ICU, BSKY)...", fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF64748B)) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Color(0xFF64748B))
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("hospital_search_input"),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF0284C7),
                unfocusedBorderColor = Color(0xFFE2E8F0),
                focusedContainerColor = Color(0xFFF8FAFC),
                unfocusedContainerColor = Color(0xFFF8FAFC)
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Filter Chips Row
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(filterOptions) { filter ->
                val isSelected = selectedFilter == filter
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedFilter = filter },
                    label = {
                        Text(
                            text = filter,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF0284C7),
                        selectedLabelColor = Color.White,
                        containerColor = Color(0xFFF1F5F9),
                        labelColor = Color(0xFF334155)
                    ),
                    border = null
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Hospital Cards List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(filteredHospitals, key = { it.id }) { hospital ->
                HospitalCardItem(
                    hospital = hospital,
                    statusText = HealthcareRepository.getHospitalDailyStatus(hospital),
                    context = context
                )
            }
        }
    }
}

@Composable
private fun HospitalCardItem(
    hospital: PrivateHospital,
    statusText: String,
    context: Context
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("hospital_card_${hospital.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Name, Category, Beds Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = hospital.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = hospital.odiaName,
                        fontSize = 12.sp,
                        color = Color(0xFF0284C7),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "📍 ${hospital.address} • ${hospital.landmark}",
                        fontSize = 11.sp,
                        color = Color(0xFF475569)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF0F9FF),
                    border = BorderStroke(1.dp, Color(0xFFBAE6FD))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${hospital.totalBeds} BEDS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0369A1)
                        )
                        if (hospital.icuBedsCount > 0) {
                            Text(
                                text = "${hospital.icuBedsCount} ICU",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFDC2626)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Dynamic Live Operational Status
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF8FAFC),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = statusText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0F172A)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Accepted Schemes (BSKY, Ayushman Bharat, etc.)
            Text(
                text = "Government & Cashless Schemes:",
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF475569)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                hospital.acceptedSchemes.take(3).forEach { scheme ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFECFDF5),
                        border = BorderStroke(1.dp, Color(0xFFA7F3D0))
                    ) {
                        Text(
                            text = scheme,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF065F46),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Key Facilities Tags (ICU, NICU, Dialysis, Blood Bank)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (hospital.is24x7Emergency) {
                    BadgeChip(text = "🚨 24x7 Casualty", color = Color(0xFFFEE2E2), textColor = Color(0xFF991B1B))
                }
                if (hospital.hasDialysisUnit) {
                    BadgeChip(text = "🩸 Dialysis Unit", color = Color(0xFFFEF3C7), textColor = Color(0xFF92400E))
                }
                if (hospital.hasNicuPicU) {
                    BadgeChip(text = "👶 NICU Nursery", color = Color(0xFFEDE9FE), textColor = Color(0xFF5B21B6))
                }
                if (hospital.hasBloodStorageOrBank) {
                    BadgeChip(text = "🏥 Blood Storage", color = Color(0xFFFFEDD5), textColor = Color(0xFF9A3412))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Specialties & Doctor Chambers
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF1F5F9),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "🩺 Clinical Specialties:",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF334155)
                    )
                    Text(
                        text = hospital.specialtiesAvailable.joinToString(" • "),
                        fontSize = 10.sp,
                        color = Color(0xFF1E293B)
                    )

                    if (hospital.keySurgeonsAndDoctors.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "👨‍⚕️ Key Specialists & Surgeons:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E3A8A)
                        )
                        hospital.keySurgeonsAndDoctors.forEach { doc ->
                            Text(
                                text = "• $doc",
                                fontSize = 9.5.sp,
                                color = Color(0xFF1E40AF)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // OPD & Visiting Hours
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "OPD: ${hospital.opdTimings}",
                    fontSize = 10.sp,
                    color = Color(0xFF64748B)
                )
                Text(
                    text = "Visit: ${hospital.visitingHours.substringBefore("&")}",
                    fontSize = 10.sp,
                    color = Color(0xFF64748B)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons: Call Emergency, Call Reception, Ambulance, Maps
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Button(
                    onClick = {
                        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${hospital.emergencyPhone}"))
                        context.startActivity(dialIntent)
                    },
                    modifier = Modifier
                        .weight(1.2f)
                        .testTag("call_emergency_${hospital.id}"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Emergency, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Emergency", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${hospital.receptionPhone}"))
                        context.startActivity(dialIntent)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("call_reception_${hospital.id}"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Reception", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = {
                        openInGoogleMaps(context, hospital.latitude, hospital.longitude, hospital.name)
                    },
                    modifier = Modifier
                        .weight(0.8f)
                        .testTag("map_hospital_${hospital.id}"),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color(0xFF0284C7)),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Navigation, contentDescription = null, tint = Color(0xFF0284C7), modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Map", fontSize = 11.sp, color = Color(0xFF0284C7), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun BadgeChip(text: String, color: Color, textColor: Color) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = color
    ) {
        Text(
            text = text,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
        )
    }
}
