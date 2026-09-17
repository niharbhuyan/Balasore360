package com.example.ui.features.transit

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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

data class TransitRoute(
    val id: String,
    val name: String,
    val odiaName: String,
    val distanceKm: Double,
    val estimatedMinutes: Int
)

enum class VehicleType(val displayName: String, val baseFare: Int, val perKmRate: Int, val isPerSeat: Boolean = false) {
    SHARED_AUTO("Shared Auto (Per Seat)", baseFare = 20, perKmRate = 4, isPerSeat = true),
    RESERVED_AUTO("Reserved Auto (3-4 seats)", baseFare = 50, perKmRate = 12),
    NON_AC_CAB("Non-AC Hatchback Cab", baseFare = 100, perKmRate = 15),
    AC_SEDAN("AC Sedan / SUV", baseFare = 150, perKmRate = 18)
}

/**
 * Standard Auto & Taxi Fare Estimator component.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransitFareEstimatorSheet(
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val routes = remember {
        listOf(
            TransitRoute("r1", "Station ↔ Chandipur Beach", "ଷ୍ଟେସନ ↔ ଚାନ୍ଦିପୁର ବେଳାଭୂମି", distanceKm = 16.0, estimatedMinutes = 30),
            TransitRoute("r2", "Station ↔ Khirachora Remuna", "ଷ୍ଟେସନ ↔ କ୍ଷୀରଚୋରା ରେମୁଣା", distanceKm = 9.0, estimatedMinutes = 20),
            TransitRoute("r3", "Station ↔ Panchalingeswar", "ଷ୍ଟେସନ ↔ ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ନୀଳଗିରି", distanceKm = 30.0, estimatedMinutes = 55),
            TransitRoute("r4", "Station ↔ Talasari Beach", "ଷ୍ଟେସନ ↔ ତାଳସାରୀ ବେଳାଭୂମି", distanceKm = 88.0, estimatedMinutes = 135),
            TransitRoute("r5", "Station ↔ Kuldiha Sanctuary", "ଷ୍ଟେସନ ↔ କୁଲଡିହା ଅଭୟାରଣ୍ୟ", distanceKm = 38.0, estimatedMinutes = 70),
            TransitRoute("r6", "Station ↔ DHH Hospital / Town", "ଷ୍ଟେସନ ↔ ଜିଲ୍ଲା ମୁଖ୍ୟ ଡାକ୍ତରଖାନା", distanceKm = 4.0, estimatedMinutes = 12)
        )
    }

    var selectedRouteIndex by remember { mutableIntStateOf(0) }
    var selectedVehicle by remember { mutableStateOf(VehicleType.RESERVED_AUTO) }
    var isNightTravel by remember { mutableStateOf(false) }
    var hasHeavyLuggage by remember { mutableStateOf(false) }
    var isRouteDropdownExpanded by remember { mutableStateOf(false) }

    val currentRoute = routes[selectedRouteIndex]

    // Calculate fare
    val calculatedFare = remember(currentRoute, selectedVehicle, isNightTravel, hasHeavyLuggage) {
        val base = selectedVehicle.baseFare + (currentRoute.distanceKm * selectedVehicle.perKmRate).toInt()
        val nightMultiplier = if (isNightTravel) 1.25 else 1.0
        val luggageCharge = if (hasHeavyLuggage && !selectedVehicle.isPerSeat) 30 else 0
        (base * nightMultiplier).toInt() + luggageCharge
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("transit_fare_estimator_sheet")
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
                                listOf(Color(0xFF2563EB), Color(0xFF1D4ED8))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Auto & Taxi Fare Estimator",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "ଅଟୋ ଓ ଟ୍ୟାକ୍ସି ସଠିକ ଭଡ଼ା ଗଣନା",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF2563EB),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFDBEAFE)
            ) {
                Text(
                    text = "RTO Rates",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E40AF)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Route Selector Dropdown
        Text(
            text = "Select Route",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(6.dp))

        ExposedDropdownMenuBox(
            expanded = isRouteDropdownExpanded,
            onExpandedChange = { isRouteDropdownExpanded = !isRouteDropdownExpanded }
        ) {
            OutlinedTextField(
                value = "${currentRoute.name} (${currentRoute.distanceKm} km)",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isRouteDropdownExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                shape = RoundedCornerShape(12.dp)
            )

            ExposedDropdownMenu(
                expanded = isRouteDropdownExpanded,
                onDismissRequest = { isRouteDropdownExpanded = false }
            ) {
                routes.forEachIndexed { index, route ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(route.name, fontWeight = FontWeight.Bold)
                                Text("${route.odiaName} • ${route.distanceKm} km", fontSize = 11.sp, color = Color(0xFF64748B))
                            }
                        },
                        onClick = {
                            selectedRouteIndex = index
                            isRouteDropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Vehicle Type Chips
        Text(
            text = "Select Vehicle Type",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(6.dp))

        VehicleType.values().forEach { vehicle ->
            Surface(
                onClick = { selectedVehicle = vehicle },
                shape = RoundedCornerShape(10.dp),
                color = if (selectedVehicle == vehicle) Color(0xFFEFF6FF) else MaterialTheme.colorScheme.surface,
                border = BorderStroke(
                    1.dp,
                    if (selectedVehicle == vehicle) Color(0xFF2563EB) else MaterialTheme.colorScheme.outlineVariant
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (vehicle.isPerSeat) Icons.Default.TwoWheeler else Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = if (selectedVehicle == vehicle) Color(0xFF2563EB) else Color(0xFF64748B),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = vehicle.displayName,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (selectedVehicle == vehicle) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedVehicle == vehicle) Color(0xFF1E40AF) else MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }

                    Text(
                        text = "₹${vehicle.baseFare} base",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Options: Night travel and Luggage
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Nightlight, contentDescription = null, tint = Color(0xFF6366F1), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Night Surcharge (10 PM - 5 AM)", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                            Text("+25% per standard RTO guidelines", fontSize = 10.sp, color = Color(0xFF64748B))
                        }
                    }

                    Switch(
                        checked = isNightTravel,
                        onCheckedChange = { isNightTravel = it },
                        colors = SwitchDefaults.colors(checkedTrackColor = Color(0xFF2563EB))
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Luggage, contentDescription = null, tint = Color(0xFF0D9488), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Heavy Luggage (3+ bags)", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                            Text("+₹30 flat handling charge", fontSize = 10.sp, color = Color(0xFF64748B))
                        }
                    }

                    Switch(
                        checked = hasHeavyLuggage,
                        onCheckedChange = { hasHeavyLuggage = it },
                        colors = SwitchDefaults.colors(checkedTrackColor = Color(0xFF2563EB))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Main Result Estimate Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
            border = BorderStroke(1.5.dp, Color(0xFF86EFAC)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ESTIMATED FARE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF15803D),
                        letterSpacing = 1.sp
                    )
                )

                Text(
                    text = "₹$calculatedFare - ₹${calculatedFare + 30}",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF166534)
                    )
                )

                Text(
                    text = if (selectedVehicle.isPerSeat) "Price per passenger seat" else "Total vehicle booking rate",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF15803D),
                        fontSize = 11.sp
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Distance", fontSize = 10.sp, color = Color(0xFF64748B))
                        Text("${currentRoute.distanceKm} km", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Travel Time", fontSize = 10.sp, color = Color(0xFF64748B))
                        Text("~${currentRoute.estimatedMinutes} mins", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Rate / km", fontSize = 10.sp, color = Color(0xFF64748B))
                        Text("₹${selectedVehicle.perKmRate}/km", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Grievance / Helpline Button
        OutlinedButton(
            onClick = {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:18003456770") // Odisha RTO Passenger Grievance
                }
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(Icons.Default.Call, null, modifier = Modifier.size(14.dp), tint = Color(0xFF2563EB))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Odisha RTO Passenger Helpline (1800-345-6770)", fontSize = 11.sp, color = Color(0xFF2563EB))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Done with Fare Calculator", fontWeight = FontWeight.Bold)
        }
    }
}
