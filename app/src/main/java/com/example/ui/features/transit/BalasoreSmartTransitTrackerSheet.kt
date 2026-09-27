package com.example.ui.features.transit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.daily.DailyBalasorePulse
import com.example.data.model.AppLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalasoreSmartTransitTrackerSheet(
    language: AppLanguage,
    dailyPulse: DailyBalasorePulse,
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("Bus Departures") }

    val busSchedules = listOf(
        BusTrip("BLS-CRUT-101", "Sahadevkhunta ➔ FM Medical College (Remuna)", "Bay 2", "06:15 AM, Every 15 min", "₹15", "ON TIME 🟢"),
        BusTrip("BLS-EXP-204", "Balasore ➔ Chandipur Sea Beach (DRDO Gate)", "Bay 4", "07:00 AM, Every 30 min", "₹25", "ON TIME 🟢"),
        BusTrip("BLS-EXP-312", "Balasore ➔ Nilagiri Palace & Panchalingeswar", "Bay 6", "07:30 AM, Every 40 min", "₹35", "BOARDING 🟡"),
        BusTrip("BLS-EXP-408", "Balasore ➔ Jaleswar & Bhograi Border", "Bay 1", "06:45 AM, Every 20 min", "₹45", "ON TIME 🟢"),
        BusTrip("BLS-EXP-515", "Balasore ➔ Soro & Bahanaga Bazar", "Bay 5", "07:15 AM, Every 25 min", "₹30", "ON TIME 🟢")
    )

    val autoTariffs = listOf(
        AutoTariff("Railway Station ➔ Sahadevkhunta Bus Stand", "₹40 - ₹50 (Shared ₹15)"),
        AutoTariff("Railway Station ➔ FM MCH (Remuna)", "₹120 - ₹140 (Shared ₹30)"),
        AutoTariff("Railway Station ➔ Chandipur Beach", "₹220 - ₹250 (Full Auto)"),
        AutoTariff("Station Square ➔ Town Hall / Cinema Chhak", "₹30 - ₹40 (Shared ₹10)"),
        AutoTariff("Sahadevkhunta ➔ Remuna Gopinath Temple", "₹100 - ₹120 (Shared ₹25)")
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
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ସ୍ମାର୍ଟ ଯାତ୍ରୀ ଓ ବସ୍ ଟ୍ରାକର୍ 🚌" else "Smart Commuter & Transit Radar 🚌",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ସହଦେବଖୁଣ୍ଟା ବସ୍ ଟର୍ମିନାଲ୍ ସୂଚୀ ଓ ଅଟୋ ଭଡ଼ା ଚାର୍ଟ" else "Sahadevkhunta Bus Terminal Timings & Fixed Auto Fare Card",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            }

            // Transit Terminal Live Status
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("CENTRAL BUS TERMINAL STATUS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Badge(containerColor = Color(0xFF16A34A)) {
                                Text("${dailyPulse.nextBusesCount} BUSES TODAY", color = Color.White, fontSize = 10.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(dailyPulse.busTerminalStatus, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }
                }
            }

            // Tabs Selector
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = selectedTab == "Bus Departures",
                        onClick = { selectedTab = "Bus Departures" },
                        label = { Text("Bus Departures 🚌") }
                    )
                    FilterChip(
                        selected = selectedTab == "Auto Tariff",
                        onClick = { selectedTab = "Auto Tariff" },
                        label = { Text("Fixed Auto Fare Card 🛺") }
                    )
                }
            }

            if (selectedTab == "Bus Departures") {
                items(busSchedules) { bus ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(bus.route, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.weight(1f))
                                Text(bus.fare, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${bus.bay} • Frequency: ${bus.frequency}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(bus.status, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                item {
                    Text(
                        "Official Balasore Auto-Rickshaw Standard Fare Reference (Prevents Tourist Overcharging)",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                items(autoTariffs) { tariff ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(tariff.destination, fontSize = 12.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                            Text(tariff.fare, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
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

private data class BusTrip(
    val busCode: String,
    val route: String,
    val bay: String,
    val frequency: String,
    val fare: String,
    val status: String
)

private data class AutoTariff(
    val destination: String,
    val fare: String
)
