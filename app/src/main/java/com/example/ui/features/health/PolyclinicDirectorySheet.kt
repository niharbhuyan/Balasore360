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
import com.example.data.daily.DailyUpdateEngine
import com.example.data.repository.HealthcareRepository
import com.example.data.repository.PolyclinicCenter
import com.example.ui.components.openInGoogleMaps

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PolyclinicDirectorySheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dailyPulse = remember { DailyUpdateEngine.getDailyPulse() }
    val allClinics = remember { HealthcareRepository.polyclinicsList }

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }

    val filterOptions = remember {
        listOf("All", "Multi-Specialty", "Diagnostic & Imaging", "NABL Accredited", "Daycare OT", "Remuna & FMMCH", "Nilagiri")
    }

    val filteredClinics = remember(searchQuery, selectedFilter) {
        allClinics.filter { clinic ->
            val matchesFilter = when (selectedFilter) {
                "Multi-Specialty" -> clinic.category.contains("Multi-specialty", ignoreCase = true)
                "Diagnostic & Imaging" -> clinic.category.contains("Imaging", ignoreCase = true) || clinic.facilities.any { it.contains("USG", ignoreCase = true) || it.contains("X-Ray", ignoreCase = true) }
                "NABL Accredited" -> clinic.category.contains("NABL", ignoreCase = true) || clinic.facilities.any { it.contains("NABL", ignoreCase = true) }
                "Daycare OT" -> clinic.category.contains("Daycare", ignoreCase = true) || clinic.facilities.any { it.contains("Daycare", ignoreCase = true) || it.contains("OT", ignoreCase = true) }
                "Remuna & FMMCH" -> clinic.address.contains("Remuna", ignoreCase = true)
                "Nilagiri" -> clinic.address.contains("Nilagiri", ignoreCase = true)
                else -> true
            }

            val matchesSearch = searchQuery.isBlank() ||
                    clinic.name.contains(searchQuery, ignoreCase = true) ||
                    clinic.category.contains(searchQuery, ignoreCase = true) ||
                    clinic.address.contains(searchQuery, ignoreCase = true) ||
                    clinic.facilities.any { it.contains(searchQuery, ignoreCase = true) } ||
                    clinic.visitingSpecialistsList.any { it.contains(searchQuery, ignoreCase = true) }

            matchesFilter && matchesSearch
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.92f)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("polyclinic_directory_sheet")
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
                        .background(Color(0xFFEDE9FE)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Biotech,
                        contentDescription = null,
                        tint = Color(0xFF7C3AED),
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Balasore Polyclinics & Labs",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    )
                    Text(
                        text = "ପଲିକ୍ଲିନିକ୍ ଓ ଡାଇଗ୍ନୋଷ୍ଟିକ୍ କେନ୍ଦ୍ର • Diagnostics Hub",
                        fontSize = 12.sp,
                        color = Color(0xFF7C3AED),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            IconButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("close_polyclinic_sheet_btn")
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Daily Auto-Update Diagnostic & Lab Status Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF5FF)),
            border = BorderStroke(1.5.dp, Color(0xFFDDD6FE))
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
                            color = Color(0xFF7C3AED),
                            modifier = Modifier.size(8.dp)
                        ) {}
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "TODAY'S LAB SAMPLE COLLECTION STATUS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6D28D9)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFEDE9FE)
                    ) {
                        Text(
                            text = "FAST-TRACK",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF7C3AED),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = dailyPulse.polyclinicSampleCollectionStatus,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF4C1D95)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "🔬 Same-day digital test reports available at Balasore Poly Clinic, Kalinga & Apollo Diagnostics",
                    fontSize = 11.sp,
                    color = Color(0xFF6D28D9)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search polyclinic, USG, X-Ray, blood tests...", fontSize = 13.sp) },
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
                .testTag("polyclinic_search_input"),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF7C3AED),
                unfocusedBorderColor = Color(0xFFE2E8F0),
                focusedContainerColor = Color(0xFFF8FAFC),
                unfocusedContainerColor = Color(0xFFF8FAFC)
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Filter Chips
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
                        selectedContainerColor = Color(0xFF7C3AED),
                        selectedLabelColor = Color.White,
                        containerColor = Color(0xFFF1F5F9),
                        labelColor = Color(0xFF334155)
                    ),
                    border = null
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Polyclinic List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredClinics, key = { it.id }) { clinic ->
                PolyclinicCardItem(
                    clinic = clinic,
                    context = context
                )
            }

            if (filteredClinics.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.SearchOff, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(40.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No clinics or diagnostic centers found for '$searchQuery'", color = Color(0xFF64748B), fontSize = 13.sp)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun PolyclinicCardItem(
    clinic: PolyclinicCenter,
    context: Context,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("polyclinic_card_${clinic.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Row 1: Name, Category
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = clinic.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = clinic.odiaName,
                        fontSize = 12.sp,
                        color = Color(0xFF7C3AED),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "📍 ${clinic.address} • ${clinic.landmark}",
                        fontSize = 11.sp,
                        color = Color(0xFF475569)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFEDE9FE),
                    border = BorderStroke(1.dp, Color(0xFFDDD6FE))
                ) {
                    Text(
                        text = clinic.category.substringBefore("&").trim(),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6D28D9),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Timings & Sample Collection
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF8FAFC)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Schedule, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Open: ${clinic.openHours}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1E293B)
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "🩸 ${clinic.sampleCollectionTimings}",
                        fontSize = 10.sp,
                        color = Color(0xFF475569)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Diagnostic Facilities Chips
            Text(
                text = "Key Diagnostic & Clinical Facilities:",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF334155)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                clinic.facilities.take(3).forEach { facility ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFF1F5F9)
                    ) {
                        Text(
                            text = facility,
                            fontSize = 9.sp,
                            color = Color(0xFF1E293B),
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Visiting Specialists Roster
            if (clinic.visitingSpecialistsList.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF0FDF4),
                    border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = "👨‍⚕️ Visiting Specialist Chambers:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF166534)
                        )
                        clinic.visitingSpecialistsList.forEach { doctor ->
                            Text(
                                text = "• $doctor",
                                fontSize = 10.sp,
                                color = Color(0xFF14532D)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${clinic.phone}"))
                        context.startActivity(dialIntent)
                    },
                    modifier = Modifier
                        .weight(1.2f)
                        .testTag("call_clinic_${clinic.id}"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED)),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Call Reception", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = {
                        openInGoogleMaps(context, clinic.latitude, clinic.longitude, clinic.name)
                    },
                    modifier = Modifier
                        .weight(0.8f)
                        .testTag("map_clinic_${clinic.id}"),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color(0xFF7C3AED)),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Navigation, contentDescription = null, tint = Color(0xFF7C3AED), modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Map", fontSize = 12.sp, color = Color(0xFF7C3AED), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
