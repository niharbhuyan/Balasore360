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
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Traffic
import androidx.compose.material.icons.filled.WaterDamage
import com.example.data.local.CivicReportEntity
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.example.data.model.AppLanguage
import com.example.ui.theme.BentoBlueLight
import com.example.ui.theme.BentoCardWhite
import com.example.ui.theme.BentoPrimaryBlue
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate200
import com.example.ui.theme.BentoSlate400
import com.example.ui.theme.BentoSlate600
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate900
import kotlin.random.Random

data class CivicIssue(
    val id: String,
    val title: String,
    val odiaTitle: String,
    val category: String,
    val location: String,
    val odiaLocation: String,
    val description: String,
    val status: String, // "SUBMITTED", "IN_PROGRESS", "RESOLVED"
    val assignedDepartment: String,
    val reportedTime: String,
    val upvotes: Int,
    val isUpvotedByUser: Boolean = false,
    val trackingId: String
)

data class CivicCategory(
    val id: String,
    val nameEn: String,
    val nameOd: String,
    val emoji: String,
    val icon: ImageVector,
    val color: Color
)

data class CivicQuadColor(
    val bg: Color,
    val fg: Color,
    val label: String,
    val icon: ImageVector
)

@Composable
fun CitizenCivicEyeSheet(
    language: AppLanguage,
    myReports: List<CivicReportEntity> = emptyList(),
    onSubmitReport: ((title: String, category: String, wardLocation: String, description: String, urgency: String, hasPhotoAttached: Boolean) -> Unit)? = null,
    onAdvanceStatus: ((String) -> Unit)? = null,
    onDeleteReport: ((String) -> Unit)? = null,
    onAutoProgress: (() -> Unit)? = null,
    autoUpdateMinutes: Int = 60,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var activeSection by remember { mutableStateOf("COMMUNITY") } // "COMMUNITY" or "MY_REPORTS"
    var myReportsFilter by remember { mutableStateOf("ALL") } // "ALL", "PENDING", "IN_PROGRESS", "RESOLVED"

    val categories = remember {
        listOf(
            CivicCategory("POTHOLE", "Road Pothole", "ରାସ୍ତା ଖାଲଖମା", "🕳️", Icons.Default.Traffic, Color(0xFFEA580C)),
            CivicCategory("STREETLIGHT", "Faulty Streetlight", "ଅଚଳ ଷ୍ଟ୍ରିଟ୍ ଲାଇଟ୍", "💡", Icons.Default.FlashlightOn, Color(0xFFD97706)),
            CivicCategory("DRAINAGE", "Drain Clog / Waterlogging", "ଡ୍ରେନେଜ୍ ଓ ଜଳବନ୍ଦୀ", "🌊", Icons.Default.WaterDamage, Color(0xFF0284C7)),
            CivicCategory("GARBAGE", "Garbage Heap / Waste", "ଆବର୍ଜନା ସଫେଇ", "🗑️", Icons.Default.DeleteSweep, Color(0xFF16A34A)),
            CivicCategory("WATER_LEAK", "Drinking Water Leak", "ପାନୀୟ ଜଳ ପାଇପ୍ ଲିକ୍", "🚰", Icons.Default.Opacity, Color(0xFF2563EB))
        )
    }

    val wardsAndLocations = remember {
        listOf(
            "Ward 1 - Azimabad", "Ward 4 - Sahadevkhunta", "Ward 7 - Motiganj",
            "Ward 12 - Cinema Chhak", "Ward 14 - OT Road / Phandi Chhak",
            "Ward 18 - Station Square", "Ward 22 - Gopalgaon", "Ward 26 - Balisahi",
            "Remuna Block", "Soro Municipality", "Jaleswar Town", "Nilagiri NAC"
        )
    }

    // Community Civic Issues Feed
    val civicIssues = remember {
        mutableStateListOf(
            CivicIssue(
                id = "civic_1",
                title = "Deep pothole cluster near Phandi Chhak on OT Road",
                odiaTitle = "ଓଟି ରୋଡ୍ ଫାଣ୍ଡି ଛକ ନିକଟରେ ବିପଜ୍ଜନକ ରାସ୍ତା ଖାଲ",
                category = "Road Pothole",
                location = "OT Road, Phandi Chhak",
                odiaLocation = "ଓଟି ରୋଡ୍, ଫାଣ୍ଡି ଛକ",
                description = "Vehicles and two-wheelers risk skidding during rain. Urgent bitumen filling needed.",
                status = "IN_PROGRESS",
                assignedDepartment = "PWD & Balasore Municipality Road Div",
                reportedTime = "2 hours ago",
                upvotes = 34,
                trackingId = "BLS-CIVIC-2026-4821"
            ),
            CivicIssue(
                id = "civic_2",
                title = "Solid waste accumulation near Nuabazar Sabzi Mandi",
                odiaTitle = "ନୂଆବଜାର ପନିପରିବା ମାର୍କେଟ ପାଖରେ ଆବର୍ଜନା ଜମାଟ",
                category = "Garbage Heap / Waste",
                location = "Nuabazar Market, Ward 8",
                odiaLocation = "ନୂଆବଜାର ମାର୍କେଟ, ୱାର୍ଡ ୮",
                description = "Vegetable waste spillover blocking pedestrian passage. Municipal dumper required.",
                status = "IN_PROGRESS",
                assignedDepartment = "BMC Sanitation Wing",
                reportedTime = "4 hours ago",
                upvotes = 19,
                trackingId = "BLS-CIVIC-2026-3914"
            ),
            CivicIssue(
                id = "civic_3",
                title = "Solar streetlights not turning on along FM University Road",
                odiaTitle = "ଫକୀର ମୋହନ ବିଶ୍ୱବିଦ୍ୟାଳୟ ରାସ୍ତାରେ ଷ୍ଟ୍ରିଟ୍ ଲାଇଟ୍ ଅଚଳ",
                category = "Faulty Streetlight",
                location = "FM University Approach Road",
                odiaLocation = "ଏଫଏମ ୟୁନିଭରସିଟି ଆପ୍ରୋଚ୍ ରୋଡ୍",
                description = "Dark stretch at night causing safety concern for evening student commuters.",
                status = "RESOLVED",
                assignedDepartment = "TPNODL & Balasore Municipal Electrical Div",
                reportedTime = "Yesterday",
                upvotes = 52,
                trackingId = "BLS-CIVIC-2026-2105"
            ),
            CivicIssue(
                id = "civic_4",
                title = "Storm drain choked with plastic bags near Cinema Chhak",
                odiaTitle = "ସିନେମା ଛକ ନିକଟରେ ପ୍ଲାଷ୍ଟିକ ଜମି ଡ୍ରେନ୍ ବନ୍ଦ",
                category = "Drain Clog / Waterlogging",
                location = "Cinema Chhak, Ward 12",
                odiaLocation = "ସିନେମା ଛକ, ୱାର୍ଡ ୧୨",
                description = "Rainwater overflowing onto merchant storefronts during downpours.",
                status = "IN_PROGRESS",
                assignedDepartment = "Drainage Desiltation Taskforce",
                reportedTime = "5 hours ago",
                upvotes = 28,
                trackingId = "BLS-CIVIC-2026-5129"
            )
        )
    }

    // Form State
    var showReportForm by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(categories.first()) }
    var selectedLocation by remember { mutableStateOf(wardsAndLocations[4]) }
    var issueTitle by remember { mutableStateOf("") }
    var issueDetails by remember { mutableStateOf("") }
    var hasPhotoAttached by remember { mutableStateOf(false) }
    var urgencyLevel by remember { mutableStateOf("NORMAL") } // NORMAL, HIGH, CRITICAL
    var filterStatus by remember { mutableStateOf("ALL") } // ALL, ACTIVE, RESOLVED

    var lastSubmittedTrackingId by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag("citizen_civic_eye_sheet"),
        color = BentoCardWhite
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Header
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
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF0284C7)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("📸", fontSize = 22.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = if (language == AppLanguage.ODIA) "ଆମ ବାଲେଶ୍ୱର: ସ୍ୱଚ୍ଛତା ଓ ଅଭିଯୋଗ" else "Citizen Civic Eye",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.White
                                        )
                                    )
                                    Text(
                                        text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ପୌରପାଳିକା ସିଧାସଳଖ ନାଗରିକ ସେବା" else "Clean Balasore Grievance & Ward Network",
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

                        // Quick Help Line Bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF1E293B))
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ପୌରପାଳିକା ହେଲ୍ପଲାଇନ (୨୪x୭)" else "Balasore Municipal Control Room",
                                    fontSize = 11.sp,
                                    color = Color(0xFF94A3B8)
                                )
                                Text(
                                    text = "06782-262002 / WhatsApp 1929",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF38BDF8)
                                )
                            }

                            Button(
                                onClick = {
                                    val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                                        data = Uri.parse("tel:06782262002")
                                    }
                                    context.startActivity(dialIntent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Call", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Section Switcher: Community Feed vs My Reports (Room DB)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(BentoSlate100)
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (activeSection == "COMMUNITY") Color.White else Color.Transparent)
                            .clickable { activeSection = "COMMUNITY" }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ନାଗରିକ ଫିଡ୍" else "Community Feed",
                            fontSize = 12.sp,
                            fontWeight = if (activeSection == "COMMUNITY") FontWeight.Bold else FontWeight.Medium,
                            color = if (activeSection == "COMMUNITY") BentoPrimaryBlue else BentoSlate600
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (activeSection == "MY_REPORTS") Color.White else Color.Transparent)
                            .clickable { activeSection = "MY_REPORTS" }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (language == AppLanguage.ODIA) "ମୋ ଅଭିଯୋଗ (${myReports.size})" else "My Reports (${myReports.size})",
                                fontSize = 12.sp,
                                fontWeight = if (activeSection == "MY_REPORTS") FontWeight.Bold else FontWeight.Medium,
                                color = if (activeSection == "MY_REPORTS") BentoPrimaryBlue else BentoSlate600
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFF10B981).copy(alpha = 0.2f))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text("ROOM", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF059669))
                            }
                        }
                    }
                }
            }

            // Quick Submission Action Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                    border = BorderStroke(1.dp, Color(0xFFBFDBFE))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ସମସ୍ୟା ଦେଖିଲେ ଫଟୋ ତୋଳନ୍ତୁ ଓ ଜଣାନ୍ତୁ" else "Spot a Civic Issue? Report in 60s",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = BentoPrimaryBlue
                                    )
                                )
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ରାସ୍ତା ଖାଲ, ଆବର୍ଜନା, ଡ୍ରେନେଜ୍ ଓ ଲାଇଟ୍ ସମସ୍ୟା" else "Potholes, drain blocks, garbage dumps, dark streetlights",
                                    fontSize = 12.sp,
                                    color = BentoSlate600
                                )
                            }

                            Button(
                                onClick = { showReportForm = !showReportForm },
                                colors = ButtonDefaults.buttonColors(containerColor = BentoPrimaryBlue),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = if (showReportForm) (if (language == AppLanguage.ODIA) "ବନ୍ଦ କରନ୍ତୁ" else "Close Form") else (if (language == AppLanguage.ODIA) "+ ଅଭିଯୋଗ" else "+ Report Issue"),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Last Submitted Tracking Banner
                        if (lastSubmittedTrackingId != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFDCFCE7))
                                    .border(BorderStroke(1.dp, Color(0xFF86EFAC)), RoundedCornerShape(8.dp))
                                    .padding(10.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF16A34A), modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = if (language == AppLanguage.ODIA) "ଅଭିଯୋଗ ସଫଳତାର ସହ ଦାଖଲ ହୋଇଛି!" else "Civic Grievance Logged Successfully!",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF15803D)
                                        )
                                        Text(
                                            text = "Tracking ID: $lastSubmittedTrackingId",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF166534)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Interactive Report Form
            if (showReportForm) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, BentoSlate200)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (language == AppLanguage.ODIA) "ନୂତନ ସମସ୍ୟା ଦାଖଲ ଫର୍ମ" else "Report Civic Problem to Balasore Municipal Ward",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            // Step 1: Category Picker
                            Text(
                                text = if (language == AppLanguage.ODIA) "୧. ସମସ୍ୟାର ପ୍ରକାର ବାଛନ୍ତୁ:" else "1. Select Category:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = BentoSlate700
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(categories) { cat ->
                                    val isSelected = cat == selectedCategory
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { selectedCategory = cat },
                                        leadingIcon = { Text(cat.emoji, fontSize = 12.sp) },
                                        label = {
                                            Text(
                                                text = if (language == AppLanguage.ODIA) cat.nameOd else cat.nameEn,
                                                fontSize = 11.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
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

                            Spacer(modifier = Modifier.height(14.dp))

                            // Step 2: Location / Ward Picker
                            Text(
                                text = if (language == AppLanguage.ODIA) "୨. ୱାର୍ଡ / ଛକ ସ୍ଥାନ:" else "2. Balasore Ward / Location:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = BentoSlate700
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(wardsAndLocations) { loc ->
                                    val isSelected = loc == selectedLocation
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) BentoPrimaryBlue else BentoSlate100)
                                            .clickable { selectedLocation = loc }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = loc,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) Color.White else BentoSlate700
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Step 3: Title and Details
                            OutlinedTextField(
                                value = issueTitle,
                                onValueChange = { issueTitle = it },
                                placeholder = {
                                    Text(
                                        text = if (language == AppLanguage.ODIA) "ଉଦାହରଣ: ଷ୍ଟେସନ ଛକ ନିକଟରେ ବଡ଼ ରାସ୍ତା ଖାଲ" else "e.g., Deep road pothole near Sahadevkhunta",
                                        fontSize = 12.sp
                                    )
                                },
                                label = { Text(if (language == AppLanguage.ODIA) "ସମସ୍ୟାର ସଂକ୍ଷିପ୍ତ ଶିରୋନାମା" else "Issue Title", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = issueDetails,
                                onValueChange = { issueDetails = it },
                                placeholder = {
                                    Text(
                                        text = if (language == AppLanguage.ODIA) "ନିର୍ଦ୍ଦିଷ୍ଟ ଲ୍ୟାଣ୍ଡମାର୍କ ଓ ବିବରଣୀ ଲେଖନ୍ତୁ..." else "Add specific landmark or description...",
                                        fontSize = 12.sp
                                    )
                                },
                                label = { Text(if (language == AppLanguage.ODIA) "ଲ୍ୟାଣ୍ଡମାର୍କ ଓ ବିବରଣୀ" else "Landmark & Description", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2,
                                maxLines = 4,
                                shape = RoundedCornerShape(10.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Photo Attach and Urgency
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (hasPhotoAttached) Color(0xFFDCFCE7) else BentoSlate100)
                                        .clickable {
                                            hasPhotoAttached = !hasPhotoAttached
                                            Toast.makeText(
                                                context,
                                                if (hasPhotoAttached) "Photo attached: [IMG_BLS_CIVIC.jpg]" else "Photo removed",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CameraAlt,
                                        contentDescription = null,
                                        tint = if (hasPhotoAttached) Color(0xFF16A34A) else BentoSlate600,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (hasPhotoAttached) "Photo Tagged ✓" else "+ Attach Photo",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (hasPhotoAttached) Color(0xFF16A34A) else BentoSlate700
                                    )
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    listOf("NORMAL", "CRITICAL").forEach { urg ->
                                        val isSel = urgencyLevel == urg
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(
                                                    if (isSel) {
                                                        if (urg == "CRITICAL") Color(0xFFDC2626) else BentoPrimaryBlue
                                                    } else BentoSlate100
                                                )
                                                .clickable { urgencyLevel = urg }
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = if (urg == "CRITICAL") "⚠️ Urgent" else "Normal",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSel) Color.White else BentoSlate700
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Submit Button
                            Button(
                                onClick = {
                                    if (issueTitle.isBlank()) {
                                        Toast.makeText(context, "Please enter an issue title", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                    val randomIdNum = Random.nextInt(1000, 9999)
                                    val newTrackId = "BLS-CIVIC-2026-$randomIdNum"

                                    val newIssue = CivicIssue(
                                        id = "civic_${System.currentTimeMillis()}",
                                        title = issueTitle,
                                        odiaTitle = issueTitle,
                                        category = selectedCategory.nameEn,
                                        location = selectedLocation,
                                        odiaLocation = selectedLocation,
                                        description = if (issueDetails.isNotBlank()) issueDetails else "Reported by citizen via Balasore 360",
                                        status = "SUBMITTED",
                                        assignedDepartment = "Ward Sanitary & Municipal Taskforce",
                                        reportedTime = "Just now",
                                        upvotes = 1,
                                        trackingId = newTrackId
                                    )
                                    onSubmitReport?.invoke(
                                        issueTitle,
                                        selectedCategory.nameEn,
                                        selectedLocation,
                                        if (issueDetails.isNotBlank()) issueDetails else "Reported by citizen via Balasore 360",
                                        urgencyLevel,
                                        hasPhotoAttached
                                    )
                                    civicIssues.add(0, newIssue)
                                    lastSubmittedTrackingId = newTrackId
                                    issueTitle = ""
                                    issueDetails = ""
                                    showReportForm = false
                                    activeSection = "MY_REPORTS"
                                    Toast.makeText(context, "Logged grievance $newTrackId with Balasore Municipality (Room DB)", Toast.LENGTH_LONG).show()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = BentoPrimaryBlue),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(imageVector = Icons.Default.AddAlert, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଅଭିଯୋଗ ଦାଖଲ କରନ୍ତୁ" else "Submit Grievance to Municipality",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            if (activeSection == "MY_REPORTS") {
                // My Reports Section (Offline Room Database Tracking)
                item {
                    val myPending = myReports.count { it.status == "PENDING" }
                    val myProgress = myReports.count { it.status == "IN_PROGRESS" }
                    val myResolved = myReports.count { it.status == "RESOLVED" }

                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        // Telemetry Sync Banner
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF1F5F9))
                                .border(BorderStroke(1.dp, BentoSlate200), RoundedCornerShape(10.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF10B981))
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (language == AppLanguage.ODIA) "ଲୋକାଲ୍ Room DB ସକ୍ରିୟ" else "Local Room DB Active",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A)
                                    )
                                }
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ସ୍ୱୟଂକ୍ରିୟ ସିଙ୍କ୍ ପ୍ରତି $autoUpdateMinutes ମିନିଟରେ" else "Auto-updates active every ${autoUpdateMinutes}m",
                                    fontSize = 10.sp,
                                    color = Color(0xFF64748B)
                                )
                            }

                            Button(
                                onClick = {
                                    onAutoProgress?.invoke()
                                    Toast.makeText(context, "Synced status with Balasore Municipality", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = BentoPrimaryBlue),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (language == AppLanguage.ODIA) "ସିଙ୍କ୍" else "Sync", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Filter Chips for My Reports
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (language == AppLanguage.ODIA) "ମୋ ଅଭିଯୋଗ ସ୍ଥିତି:" else "Track Status:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = BentoSlate900
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                listOf(
                                    "ALL" to (if (language == AppLanguage.ODIA) "ସମସ୍ତ (${myReports.size})" else "All (${myReports.size})"),
                                    "PENDING" to (if (language == AppLanguage.ODIA) "ବକେୟା ($myPending)" else "Pending ($myPending)"),
                                    "IN_PROGRESS" to (if (language == AppLanguage.ODIA) "ଚାଲୁ ($myProgress)" else "Active ($myProgress)"),
                                    "RESOLVED" to (if (language == AppLanguage.ODIA) "ସମାହିତ ($myResolved)" else "Resolved ($myResolved)")
                                ).forEach { (key, label) ->
                                    val isSel = myReportsFilter == key
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (isSel) BentoPrimaryBlue else BentoSlate100)
                                            .clickable { myReportsFilter = key }
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
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
                }

                val myFilteredReports = myReports.filter { report ->
                    when (myReportsFilter) {
                        "PENDING" -> report.status == "PENDING"
                        "IN_PROGRESS" -> report.status == "IN_PROGRESS"
                        "RESOLVED" -> report.status == "RESOLVED"
                        else -> true
                    }
                }

                if (myFilteredReports.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("📋", fontSize = 36.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଏହି ସ୍ଥିତିରେ କୌଣସି ଅଭିଯୋଗ ନାହିଁ" else "No Reports in this Status Category",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BentoSlate700
                                )
                            }
                        }
                    }
                } else {
                    items(myFilteredReports, key = { it.id }) { report ->
                        MyCivicReportItemRow(
                            report = report,
                            language = language,
                            onAdvanceStatus = { onAdvanceStatus?.invoke(report.id) },
                            onDelete = { onDeleteReport?.invoke(report.id) },
                            onShare = {
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "Balasore Civic Issue [${report.id}]: ${report.title} at ${report.wardLocation}. Status: ${report.status}. Notes: ${report.resolutionNotes}. Track on Balasore 360 app."
                                    )
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, null))
                            }
                        )
                    }
                }
            } else {
                // Community Civic Issues Feed
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ସାମ୍ପ୍ରତିକ ନାଗରିକ ଅଭିଯୋଗ ଟ୍ରାକର୍" else "Active Community Civic Tracker",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = BentoSlate900)
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            listOf("ALL" to "All", "ACTIVE" to "Active", "RESOLVED" to "Resolved").forEach { (key, label) ->
                                val isSel = filterStatus == key
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isSel) BentoPrimaryBlue else BentoSlate100)
                                        .clickable { filterStatus = key }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
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

                // Civic Issues List
                val filteredIssues = civicIssues.filter { issue ->
                    when (filterStatus) {
                        "ACTIVE" -> issue.status != "RESOLVED"
                        "RESOLVED" -> issue.status == "RESOLVED"
                        else -> true
                    }
                }

                items(filteredIssues, key = { it.id }) { issue ->
                CivicIssueCard(
                    issue = issue,
                    language = language,
                    onUpvote = {
                        val index = civicIssues.indexOfFirst { it.id == issue.id }
                        if (index != -1) {
                            val current = civicIssues[index]
                            civicIssues[index] = current.copy(
                                upvotes = if (current.isUpvotedByUser) current.upvotes - 1 else current.upvotes + 1,
                                isUpvotedByUser = !current.isUpvotedByUser
                            )
                        }
                    },
                    onShare = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Balasore Civic Issue [${issue.trackingId}]: ${issue.title} at ${issue.location}. Status: ${issue.status}. Track on Balasore 360 app."
                            )
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, null))
                    }
                )
            }
        }
    }
}
}

