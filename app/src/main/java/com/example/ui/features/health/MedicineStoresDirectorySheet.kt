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
import com.example.data.repository.MedicineStore
import com.example.ui.components.openInGoogleMaps

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineStoresDirectorySheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dailyPulse = remember { DailyUpdateEngine.getDailyPulse() }
    val allStores = remember { HealthcareRepository.medicineStoresList }

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }

    val filterOptions = remember {
        listOf(
            "All",
            "10 km Town Locality",
            "24x7 Open",
            "Jan Aushadhi Generic",
            "Home Delivery",
            "Kuruda & Remuna",
            "Station & OT Road",
            "DHH & Hospitals"
        )
    }

    val filteredStores = remember(searchQuery, selectedFilter) {
        allStores.filter { store ->
            val matchesFilter = when (selectedFilter) {
                "10 km Town Locality" -> {
                    // Balasore central latitude 21.4934, longitude 86.9325.
                    // Stores within town locality: Station, OT Road, Azimabad, Remuna, Kuruda, Sovarampur, Nayabazar, Manikhamb, DHH, FMMCH
                    !store.areaLocation.contains("Soro", ignoreCase = true) &&
                    !store.areaLocation.contains("Jaleswar", ignoreCase = true) &&
                    !store.areaLocation.contains("Nilagiri", ignoreCase = true)
                }
                "24x7 Open" -> store.is24x7
                "Jan Aushadhi Generic" -> store.isJanAushadhiGeneric
                "Home Delivery" -> store.hasHomeDelivery
                "Kuruda & Remuna" -> store.areaLocation.contains("Kuruda", ignoreCase = true) || store.areaLocation.contains("Remuna", ignoreCase = true)
                "Station & OT Road" -> store.areaLocation.contains("Station", ignoreCase = true) || store.areaLocation.contains("OT Road", ignoreCase = true) || store.areaLocation.contains("Azimabad", ignoreCase = true)
                "DHH & Hospitals" -> store.areaLocation.contains("DHH", ignoreCase = true) || store.areaLocation.contains("FMMCH", ignoreCase = true) || store.areaLocation.contains("Hospital", ignoreCase = true)
                else -> true
            }

            val matchesSearch = searchQuery.isBlank() ||
                    store.name.contains(searchQuery, ignoreCase = true) ||
                    store.odiaName.contains(searchQuery, ignoreCase = true) ||
                    store.areaLocation.contains(searchQuery, ignoreCase = true) ||
                    store.fullAddress.contains(searchQuery, ignoreCase = true) ||
                    store.licensedPharmacistName.contains(searchQuery, ignoreCase = true) ||
                    store.emergencyMedicines.any { it.contains(searchQuery, ignoreCase = true) }

            matchesFilter && matchesSearch
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.92f)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("medicine_stores_sheet")
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
                        .background(Color(0xFFDCFCE7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.LocalPharmacy,
                        contentDescription = null,
                        tint = Color(0xFF16A34A),
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Balasore Medicine Stores",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    )
                    Text(
                        text = "ଔଷଧ ଦୋକାନ • 24x7 Emergency Chemists",
                        fontSize = 12.sp,
                        color = Color(0xFF16A34A),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            IconButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("close_medicine_sheet_btn")
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Daily Auto-Update Pharmacy Status Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
            border = BorderStroke(1.5.dp, Color(0xFFBFDBFE))
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
                            color = Color(0xFF2563EB),
                            modifier = Modifier.size(8.dp)
                        ) {}
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "24x7 EMERGENCY PHARMACY WATCH",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E40AF)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFDBEAFE)
                    ) {
                        Text(
                            text = "${dailyPulse.activePharmacies24x7Count} STORES OPEN 24/7",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1D4ED8),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "🚨 ${dailyPulse.antiVenomStockAdvisory}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1E3A8A)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "📍 Night Shift Duty: ${dailyPulse.medicineStoreEmergencyDuty}",
                    fontSize = 11.sp,
                    color = Color(0xFF2563EB)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "🛵 10 km Radius Locality: Station Road, OT Road, Azimabad, Kuruda, Remuna, Sovarampur, Nayabazar, Chandipur Road & FMMCH.",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1D4ED8)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search chemist, insulin, anti-venom, oxygen...", fontSize = 13.sp) },
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
                .testTag("medicine_search_input"),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF16A34A),
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
                        selectedContainerColor = Color(0xFF16A34A),
                        selectedLabelColor = Color.White,
                        containerColor = Color(0xFFF1F5F9),
                        labelColor = Color(0xFF334155)
                    ),
                    border = null
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Stores List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredStores, key = { it.id }) { store ->
                MedicineStoreCardItem(
                    store = store,
                    context = context
                )
            }

            if (filteredStores.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Medication, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(40.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No medicine stores found for '$searchQuery'", color = Color(0xFF64748B), fontSize = 13.sp)
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
fun MedicineStoreCardItem(
    store: MedicineStore,
    context: Context,
    modifier: Modifier = Modifier
) {
    val isOpenNow = remember { HealthcareRepository.isStoreOpenNow(store) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("medicine_store_card_${store.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Row 1: Name, 24x7 Badge, Jan Aushadhi
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = if (isOpenNow) Color(0xFF22C55E) else Color(0xFFEF4444),
                            modifier = Modifier.size(8.dp)
                        ) {}
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = store.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF0F172A)
                        )
                    }
                    Text(
                        text = store.odiaName,
                        fontSize = 12.sp,
                        color = Color(0xFF16A34A),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "📍 ${store.areaLocation} • ${store.fullAddress}",
                        fontSize = 11.sp,
                        color = Color(0xFF475569)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    if (store.is24x7) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFDCFCE7),
                            border = BorderStroke(1.dp, Color(0xFF86EFAC))
                        ) {
                            Text(
                                text = "24x7",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF166534),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                    if (store.isJanAushadhiGeneric) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFEF3C7),
                            border = BorderStroke(1.dp, Color(0xFFFDE68A))
                        ) {
                            Text(
                                text = "GENERIC",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB45309),
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Timings & Delivery Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = store.openingHours,
                        fontSize = 11.sp,
                        color = Color(0xFF334155),
                        fontWeight = FontWeight.Medium
                    )
                }

                if (store.hasHomeDelivery) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFF0FDF4)
                    ) {
                        Text(
                            text = "🛵 Delivery within ${store.deliveryRadiusKm}km",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF15803D),
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Emergency Medicines available tags
            Text(
                text = "Emergency & Critical Medicines:",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF475569)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                store.emergencyMedicines.take(3).forEach { med ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFF1F5F9)
                    ) {
                        Text(
                            text = med,
                            fontSize = 9.sp,
                            color = Color(0xFF1E293B),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Pharmacist details & Alternate Contact
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "💊 Regd. Pharmacist: ${store.licensedPharmacistName}",
                    fontSize = 10.sp,
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Medium
                )
                if (store.alternatePhone != null) {
                    Text(
                        text = "Alt: ${store.alternatePhone}",
                        fontSize = 10.sp,
                        color = Color(0xFF0284C7),
                        fontWeight = FontWeight.SemiBold
                    )
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
                        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${store.phone}"))
                        context.startActivity(dialIntent)
                    },
                    modifier = Modifier
                        .weight(1.2f)
                        .testTag("call_store_${store.id}"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Call Chemist", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = {
                        openInGoogleMaps(context, store.latitude, store.longitude, store.name)
                    },
                    modifier = Modifier
                        .weight(0.8f)
                        .testTag("map_store_${store.id}"),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color(0xFF16A34A)),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Navigation, contentDescription = null, tint = Color(0xFF16A34A), modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Map", fontSize = 12.sp, color = Color(0xFF16A34A), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
