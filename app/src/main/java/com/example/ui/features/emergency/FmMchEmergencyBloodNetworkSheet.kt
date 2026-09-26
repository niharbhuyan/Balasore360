package com.example.ui.features.emergency

import android.content.Context
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
fun FmMchEmergencyBloodNetworkSheet(
    language: AppLanguage,
    dailyPulse: DailyBalasorePulse,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedGroupFilter by remember { mutableStateOf("All") }
    var selectedBlockFilter by remember { mutableStateOf("All") }
    var showBroadcastForm by remember { mutableStateOf(false) }

    // Patient Family SOS Beacon Form fields
    var patientName by remember { mutableStateOf("") }
    var hospitalWard by remember { mutableStateOf("FM MCH Trauma ICU") }
    var reqBloodGroup by remember { mutableStateOf("O-") }
    var reqUnits by remember { mutableStateOf("2") }
    var contactPhone by remember { mutableStateOf("") }
    var isBroadcastActive by remember { mutableStateOf(false) }

    val bloodInventory = remember(dailyPulse) {
        listOf(
            StockItem("A+", 28, false),
            StockItem("B+", 42, false),
            StockItem("O+", dailyPulse.fmmchBloodInventoryOposUnits, false),
            StockItem("AB+", 18, false),
            StockItem("A-", 6, false),
            StockItem("B-", 7, false),
            StockItem("O-", 3, true), // Critical
            StockItem("AB-", 2, true) // Critical
        )
    }

    val donors = remember {
        listOf(
            VolunteerDonor("Soumya Ranjan Mohapatra", "O+", "Balasore Town (Station Bazar)", "+919437100201", "24 days ago", true),
            VolunteerDonor("Debasis Panda", "A+", "Remuna Block", "+919861200302", "45 days ago", true),
            VolunteerDonor("Tushar Kanta Giri", "B+", "Soro Sub-division", "+919777300403", "12 days ago", true),
            VolunteerDonor("Ashutosh Behera", "AB+", "Jaleswar Town", "+919438400504", "60 days ago", true),
            VolunteerDonor("Prakash Chandra Das", "O-", "Nilagiri NAC", "+919853500605", "30 days ago", true),
            VolunteerDonor("Manoj Kumar Sethi", "A-", "Basta Block", "+919439600706", "18 days ago", true),
            VolunteerDonor("Alok Narayan Nayak", "B-", "Balasore Sadar (Motiganj)", "+919778700807", "40 days ago", true),
            VolunteerDonor("Subhransu Shekhar Jena", "AB-", "Bhograi Border", "+919938800908", "55 days ago", true)
        )
    }

    val filteredDonors = remember(donors, selectedGroupFilter, selectedBlockFilter) {
        donors.filter { donor ->
            val matchGroup = selectedGroupFilter == "All" || donor.bloodGroup == selectedGroupFilter
            val matchBlock = selectedBlockFilter == "All" || donor.block.contains(selectedBlockFilter, ignoreCase = true)
            matchGroup && matchBlock
        }
    }

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
                            text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ଜରୁରୀ ରକ୍ତଦାତା ନେଟୱାର୍କ 🩸" else "Emergency Blood Network & FM MCH SOS 🩸",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFDC2626)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଫକୀର ମୋହନ ମେଡିକାଲ୍ କଲେଜ୍ ରକ୍ତ ଭଣ୍ଡାର ଓ ଲାଇଭ୍ ଦାତା ସୂଚୀ" else "FM Medical College Blood Bank Stock & 24x7 Donors Roster",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            }

            // Hospital SOS Quick Dial Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF450A0A)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("FM MCH CASUALTY & TRAUMA SOS", color = Color(0xFFFCA5A5), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Badge(containerColor = Color(0xFFDC2626)) {
                                Text("24x7 ACTIVE", color = Color.White, fontSize = 10.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Fakir Mohan Medical College & Hospital",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Critical Shortage: ${dailyPulse.fmmchCriticalBloodShortageGroup}",
                            color = Color(0xFFF87171),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+916782262057"))
                                    context.startActivity(dialIntent)
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                            ) {
                                Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Blood Bank")
                            }

                            Button(
                                onClick = {
                                    val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:108"))
                                    context.startActivity(dialIntent)
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF991B1B))
                            ) {
                                Icon(Icons.Default.LocalHospital, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("108 Ambulance")
                            }
                        }
                    }
                }
            }

            // Live Component Stock Grid
            item {
                Text(
                    text = if (language == AppLanguage.ODIA) "ରକ୍ତ ଭଣ୍ଡାର ବର୍ତ୍ତମାନ ମହଜୁଦ ସ୍ଥିତି" else "Central Blood Bank Live Inventory",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    bloodInventory.take(4).forEach { item ->
                        StockBadge(item, modifier = Modifier.weight(1f))
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    bloodInventory.drop(4).forEach { item ->
                        StockBadge(item, modifier = Modifier.weight(1f))
                    }
                }
            }

            // Active Emergency Broadcast Beacon (If initiated)
            if (isBroadcastActive) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                        border = BorderStroke(2.dp, Color(0xFFDC2626)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🚨", fontSize = 18.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("ACTIVE SOS BLOOD BEACON", color = Color(0xFFDC2626), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                                TextButton(onClick = { isBroadcastActive = false }) {
                                    Text("Dismiss", color = Color(0xFF991B1B), fontSize = 11.sp)
                                }
                            }
                            Text(
                                "Patient: $patientName | Need: $reqUnits Units of $reqBloodGroup",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color(0xFF7F1D1D)
                            )
                            Text("Hospital: $hospitalWard", fontSize = 12.sp, color = Color(0xFF991B1B))
                            Spacer(modifier = Modifier.height(6.dp))
                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$contactPhone"))
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                            ) {
                                Text("Call Attendant: $contactPhone")
                            }
                        }
                    }
                }
            }

            // Broadcast SOS Button / Form Toggle
            item {
                OutlinedButton(
                    onClick = { showBroadcastForm = !showBroadcastForm },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                    border = BorderStroke(1.dp, Color(0xFFDC2626))
                ) {
                    Icon(Icons.Default.Campaign, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (showBroadcastForm) "Close SOS Form" else "Broadcast Urgent Blood Requirement 📣")
                }
            }

            // Broadcast Form
            if (showBroadcastForm) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("Post Emergency Blood Beacon", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            OutlinedTextField(
                                value = patientName,
                                onValueChange = { patientName = it },
                                label = { Text("Patient Name") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = hospitalWard,
                                onValueChange = { hospitalWard = it },
                                label = { Text("Hospital & Ward (e.g. FM MCH ICU)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = reqBloodGroup,
                                    onValueChange = { reqBloodGroup = it },
                                    label = { Text("Blood Group") },
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = reqUnits,
                                    onValueChange = { reqUnits = it },
                                    label = { Text("Units Needed") },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            OutlinedTextField(
                                value = contactPhone,
                                onValueChange = { contactPhone = it },
                                label = { Text("Bystander Phone Number") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Button(
                                onClick = {
                                    if (patientName.isNotBlank() && contactPhone.isNotBlank()) {
                                        isBroadcastActive = true
                                        showBroadcastForm = false
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                            ) {
                                Text("Publish SOS Beacon Instantly")
                            }
                        }
                    }
                }
            }

            // Donors Filter Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == AppLanguage.ODIA) "ସ୍ୱେଚ୍ଛାସେବୀ ରକ୍ତଦାତା ସୂଚୀ" else "Verified Voluntary Donors",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text("${filteredDonors.size} available", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                }
            }

            // Blood Group Filter Chips
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf("All", "O+", "A+", "B+", "AB+", "O-").forEach { group ->
                        FilterChip(
                            selected = selectedGroupFilter == group,
                            onClick = { selectedGroupFilter = group },
                            label = { Text(group, fontSize = 11.sp) }
                        )
                    }
                }
            }

            // Donors List
            items(filteredDonors) { donor ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFEE2E2)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(donor.bloodGroup, color = Color(0xFFDC2626), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(donor.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(donor.block, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("Last donated: ${donor.lastDonated}", fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                            }
                        }

                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${donor.phone}"))
                                context.startActivity(intent)
                            },
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Call", fontSize = 11.sp)
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

private data class StockItem(
    val group: String,
    val units: Int,
    val isCritical: Boolean
)

private data class VolunteerDonor(
    val name: String,
    val bloodGroup: String,
    val block: String,
    val phone: String,
    val lastDonated: String,
    val isVerified: Boolean
)

@Composable
private fun StockBadge(item: StockItem, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (item.isCritical) Color(0xFFFEE2E2) else Color(0xFFF0FDF4)
        ),
        border = BorderStroke(1.dp, if (item.isCritical) Color(0xFFF87171) else Color(0xFF86EFAC)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(item.group, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (item.isCritical) Color(0xFFDC2626) else Color(0xFF16A34A))
            Text("${item.units} u", fontSize = 11.sp, color = Color(0xFF334155))
        }
    }
}
