package com.example.ui.features.civic

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoBalasoreCivicStormReporterSheet(
    language: AppLanguage,
    dailyPulse: DailyBalasorePulse,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    var selectedCategory by remember { mutableStateOf("Waterlogging & Drains 🌊") }
    var selectedWard by remember { mutableStateOf("Balasore Municipality Ward 14 (Station Bazar)") }
    var landmarkInput by remember { mutableStateOf("") }
    var descriptionInput by remember { mutableStateOf("") }
    var attachedPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var isSubmittedSuccess by remember { mutableStateOf(false) }

    // Photo picker launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        attachedPhotoUri = uri
    }

    // Local in-memory list of recent user filed complaints
    val userReports = remember {
        mutableStateListOf(
            CivicReportItem(
                trackingToken = "BLS-2026-0842",
                category = "Snapped Electric Line ⚡",
                ward = "Ward 7 (Motiganj)",
                landmark = "Near Cinema Chhak Transformer",
                timestamp = "Today 08:30 AM",
                status = "Action Dispatched 🔵 (TPNODL Line Crew)",
                isResolved = false
            ),
            CivicReportItem(
                trackingToken = "BLS-2026-0839",
                category = "Fallen Tree & Blocked Road 🌳",
                ward = "Remuna High School Road",
                landmark = "Near Gopinath Temple Gate",
                timestamp = "Yesterday",
                status = "Resolved 🟢 (Cleared by Fire Station)",
                isResolved = true
            )
        )
    }

    val categories = listOf(
        "Waterlogging & Drains 🌊",
        "Fallen Tree & Blocked Road 🌳",
        "Snapped Electric Wires ⚡",
        "Road Potholes & Embankments 🕳️",
        "Uncollected Garbage & Health 🗑️"
    )

    val wards = listOf(
        "Balasore Municipality Ward 14 (Station Bazar)",
        "Balasore Municipality Ward 7 (Motiganj)",
        "Balasore Municipality Ward 21 (Sahadevkhunta)",
        "Balasore Municipality Ward 4 (FM Goltei / Town Hall)",
        "Remuna NAC (Gopinath Area)",
        "Soro Municipality (Main Market)",
        "Jaleswar Municipality (Station Road)",
        "Nilagiri NAC (Palace Chhak)",
        "Bhograi Coastal Sector"
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
                            text = if (language == AppLanguage.ODIA) "ମୋ ବାଲେଶ୍ୱର ନାଗରିକ ଓ ବାତ୍ୟା କ୍ଷତି ରିପୋର୍ଟର୍ 📸" else "Mo Balasore Civic & Storm Reporter 📸",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଜଳବନ୍ଦୀ, ଭଙ୍ଗା ରାସ୍ତା, ବିଦ୍ୟୁତ୍ ତାର ଓ ଗଛ ଭାଙ୍ଗିବା ସମସ୍ୟାର ସିଧାସଳଖ ସମାଧାନ" else "Report Waterlogging, Fallen Trees, Power Snaps with Photo & Auto-Sync",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            }

            // Civic Telemetry Pulse
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Pending Grievances", style = MaterialTheme.typography.labelSmall)
                            Text(
                                "${dailyPulse.pendingCivicReportsCount} Active",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text("Ward Patrols Dispatched", fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Resolved Today", style = MaterialTheme.typography.labelSmall)
                            Text(
                                "${dailyPulse.resolvedCivicReportsTodayCount} Cleared",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF16A34A)
                            )
                            Text("Quick Response Team", fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                        }
                    }
                }
            }

            // Success Message Banner
            if (isSubmittedSuccess) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                        border = BorderStroke(1.dp, Color(0xFF86EFAC)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF16A34A))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Grievance Successfully Registered!", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF14532D))
                                Text("Tracking Token: ${userReports.firstOrNull()?.trackingToken ?: "BLS-2026-0843"}. Queued for auto-dispatch.", fontSize = 11.sp, color = Color(0xFF166534))
                            }
                        }
                    }
                }
            }

            // Grievance Form
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ନୂତନ ଅଭିଯୋଗ ଦାୟର କରନ୍ତୁ" else "File New Civic / Storm Damage Incident",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )

                        // Category Dropdown / Selector
                        Text("Incident Category", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            categories.take(2).forEach { cat ->
                                FilterChip(
                                    selected = selectedCategory == cat,
                                    onClick = { selectedCategory = cat },
                                    label = { Text(cat.split(" ").first(), fontSize = 11.sp) }
                                )
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            categories.drop(2).forEach { cat ->
                                FilterChip(
                                    selected = selectedCategory == cat,
                                    onClick = { selectedCategory = cat },
                                    label = { Text(cat.split(" ").first(), fontSize = 11.sp) }
                                )
                            }
                        }

                        // Ward Selector
                        OutlinedTextField(
                            value = selectedWard,
                            onValueChange = { selectedWard = it },
                            label = { Text("Ward / Block") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Landmark
                        OutlinedTextField(
                            value = landmarkInput,
                            onValueChange = { landmarkInput = it },
                            label = { Text("Exact Landmark / Road Name") },
                            placeholder = { Text("e.g. Near Station Square, opposite BSNL office") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Description
                        OutlinedTextField(
                            value = descriptionInput,
                            onValueChange = { descriptionInput = it },
                            label = { Text("Details of Damage / Urgency") },
                            minLines = 2,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Photo Attachment & Geo Tag
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedButton(
                                onClick = { photoPickerLauncher.launch("image/*") }
                            ) {
                                Icon(Icons.Default.PhotoCamera, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (attachedPhotoUri != null) "Photo Attached ✅" else "Attach Photo")
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("GPS Tagged: 21.49°N, 86.91°E", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                            }
                        }

                        // Submit Button
                        Button(
                            onClick = {
                                if (landmarkInput.isNotBlank() || descriptionInput.isNotBlank()) {
                                    val nowFormat = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
                                    val randomTokenNum = (1000..9999).random()
                                    userReports.add(
                                        0,
                                        CivicReportItem(
                                            trackingToken = "BLS-2026-$randomTokenNum",
                                            category = selectedCategory,
                                            ward = selectedWard,
                                            landmark = landmarkInput.ifBlank { "Balasore City Center" },
                                            timestamp = "Today $nowFormat",
                                            status = "Queued for Auto-Sync 🟡",
                                            isResolved = false
                                        )
                                    )
                                    landmarkInput = ""
                                    descriptionInput = ""
                                    attachedPhotoUri = null
                                    isSubmittedSuccess = true
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Submit Incident (Auto-Syncs Offline)")
                        }
                    }
                }
            }

            // Direct Helpline Actions
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+916782262002"))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Municipality SOS", fontSize = 11.sp)
                    }

                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/919437155200?text=Balasore%20Civic%20Grievance"))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("WhatsApp Helpline 💬", fontSize = 11.sp)
                    }
                }
            }

            // Recent Grievance Tracker
            item {
                Text(
                    text = if (language == AppLanguage.ODIA) "ଆପଣଙ୍କ ଦାୟର ଅଭିଯୋଗ ଟ୍ରାକର୍" else "My Filed Incident Reports",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            items(userReports) { report ->
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
                            Text(report.trackingToken, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                            Badge(containerColor = if (report.isResolved) Color(0xFF16A34A) else Color(0xFF0284C7)) {
                                Text(if (report.isResolved) "RESOLVED" else "IN PROGRESS", color = Color.White, fontSize = 9.sp)
                            }
                        }

                        Text(report.category, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Text("${report.ward} • ${report.landmark}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(report.timestamp, fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                            Text(report.status, fontSize = 11.sp, fontWeight = FontWeight.Medium)
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

private data class CivicReportItem(
    val trackingToken: String,
    val category: String,
    val ward: String,
    val landmark: String,
    val timestamp: String,
    val status: String,
    val isResolved: Boolean
)