@Composable
fun CivicIssueCard(
    issue: CivicIssue,
    language: AppLanguage,
    onUpvote: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, BentoSlate100)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Top Row: Category + Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFEFF6FF))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = issue.category,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BentoPrimaryBlue
                    )
                }

                val (statusBg, statusFg, statusLabel) = when (issue.status) {
                    "RESOLVED" -> Triple(Color(0xFFDCFCE7), Color(0xFF15803D), "RESOLVED")
                    "IN_PROGRESS" -> Triple(Color(0xFFFEF3C7), Color(0xFFB45309), "IN PROGRESS")
                    else -> Triple(Color(0xFFF1F5F9), Color(0xFF475569), "SUBMITTED")
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusBg)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = statusLabel,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = statusFg
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = if (language == AppLanguage.ODIA) issue.odiaTitle else issue.title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = BentoSlate900,
                    lineHeight = 20.sp
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Location
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = BentoSlate400,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = if (language == AppLanguage.ODIA) issue.odiaLocation else issue.location,
                    fontSize = 11.sp,
                    color = BentoSlate600,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "• ${issue.reportedTime}",
                    fontSize = 11.sp,
                    color = BentoSlate400
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Description
            Text(
                text = issue.description,
                style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate700, lineHeight = 18.sp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Department Assignment & Tracking ID
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF8FAFC))
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Assigned: ${issue.assignedDepartment}",
                        fontSize = 10.sp,
                        color = BentoSlate600,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = issue.trackingId,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = BentoPrimaryBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action row: Upvote / Endorse & Share
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (issue.isUpvotedByUser) Color(0xFFEFF6FF) else BentoSlate100)
                        .clickable { onUpvote() }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = "Upvote",
                        tint = if (issue.isUpvotedByUser) BentoPrimaryBlue else BentoSlate600,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${issue.upvotes} Citizens Agree",
                        fontSize = 11.sp,
                        fontWeight = if (issue.isUpvotedByUser) FontWeight.Bold else FontWeight.Medium,
                        color = if (issue.isUpvotedByUser) BentoPrimaryBlue else BentoSlate700
                    )
                }

                IconButton(
                    onClick = onShare,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share Issue",
                        tint = BentoSlate400,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MyCivicReportItemRow(
    report: CivicReportEntity,
    language: AppLanguage,
    onAdvanceStatus: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (statusBg, statusFg, statusLabel, statusIcon) = when (report.status) {
        "RESOLVED" -> CivicQuadColor(Color(0xFFDCFCE7), Color(0xFF15803D), if (language == AppLanguage.ODIA) "ସମାହିତ" else "RESOLVED", Icons.Default.CheckCircle)
        "IN_PROGRESS" -> CivicQuadColor(Color(0xFFDBEAFE), Color(0xFF1D4ED8), if (language == AppLanguage.ODIA) "ଚାଲୁଅଛି" else "IN PROGRESS", Icons.Default.Build)
        else -> CivicQuadColor(Color(0xFFFEF3C7), Color(0xFFB45309), if (language == AppLanguage.ODIA) "ବକେୟା" else "PENDING", Icons.Default.HourglassTop)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, BentoSlate200)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Category & Status Badge
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

            // Stepper
            val currentStep = when (report.status) {
                "RESOLVED" -> 3
                "IN_PROGRESS" -> 2
                else -> 1
            }

            val steps = listOf(
                if (language == AppLanguage.ODIA) "୧. ଦାଖଲ (Room DB)" else "1. Logged",
                if (language == AppLanguage.ODIA) "୨. ଯାଞ୍ଚ ଚାଲୁ" else "2. In Progress",
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
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (report.status != "RESOLVED") {
                    OutlinedButton(
                        onClick = onAdvanceStatus,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = BentoPrimaryBlue),
                        border = BorderStroke(1.dp, BentoPrimaryBlue),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (report.status == "PENDING") "Advance Status" else "Mark Resolved",
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
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444), modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

