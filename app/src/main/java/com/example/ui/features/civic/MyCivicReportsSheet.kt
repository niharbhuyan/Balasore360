package com.example.ui.features.civic

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Traffic
import androidx.compose.material.icons.filled.WaterDamage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.CivicReportEntity
import com.example.data.model.AppLanguage
import com.example.ui.theme.BentoCardWhite
import com.example.ui.theme.BentoPrimaryBlue
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate200
import com.example.ui.theme.BentoSlate600
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate900
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 'My Reports' Section:
 * Tracks citizen reported civic issues (Pending, In-Progress, Resolved)
 * using local Room Database for 100% offline tracking, status progression,
 * and automated municipal updates.
 */
@Composable
fun MyCivicReportsSheet(
    language: AppLanguage,
    myReports: List<CivicReportEntity>,
    selectedFilter: String,
    onFilterChange: (String) -> Unit,
    onSubmitReport: (title: String, category: String, wardLocation: String, description: String, urgency: String, hasPhotoAttached: Boolean) -> Unit,
    onAdvanceStatus: (String) -> Unit,
    onDeleteReport: (String) -> Unit,
    onAutoProgress: () -> Unit,
    autoUpdateMinutes: Int,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showNewReportForm by remember { mutableStateOf(false) }

    val categories = remember {
        listOf(
            CivicFormCategory("Road Pothole", "ରାସ୍ତା ଖାଲଖମା", "🕳️", Icons.Default.Traffic, Color(0xFFEA580C)),
            CivicFormCategory("Faulty Streetlight", "ଅଚଳ ଷ୍ଟ୍ରିଟ୍ ଲାଇଟ୍", "💡", Icons.Default.FlashlightOn, Color(0xFFD97706)),
            CivicFormCategory("Drain Clog / Waterlogging", "ଡ୍ରେନେଜ୍ ଓ ଜଳବନ୍ଦୀ", "🌊", Icons.Default.WaterDamage, Color(0xFF0284C7)),
            CivicFormCategory("Garbage Heap / Waste", "ଆବର୍ଜନା ସଫେଇ", "🗑️", Icons.Default.DeleteSweep, Color(0xFF16A34A)),
            CivicFormCategory("Drinking Water Leak", "ପାନୀୟ ଜଳ ପାଇପ୍ ଲିକ୍", "🚰", Icons.Default.Opacity, Color(0xFF2563EB))
        )
    }

    val wards = remember {
        listOf(
            "Ward 14 - OT Road / Phandi Chhak",
            "Ward 4 - Sahadevkhunta",
            "Ward 1 - Azimabad",
            "Ward 7 - Motiganj",
            "Ward 12 - Cinema Chhak",
            "Ward 18 - Station Square",
            "Ward 22 - Gopalgaon",
            "Remuna NAC (Gopinath Area)",
            "Soro Municipality",
            "Jaleswar Town",
            "Nilagiri NAC"
        )
    }

    // Form inputs
    var formCategory by remember { mutableStateOf(categories.first()) }
    var formWard by remember { mutableStateOf(wards.first()) }
    var formTitle by remember { mutableStateOf("") }
    var formDetails by remember { mutableStateOf("") }
    var formUrgency by remember { mutableStateOf("NORMAL") }
    var formPhotoAttached by remember { mutableStateOf(false) }

    val totalCount = myReports.size
    val pendingCount = myReports.count { it.status == "PENDING" }
    val inProgressCount = myReports.count { it.status == "IN_PROGRESS" }
    val resolvedCount = myReports.count { it.status == "RESOLVED" }

    val filteredList = remember(myReports, selectedFilter) {
        when (selectedFilter) {
            "PENDING" -> myReports.filter { it.status == "PENDING" }
            "IN_PROGRESS" -> myReports.filter { it.status == "IN_PROGRESS" }
            "RESOLVED" -> myReports.filter { it.status == "RESOLVED" }
            else -> myReports
        }
    }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag("my_civic_reports_sheet"),
        color = BentoCardWhite
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {
            // Header Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0F172A))
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Column {
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
                                        .background(Color(0xFF2563EB)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Storage,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = if (language == AppLanguage.ODIA) "ମୋ ଅଭିଯୋଗ ଟ୍ରାକର୍" else "My Civic Reports",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.ExtraBold,
                                                color = Color.White
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(Color(0xFF10B981).copy(alpha = 0.25f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "ROOM DB",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = Color(0xFF34D399)
                                            )
                                        }
                                    }
                                    Text(
                                        text = if (language == AppLanguage.ODIA) "ଅଫଲାଇନ୍ ଲୋକାଲ୍ ଡାଟାବେସ୍ ଓ ସ୍ୱୟଂକ୍ରିୟ ମ୍ୟୁନିସିପାଲ୍ ଅପଡେଟ୍" else "Offline Room Tracking • Balasore Municipal Sync",
                                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF94A3B8))
                                    )
                                }
                            }

                            IconButton(
                                onClick = onClose,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.15f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Close",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Auto Updates Telemetry Bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF1E293B))
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF34D399))
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = if (language == AppLanguage.ODIA) "ସ୍ୱୟଂକ୍ରିୟ ଅପଡେଟ୍ ସକ୍ରିୟ (${autoUpdateMinutes} ମିନିଟ୍ ଚକ୍ର)" else "Auto-Updates Active (${autoUpdateMinutes}m Sync Cycle)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF38BDF8)
                                    )
                                    Text(
                                        text = if (language == AppLanguage.ODIA) "ନାଗରିକ ଅଭିଯୋଗ ଗୁଡ଼ିକ ଲୋକାଲ୍ Room DB ରେ ସୁରକ୍ଷିତ" else "Reports stored offline locally with status progression",
                                        fontSize = 10.sp,
                                        color = Color(0xFF94A3B8)
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    onAutoProgress()
                                    Toast.makeText(context, "Synced Room DB with Ward Grievance Dispatcher", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("auto_sync_btn")
                            ) {
                                Icon(imageVector = Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ସିଙ୍କ୍" else "Sync",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Status KPI Summary Cards
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    KpiStatCard(
                        count = totalCount,
                        label = if (language == AppLanguage.ODIA) "ମୋଟ୍" else "Total",
                        color = Color(0xFF475569),
                        bgColor = Color(0xFFF1F5F9),
                        modifier = Modifier.weight(1f)
                    )
                    KpiStatCard(
                        count = pendingCount,
                        label = if (language == AppLanguage.ODIA) "ବକେୟା" else "Pending",
                        color = Color(0xFFD97706),
                        bgColor = Color(0xFFFEF3C7),
                        modifier = Modifier.weight(1f)
                    )
                    KpiStatCard(
                        count = inProgressCount,
                        label = if (language == AppLanguage.ODIA) "ଚାଲୁଅଛି" else "In Progress",
                        color = Color(0xFF2563EB),
                        bgColor = Color(0xFFDBEAFE),
                        modifier = Modifier.weight(1f)
                    )
                    KpiStatCard(
                        count = resolvedCount,
                        label = if (language == AppLanguage.ODIA) "ସମାହିତ" else "Resolved",
                        color = Color(0xFF16A34A),
                        bgColor = Color(0xFFDCFCE7),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Quick Action: + Report New Issue
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    border = BorderStroke(1.dp, BentoSlate200)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ନୂତନ ସମସ୍ୟା ଦାଖଲ କରନ୍ତୁ" else "File New Civic Issue (Room DB)",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BentoSlate900
                                )
                                Text(
                                    text = if (language == AppLanguage.ODIA) "୧୦୦% ଅଫଲାଇନ୍ ଟ୍ରାକିଂ ସହ ଲୋକାଲ୍ ଡାଟାବେସରେ ସେଭ୍ ହେବ" else "Saved locally in SQLite Room DB; auto-advances as municipal crews act",
                                    fontSize = 11.sp,
                                    color = BentoSlate600
                                )
                            }
                            Button(
                                onClick = { showNewReportForm = !showNewReportForm },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (showNewReportForm) Color(0xFF64748B) else BentoPrimaryBlue
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("submit_new_report_btn")
                            ) {
                                Icon(
                                    imageVector = if (showNewReportForm) Icons.Default.Clear else Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (showNewReportForm) (if (language == AppLanguage.ODIA) "ବନ୍ଦ" else "Cancel") else (if (language == AppLanguage.ODIA) "+ ଅଭିଯୋଗ" else "+ Report"),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Form Fields
                        AnimatedVisibility(visible = showNewReportForm) {
                            Column(modifier = Modifier.padding(top = 14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ସମସ୍ୟାର ବର୍ଗ ଚୟନ କରନ୍ତୁ:" else "1. Select Category:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = BentoSlate700
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    items(categories) { cat ->
                                        val isSel = cat == formCategory
                                        FilterChip(
                                            selected = isSel,
                                            onClick = { formCategory = cat },
                                            leadingIcon = { Text(cat.emoji, fontSize = 12.sp) },
                                            label = {
                                                Text(
                                                    text = if (language == AppLanguage.ODIA) cat.nameOd else cat.nameEn,
                                                    fontSize = 11.sp,
                                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                                                )
                                            },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = cat.color,
                                                selectedLabelColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = if (language == AppLanguage.ODIA) "ୱାର୍ଡ / ସ୍ଥାନ:" else "2. Ward Location:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = BentoSlate700
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    items(wards) { ward ->
                                        val isSel = ward == formWard
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (isSel) BentoPrimaryBlue else BentoSlate100)
                                                .clickable { formWard = ward }
                                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = ward,
                                                fontSize = 11.sp,
                                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSel) Color.White else BentoSlate700
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedTextField(
                                    value = formTitle,
                                    onValueChange = { formTitle = it },
                                    label = { Text(if (language == AppLanguage.ODIA) "ସମସ୍ୟାର ସଂକ୍ଷିପ୍ତ ଶିରୋନାମା" else "Issue Title", fontSize = 12.sp) },
                                    placeholder = { Text("e.g. Broken water pipe leaking near main road", fontSize = 12.sp) },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    shape = RoundedCornerShape(10.dp)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = formDetails,
                                    onValueChange = { formDetails = it },
                                    label = { Text(if (language == AppLanguage.ODIA) "ବିବରଣୀ ଓ ଲ୍ୟାଣ୍ଡମାର୍କ" else "Details & Landmark", fontSize = 12.sp) },
                                    placeholder = { Text("Specify landmark, pole number or street corner...", fontSize = 12.sp) },
                                    modifier = Modifier.fillMaxWidth(),
                                    minLines = 2,
                                    maxLines = 3,
                                    shape = RoundedCornerShape(10.dp)
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (formPhotoAttached) Color(0xFFDCFCE7) else BentoSlate100)
                                            .clickable {
                                                formPhotoAttached = !formPhotoAttached
                                                Toast.makeText(
                                                    context,
                                                    if (formPhotoAttached) "Photo attached to Room record ✓" else "Photo detached",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                            .padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CameraAlt,
                                            contentDescription = null,
                                            tint = if (formPhotoAttached) Color(0xFF16A34A) else BentoSlate600,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (formPhotoAttached) "Photo Tagged ✓" else "+ Attach Photo",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (formPhotoAttached) Color(0xFF16A34A) else BentoSlate700
                                        )
                                    }

                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        listOf("NORMAL" to "Normal", "HIGH" to "High", "CRITICAL" to "⚠️ Critical").forEach { (urg, label) ->
                                            val isSel = formUrgency == urg
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(
                                                        if (isSel) {
                                                            when (urg) {
                                                                "CRITICAL" -> Color(0xFFDC2626)
                                                                "HIGH" -> Color(0xFFEA580C)
                                                                else -> BentoPrimaryBlue
                                                            }
                                                        } else BentoSlate100
                                                    )
                                                    .clickable { formUrgency = urg }
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = label,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isSel) Color.White else BentoSlate700
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Button(
                                    onClick = {
                                        if (formTitle.isBlank()) {
                                            Toast.makeText(context, "Please enter an issue title", Toast.LENGTH_SHORT).show()
                                            return@Button
                                        }
                                        onSubmitReport(
                                            formTitle,
                                            formCategory.nameEn,
                                            formWard,
                                            formDetails,
                                            formUrgency,
                                            formPhotoAttached
                                        )
                                        formTitle = ""
                                        formDetails = ""
                                        showNewReportForm = false
                                        Toast.makeText(context, "Report persisted to local Room Database!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = BentoPrimaryBlue),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Storage, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (language == AppLanguage.ODIA) "ଅଫଲାଇନ୍ Room DB ରେ ସେଭ୍ କରନ୍ତୁ" else "Save Report to Local Room Database",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Status Filter Tabs
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == AppLanguage.ODIA) "ଅଭିଯୋଗ ସ୍ଥିତି ଫିଲ୍ଟର" else "Filter by Status",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate900
                        )
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf(
                            "ALL" to (if (language == AppLanguage.ODIA) "ସମସ୍ତ ($totalCount)" else "All ($totalCount)"),
                            "PENDING" to (if (language == AppLanguage.ODIA) "ବକେୟା ($pendingCount)" else "Pending ($pendingCount)"),
                            "IN_PROGRESS" to (if (language == AppLanguage.ODIA) "ଚାଲୁ ($inProgressCount)" else "Active ($inProgressCount)"),
                            "RESOLVED" to (if (language == AppLanguage.ODIA) "ସମାହିତ ($resolvedCount)" else "Resolved ($resolvedCount)")
                        ).forEach { (key, label) ->
                            val isSel = selectedFilter == key
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSel) BentoPrimaryBlue else BentoSlate100)
                                    .clickable { onFilterChange(key) }
                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                                    .testTag("filter_chip_${key.lowercase()}")
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSel) Color.White else BentoSlate700
                                )
                            }
                        }
                    }
                }
            }

            // Reports List or Empty State
            if (filteredList.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 20.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = BorderStroke(1.dp, BentoSlate200)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("📋", fontSize = 42.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (language == AppLanguage.ODIA) "ଏହି ବର୍ଗରେ କୌଣସି ଅଭିଯୋଗ ନାହିଁ" else "No Reports in this Status Category",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = BentoSlate700
                            )
                            Text(
                                text = if (language == AppLanguage.ODIA) "ନୂଆ ଅଭିଯୋଗ ଦାଖଲ କରିବା ପାଇଁ ଉପର ବଟନ୍ ଦବାନ୍ତୁ।" else "Tap '+ Report' above to log an issue into the offline Room Database.",
                                fontSize = 12.sp,
                                color = BentoSlate600,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            } else {
                items(filteredList, key = { it.id }) { report ->
                    MyCivicReportItemCard(
                        report = report,
                        language = language,
                        onAdvanceStatus = { onAdvanceStatus(report.id) },
                        onDeleteReport = { onDeleteReport(report.id) },
                        onShare = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Balasore Civic Issue Report [${report.id}]:\nTitle: ${report.title}\nCategory: ${report.category}\nWard: ${report.wardLocation}\nStatus: ${report.status}\nDepartment: ${report.assignedDepartment}\nNotes: ${report.resolutionNotes}\nTracked via Balasore 360 App."
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Civic Issue Report"))
                        },
                        onDialHelpline = {
                            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:06782262002")
                            }
                            context.startActivity(dialIntent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun KpiStatCard(
    count: Int,
    label: String,
    color: Color,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.dp, color.copy(alpha = 0.25f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = color.copy(alpha = 0.85f),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun MyCivicReportItemCard(
    report: CivicReportEntity,
    language: AppLanguage,
    onAdvanceStatus: () -> Unit,
    onDeleteReport: () -> Unit,
    onShare: () -> Unit,
    onDialHelpline: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (statusBg, statusFg, statusLabel, statusIcon) = when (report.status) {
        "RESOLVED" -> Quad(Color(0xFFDCFCE7), Color(0xFF15803D), if (language == AppLanguage.ODIA) "ସମାହିତ" else "RESOLVED", Icons.Default.CheckCircle)
        "IN_PROGRESS" -> Quad(Color(0xFFDBEAFE), Color(0xFF1D4ED8), if (language == AppLanguage.ODIA) "ଚାଲୁଅଛି" else "IN PROGRESS", Icons.Default.Build)
        else -> Quad(Color(0xFFFEF3C7), Color(0xFFB45309), if (language == AppLanguage.ODIA) "ବକେୟା" else "PENDING", Icons.Default.HourglassTop)
    }

    val reportedFormatted = remember(report.reportedTimestamp) {
        val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        sdf.format(Date(report.reportedTimestamp))
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("report_card_item"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, BentoSlate200)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Top Row: Category + Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFEFF6FF))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = report.category,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BentoPrimaryBlue
                        )
                    }

                    if (report.urgency == "CRITICAL" || report.urgency == "HIGH") {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFFEE2E2))
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "⚠️ ${report.urgency}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB91C1C)
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusBg)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = statusIcon, contentDescription = null, tint = statusFg, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = statusLabel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = statusFg
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = report.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = BentoSlate900,
                lineHeight = 20.sp
            )

            // Location
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = report.wardLocation,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF64748B)
                )
                if (report.hasPhotoAttached) {
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, tint = Color(0xFF16A34A), modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "Photo Attached",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF16A34A)
                    )
                }
            }

            // Description
            if (report.description.isNotBlank()) {
                Text(
                    text = report.description,
                    fontSize = 12.sp,
                    color = BentoSlate700,
                    modifier = Modifier.padding(top = 6.dp),
                    lineHeight = 17.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Stepper / Lifecycle Indicator
            StatusLifecycleStepper(status = report.status, language = language)

            Spacer(modifier = Modifier.height(10.dp))

            // Assigned Department & Resolution Notes Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(BorderStroke(1.dp, BentoSlate100), RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Dept: ${report.assignedDepartment}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF475569)
                        )
                        Text(
                            text = report.id,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = BentoPrimaryBlue
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = report.resolutionNotes,
                        fontSize = 11.sp,
                        color = Color(0xFF334155),
                        lineHeight = 16.sp
                    )
                    Text(
                        text = "Reported: $reportedFormatted",
                        fontSize = 9.sp,
                        color = Color(0xFF94A3B8),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Advance / Simulate municipal progress button
                if (report.status != "RESOLVED") {
                    OutlinedButton(
                        onClick = onAdvanceStatus,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = BentoPrimaryBlue),
                        border = BorderStroke(1.dp, BentoPrimaryBlue),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("advance_status_btn")
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (report.status == "PENDING") "Advance to In-Progress" else "Mark Resolved",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AssignmentTurnedIn, contentDescription = null, tint = Color(0xFF16A34A), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.ODIA) "କାର୍ଯ୍ୟ ସମାପ୍ତ ✓" else "Closed in Registry ✓",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF16A34A)
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = onShare,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = Color(0xFF64748B), modifier = Modifier.size(16.dp))
                    }
                    IconButton(
                        onClick = onDialHelpline,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Call, contentDescription = "Call Ward", tint = Color(0xFF0284C7), modifier = Modifier.size(16.dp))
                    }
                    IconButton(
                        onClick = onDeleteReport,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444), modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusLifecycleStepper(
    status: String,
    language: AppLanguage
) {
    val currentStep = when (status) {
        "RESOLVED" -> 3
        "IN_PROGRESS" -> 2
        else -> 1
    }

    val steps = listOf(
        if (language == AppLanguage.ODIA) "୧. ଦାଖଲ (Room DB)" else "1. Logged",
        if (language == AppLanguage.ODIA) "୨. କ୍ଷେତ୍ର ଯାଞ୍ଚ" else "2. In Progress",
        if (language == AppLanguage.ODIA) "୩. ସମାଧାନ" else "3. Resolved"
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        steps.forEachIndexed { index, title ->
            val stepNumber = index + 1
            val isDone = stepNumber <= currentStep
            val stepColor = if (isDone) {
                if (stepNumber == 3) Color(0xFF16A34A) else BentoPrimaryBlue
            } else Color(0xFFCBD5E1)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(stepColor),
                    contentAlignment = Alignment.Center
                ) {
                    if (isDone) {
                        Text("✓", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    } else {
                        Text(stepNumber.toString(), fontSize = 9.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = title,
                    fontSize = 10.sp,
                    fontWeight = if (isDone) FontWeight.Bold else FontWeight.Normal,
                    color = if (isDone) BentoSlate900 else Color(0xFF94A3B8)
                )
            }

            if (index < steps.size - 1) {
                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                        .background(if (stepNumber < currentStep) BentoPrimaryBlue else Color(0xFFE2E8F0))
                )
            }
        }
    }
}

private data class CivicFormCategory(
    val nameEn: String,
    val nameOd: String,
    val emoji: String,
    val icon: ImageVector,
    val color: Color
)

private data class Quad<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)
