package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BentoAmberBg
import com.example.ui.theme.BentoAmberText
import com.example.ui.theme.BentoBlueLight
import com.example.ui.theme.BentoBluePill
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoCardWhite
import com.example.ui.theme.BentoGreenBg
import com.example.ui.theme.BentoGreenText
import com.example.ui.theme.BentoPrimaryBlue
import com.example.ui.theme.BentoRedBg
import com.example.ui.theme.BentoRedText
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate400
import com.example.ui.theme.BentoSlate500
import com.example.ui.theme.BentoSlate700
import com.example.ui.components.BalasoreSocialHubCard
import com.example.ui.theme.BentoSlate900

enum class EmergencyCategory(val label: String, val odiaLabel: String) {
    ALL("All Contacts", "ସମସ୍ତ"),
    HOSPITALS("Hospitals", "ଡାକ୍ତରଖାନା"),
    POLICE("Police", "ପୋଲିସ"),
    FIRE_RESCUE("Fire & Rescue", "ଅଗ୍ନିଶମ ଓ ବିପର୍ଯ୍ୟୟ")
}

data class EmergencyContact(
    val id: String,
    val name: String,
    val odiaName: String,
    val phone: String,
    val tollFree: String? = null,
    val role: String,
    val location: String,
    val category: EmergencyCategory,
    val icon: ImageVector,
    val availableHours: String = "24x7 Emergency"
)

data class SosSpeedDialItem(
    val id: String,
    val number: String,
    val label: String,
    val subtitle: String,
    val icon: ImageVector,
    val bgColor: Color,
    val textColor: Color
)

fun dialPhoneNumber(context: Context, phoneNumber: String) {
    try {
        val cleanNumber = phoneNumber.replace(Regex("[^0-9+]"), "")
        val dialIntent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$cleanNumber")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(dialIntent)
    } catch (_: Exception) {
        // Graceful fallback
    }
}

@Composable
fun EssentialsScreen(
    weatherAlertsEnabled: Boolean = true,
    breakingNewsEnabled: Boolean = true,
    onToggleWeatherAlerts: (Boolean) -> Unit = {},
    onToggleBreakingNews: (Boolean) -> Unit = {},
    onOpenAlertCenter: () -> Unit = {},
    onSimulateWeatherAlert: () -> Unit = {},
    onSimulateBreakingNews: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf(EmergencyCategory.ALL) }
    var searchQuery by remember { mutableStateOf("") }

    // High Priority Instant SOS Speed-Dial numbers
    val sosActions = listOf(
        SosSpeedDialItem(
            id = "sos_112",
            number = "112",
            label = "National SOS",
            subtitle = "Police • Fire • Med",
            icon = Icons.Default.Emergency,
            bgColor = BentoRedBg,
            textColor = BentoRedText
        ),
        SosSpeedDialItem(
            id = "sos_108",
            number = "108",
            label = "Ambulance",
            subtitle = "Medical Emergency",
            icon = Icons.Default.LocalHospital,
            bgColor = BentoGreenBg,
            textColor = BentoGreenText
        ),
        SosSpeedDialItem(
            id = "sos_101",
            number = "101",
            label = "Fire Control",
            subtitle = "Disaster & Rescue",
            icon = Icons.Default.LocalFireDepartment,
            bgColor = BentoAmberBg,
            textColor = BentoAmberText
        ),
        SosSpeedDialItem(
            id = "sos_1077",
            number = "1077",
            label = "DEOC Balasore",
            subtitle = "Disaster Relief",
            icon = Icons.Default.PhoneInTalk,
            bgColor = BentoBlueLight,
            textColor = BentoPrimaryBlue
        )
    )

    // Complete Directory of Balasore District Essential Services
    val allContacts = remember {
        listOf(
            // === HOSPITALS & HEALTHCARE ===
            EmergencyContact(
                id = "fm_mch",
                name = "FM Medical College & Hospital",
                odiaName = "ଫକୀର ମୋହନ ମେଡିକାଲ କଲେଜ ଓ ହସ୍ପିଟାଲ",
                phone = "06782-255010",
                tollFree = "108",
                role = "Casualty & Trauma Centre",
                location = "Remuna, Balasore",
                category = EmergencyCategory.HOSPITALS,
                icon = Icons.Default.LocalHospital,
                availableHours = "24x7 Casualty & Emergency"
            ),
            EmergencyContact(
                id = "dhh_balasore",
                name = "District Headquarters Hospital (DHH)",
                odiaName = "ଜିଲ୍ଲା ମୁଖ୍ୟ ଚିକିତ୍ସାଳୟ, ବାଲେଶ୍ୱର",
                phone = "06782-262016",
                role = "District Trauma & Emergency Unit",
                location = "Hospital Road, Balasore Town",
                category = EmergencyCategory.HOSPITALS,
                icon = Icons.Default.LocalHospital,
                availableHours = "24x7 Emergency Services"
            ),
            EmergencyContact(
                id = "red_cross_blood",
                name = "Red Cross Central Blood Bank",
                odiaName = "ରେଡକ୍ରସ କେନ୍ଦ୍ରୀୟ ରକ୍ତ ଭଣ୍ଡାର",
                phone = "06782-262035",
                role = "Emergency Blood & Component Bank",
                location = "DHH Campus, Balasore",
                category = EmergencyCategory.HOSPITALS,
                icon = Icons.Default.LocalHospital,
                availableHours = "24x7 Blood Availability"
            ),
            EmergencyContact(
                id = "sdh_nilagiri",
                name = "Sub-Divisional Hospital (SDH) Nilagiri",
                odiaName = "ଉପଖଣ୍ଡ ଡାକ୍ତରଖାନା, ନୀଳଗିରି",
                phone = "06782-233224",
                role = "Sub-Divisional Emergency & Maternity",
                location = "Nilagiri Town",
                category = EmergencyCategory.HOSPITALS,
                icon = Icons.Default.LocalHospital,
                availableHours = "24x7 Emergency"
            ),
            EmergencyContact(
                id = "chc_chandipur",
                name = "Chandipur Coastal Primary Health Centre",
                odiaName = "ଚାନ୍ଦିପୁର ଉପକୂଳ ସ୍ୱାସ୍ଥ୍ୟ କେନ୍ଦ୍ର",
                phone = "06782-272212",
                role = "Coastal Emergency & Beach First Aid",
                location = "Near Chandipur Beach",
                category = EmergencyCategory.HOSPITALS,
                icon = Icons.Default.LocalHospital,
                availableHours = "24x7 First Response"
            ),

            // === POLICE & LAW ENFORCEMENT ===
            EmergencyContact(
                id = "sp_balasore",
                name = "Superintendent of Police (SP) Balasore",
                odiaName = "ଆରକ୍ଷୀ ଅଧୀକ୍ଷକ କାର୍ଯ୍ୟାଳୟ, ବାଲେଶ୍ୱର",
                phone = "06782-262024",
                tollFree = "112",
                role = "District Police Headquarters & Control",
                location = "Collectorate Road, Balasore",
                category = EmergencyCategory.POLICE,
                icon = Icons.Default.LocalPolice,
                availableHours = "24x7 Command Room"
            ),
            EmergencyContact(
                id = "town_ps",
                name = "Balasore Town Police Station",
                odiaName = "ବାଲେଶ୍ୱର ଟାଉନ ଥାନା",
                phone = "06782-262032",
                tollFree = "112",
                role = "Town Law & Order Patrol",
                location = "Cinema Chhak, Balasore",
                category = EmergencyCategory.POLICE,
                icon = Icons.Default.LocalPolice,
                availableHours = "24x7 Active Duty"
            ),
            EmergencyContact(
                id = "sahadevkhunta_ps",
                name = "Sahadevkhunta Police Station",
                odiaName = "ସହଦେବଖୁଣ୍ଟା ଥାନା",
                phone = "06782-262054",
                tollFree = "112",
                role = "Central Station & Bus Terminal Sector",
                location = "Station Road, Sahadevkhunta",
                category = EmergencyCategory.POLICE,
                icon = Icons.Default.LocalPolice,
                availableHours = "24x7 Active Duty"
            ),
            EmergencyContact(
                id = "chandipur_marine_ps",
                name = "Chandipur Marine Police Station",
                odiaName = "ଚାନ୍ଦିପୁର ସାମୁଦ୍ରିକ ଥାନା",
                phone = "06782-272100",
                tollFree = "112",
                role = "Coastal Security & Beach Lifeguards",
                location = "Marine Drive, Chandipur",
                category = EmergencyCategory.POLICE,
                icon = Icons.Default.Security,
                availableHours = "24x7 Coastal Patrol"
            ),
            EmergencyContact(
                id = "women_cyber_cell",
                name = "Women Helpline & Cyber Crime Unit",
                odiaName = "ମହିଳା ସହାୟତା ଓ ସାଇବର ଥାନା",
                phone = "1930",
                tollFree = "181",
                role = "Financial Cyber Fraud & Women Safety",
                location = "District Police HQ, Balasore",
                category = EmergencyCategory.POLICE,
                icon = Icons.Default.Security,
                availableHours = "24x7 Toll-Free"
            ),

            // === FIRE & DISASTER RESCUE ===
            EmergencyContact(
                id = "balasore_main_fire",
                name = "Balasore Main Fire Station",
                odiaName = "ବାଲେଶ୍ୱର ମୁଖ୍ୟ ଅଗ୍ନିଶମ କେନ୍ଦ୍ର",
                phone = "06782-262101",
                tollFree = "101",
                role = "Fire Fighting & Rapid Accident Rescue",
                location = "Station Chhak, Balasore",
                category = EmergencyCategory.FIRE_RESCUE,
                icon = Icons.Default.LocalFireDepartment,
                availableHours = "24x7 Immediate Dispatch"
            ),
            EmergencyContact(
                id = "remuna_fire",
                name = "Remuna Fire & Rescue Station",
                odiaName = "ରେମୁଣା ଅଗ୍ନିଶମ କେନ୍ଦ୍ର",
                phone = "06782-275101",
                tollFree = "101",
                role = "Industrial Zone & Highway Rescue",
                location = "NH-16 / Remuna Industrial Area",
                category = EmergencyCategory.FIRE_RESCUE,
                icon = Icons.Default.LocalFireDepartment,
                availableHours = "24x7 Immediate Dispatch"
            ),
            EmergencyContact(
                id = "deoc_disaster",
                name = "District Emergency Operation Centre (DEOC)",
                odiaName = "ଜିଲ୍ଲା ଜରୁରୀକାଳୀନ ବିପର୍ଯ୍ୟୟ ପରିଚାଳନା କକ୍ଷ",
                phone = "06782-262274",
                tollFree = "1077",
                role = "Cyclone, Flood & Disaster Management",
                location = "Collectorate, Balasore",
                category = EmergencyCategory.FIRE_RESCUE,
                icon = Icons.Default.Security,
                availableHours = "24x7 Control Room"
            ),
            EmergencyContact(
                id = "odraf_balasore",
                name = "ODRAF 4th Battalion Rescue Unit",
                odiaName = "ଓଡ଼ିଶା ବିପର୍ଯ୍ୟୟ ଦ୍ରୁତ କାର୍ଯ୍ୟାନୁଷ୍ଠାନ ବଳ",
                phone = "06782-262333",
                role = "Marine Search & Flood Evacuation Force",
                location = "Balasore Coastal Command",
                category = EmergencyCategory.FIRE_RESCUE,
                icon = Icons.Default.Emergency,
                availableHours = "24x7 Rapid Deployment"
            )
        )
    }

    // Filtered contacts based on category and search query
    val filteredContacts = remember(selectedCategory, searchQuery, allContacts) {
        allContacts.filter { contact ->
            val matchesCategory = selectedCategory == EmergencyCategory.ALL || contact.category == selectedCategory
            val matchesSearch = searchQuery.isBlank() ||
                    contact.name.contains(searchQuery, ignoreCase = true) ||
                    contact.odiaName.contains(searchQuery, ignoreCase = true) ||
                    contact.role.contains(searchQuery, ignoreCase = true) ||
                    contact.location.contains(searchQuery, ignoreCase = true) ||
                    contact.phone.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp, 8.dp, 16.dp, 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ==========================================
        // DEDICATED EMERGENCY HEADER
        // ==========================================
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("emergency_section_header")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = BentoRedBg,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Emergency,
                                contentDescription = null,
                                tint = BentoRedText,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Text(
                        text = "EMERGENCY SERVICES",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = BentoRedText
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    // 24x7 active beacon
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = BentoGreenBg
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(BentoGreenText)
                            )
                            Text(
                                text = "24x7 Active",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = BentoGreenText,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Balasore Emergency Contacts",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = BentoSlate900
                )
                Text(
                    text = "One-tap calling for hospitals, police, fire stations & disaster rescue.",
                    style = MaterialTheme.typography.bodySmall,
                    color = BentoSlate500
                )
            }
        }

        // ==========================================
        // SOS SPEED-DIAL GRID (One-Tap Calling)
        // ==========================================
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = BentoCardWhite),
                border = BorderStroke(1.dp, BentoBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("sos_speed_dial_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "⚡ Instant SOS Speed-Dial",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = BentoSlate900
                        )
                        Text(
                            text = "Tap to call directly",
                            style = MaterialTheme.typography.labelSmall,
                            color = BentoSlate500
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 2x2 Grid of Quick SOS Dials
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SosQuickDialCard(
                            item = sosActions[0],
                            modifier = Modifier.weight(1f),
                            onClick = { dialPhoneNumber(context, sosActions[0].number) }
                        )
                        SosQuickDialCard(
                            item = sosActions[1],
                            modifier = Modifier.weight(1f),
                            onClick = { dialPhoneNumber(context, sosActions[1].number) }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SosQuickDialCard(
                            item = sosActions[2],
                            modifier = Modifier.weight(1f),
                            onClick = { dialPhoneNumber(context, sosActions[2].number) }
                        )
                        SosQuickDialCard(
                            item = sosActions[3],
                            modifier = Modifier.weight(1f),
                            onClick = { dialPhoneNumber(context, sosActions[3].number) }
                        )
                    }
                }
            }
        }

        // ==========================================
        // SEARCH & CATEGORY SELECTOR
        // ==========================================
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Search Input
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = "Search hospital, police, fire station...",
                            style = MaterialTheme.typography.bodySmall,
                            color = BentoSlate400
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = BentoSlate400,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear search",
                                    tint = BentoSlate500,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = BentoCardWhite,
                        unfocusedContainerColor = BentoCardWhite,
                        focusedBorderColor = BentoPrimaryBlue,
                        unfocusedBorderColor = BentoBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("emergency_search_field")
                )

                // Category Filter Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EmergencyCategory.entries.forEach { category ->
                        val isSelected = selectedCategory == category
                        val count = if (category == EmergencyCategory.ALL) {
                            allContacts.size
                        } else {
                            allContacts.count { it.category == category }
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) BentoPrimaryBlue else BentoCardWhite,
                            border = if (isSelected) null else BorderStroke(1.dp, BentoBorder),
                            shadowElevation = if (isSelected) 2.dp else 0.dp,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { selectedCategory = category }
                                .testTag("emergency_chip_${category.name.lowercase()}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = category.label,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = if (isSelected) Color.White else BentoSlate700
                                )
                                Surface(
                                    shape = CircleShape,
                                    color = if (isSelected) Color.White.copy(alpha = 0.25f) else BentoSlate100,
                                    modifier = Modifier.size(18.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = count.toString(),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else BentoSlate700
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // ==========================================
        // DEDICATED EMERGENCY CONTACT CARDS
        // ==========================================
        if (filteredContacts.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = BentoCardWhite),
                    border = BorderStroke(1.dp, BentoBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = BentoSlate400,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "No emergency contacts found",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = BentoSlate900
                        )
                        Text(
                            text = "Try searching for a different keyword or select another category.",
                            style = MaterialTheme.typography.bodySmall,
                            color = BentoSlate500
                        )
                    }
                }
            }
        } else {
            items(filteredContacts, key = { it.id }) { contact ->
                EmergencyContactCard(
                    contact = contact,
                    onCallClick = { dialPhoneNumber(context, contact.phone) }
                )
            }
        }

        // ==========================================
        // LOCAL ALERTS & FCM PUSH NOTIFICATIONS
        // ==========================================
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = BentoCardWhite),
                border = BorderStroke(1.dp, BentoBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("fcm_alerts_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = BentoRedBg,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.NotificationsActive,
                                        contentDescription = null,
                                        tint = BentoRedText,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = "Local Alerts & Push Opt-In",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = BentoSlate900
                                )
                                Text(
                                    text = "ପୁଶ୍ ବିଜ୍ଞପ୍ତି ଓ ଜରୁରୀ ସତର୍କତା (FCM)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = BentoSlate500
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = BentoBlueLight,
                            border = BorderStroke(1.dp, BentoBorder),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable(onClick = onOpenAlertCenter)
                                .testTag("open_alert_center_pill")
                        ) {
                            Text(
                                text = "Alert Center →",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = BentoPrimaryBlue,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Severe Weather & Tide Alerts Opt-in Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Severe Weather & Marine Warnings",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = BentoSlate900
                            )
                            Text(
                                text = "Bay of Bengal storms & Chandipur beach tidal surges",
                                style = MaterialTheme.typography.bodySmall,
                                color = BentoSlate500
                            )
                        }
                        Switch(
                            checked = weatherAlertsEnabled,
                            onCheckedChange = onToggleWeatherAlerts,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = BentoPrimaryBlue,
                                uncheckedThumbColor = BentoSlate400,
                                uncheckedTrackColor = BentoBorder
                            ),
                            modifier = Modifier.testTag("essentials_switch_weather")
                        )
                    }

                    HorizontalDivider(color = BentoBorder, modifier = Modifier.padding(vertical = 6.dp))

                    // Breaking News & Civic Bulletins Opt-in Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Breaking News & Civic Wire",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = BentoSlate900
                            )
                            Text(
                                text = "Collectorate orders, civic bulletins, local road & transit notices",
                                style = MaterialTheme.typography.bodySmall,
                                color = BentoSlate500
                            )
                        }
                        Switch(
                            checked = breakingNewsEnabled,
                            onCheckedChange = onToggleBreakingNews,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = BentoPrimaryBlue,
                                uncheckedThumbColor = BentoSlate400,
                                uncheckedTrackColor = BentoBorder
                            ),
                            modifier = Modifier.testTag("essentials_switch_news")
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Quick Simulator Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onSimulateWeatherAlert,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, BentoBorder),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("essentials_test_weather_btn")
                        ) {
                            Text("⚡ Test Weather", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BentoPrimaryBlue)
                        }

                        OutlinedButton(
                            onClick = onSimulateBreakingNews,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, BentoBorder),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("essentials_test_news_btn")
                        ) {
                            Text("📰 Test News", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BentoPrimaryBlue)
                        }
                    }
                }
            }
        }

        // ==========================================
        // BALASORE TRANSIT & CONNECTIVITY
        // ==========================================
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = BentoCardWhite),
                border = BorderStroke(1.dp, BentoBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("transit_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Balasore Transit & Connectivity",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = BentoSlate900
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    BentoTransitItem(
                        icon = Icons.Default.Train,
                        title = "Balasore Junction (BLS)",
                        desc = "A1-category station on Howrah–Chennai mainline. Direct superfast & Vande Bharat trains connect Kolkata, Bhubaneswar, Chennai, and Delhi."
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    BentoTransitItem(
                        icon = Icons.Default.DirectionsBus,
                        title = "Sahadevkhunta Central Bus Stand",
                        desc = "Frequent OSRTC and private deluxe buses operate to Bhubaneswar, Cuttack, Baripada, Digha, and Jaleswar."
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    BentoTransitItem(
                        icon = Icons.Default.Info,
                        title = "Tourist Information Counter (OTDC)",
                        desc = "Located at Balasore Railway Station Concourse (Ph: 06782-262048) for local guides and itinerary advice."
                    )
                }
            }
        }

        // ==========================================
        // OFFICIAL SOCIAL MEDIA PLATFORMS
        // ==========================================
        item {
            BalasoreSocialHubCard()
        }

        // ==========================================
        // GOOGLE PLAY STORE & COMPLIANCE SPECIFICATIONS
        // ==========================================
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = BentoBlueLight),
                border = BorderStroke(1.dp, BentoBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("play_store_info_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = BentoPrimaryBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Google Play Store Ready",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = BentoPrimaryBlue
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "• Version: 1.0.0 (Production Release ready)\n" +
                                "• Theme: Bento Grid System with Dark/Light Support\n" +
                                "• Package: com.niharsales.balasore360\n" +
                                "• Target SDK: Android 15 (API 36)\n" +
                                "• Zero-permission photo picker & privacy compliant\n" +
                                "• Instant one-tap phone dialer (Intent.ACTION_DIAL)\n" +
                                "• Offline-first SQLite/Room database caching\n" +
                                "• Dedicated to the citizens and tourists of Balasore, Odisha",
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 20.sp,
                        color = BentoSlate700
                    )
                }
            }
        }
    }
}

/**
 * High-craft Speed-Dial Card for immediate SOS access.
 */
@Composable
fun SosQuickDialCard(
    item: SosSpeedDialItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = item.bgColor),
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .testTag(item.id)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = item.textColor.copy(alpha = 0.15f),
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = item.textColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = item.textColor,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onClick)
                        .testTag("sos_btn_${item.number}")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Dial ${item.number}",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = item.number,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
                color = item.textColor,
                lineHeight = 26.sp
            )

            Text(
                text = item.label,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = item.textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = item.subtitle,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = item.textColor.copy(alpha = 0.8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Detailed Bento Card for individual Emergency Services with One-Tap Calling.
 */
@Composable
fun EmergencyContactCard(
    contact: EmergencyContact,
    modifier: Modifier = Modifier,
    onCallClick: () -> Unit
) {
    val (categoryColor, categoryBg) = when (contact.category) {
        EmergencyCategory.HOSPITALS -> Pair(BentoRedText, BentoRedBg)
        EmergencyCategory.POLICE -> Pair(Color(0xFF4338CA), Color(0xFFEEF2FF))
        EmergencyCategory.FIRE_RESCUE -> Pair(BentoAmberText, BentoAmberBg)
        EmergencyCategory.ALL -> Pair(BentoPrimaryBlue, BentoBluePill)
    }

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = BentoCardWhite),
        border = BorderStroke(1.dp, BentoBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .clickable(onClick = onCallClick)
            .testTag("contact_card_${contact.id}")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Category & Availability Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = categoryBg
                ) {
                    Text(
                        text = contact.category.label.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        ),
                        color = categoryColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        fontSize = 10.sp
                    )
                }

                Text(
                    text = contact.availableHours,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                    color = BentoGreenText,
                    fontSize = 11.sp
                )
            }

            // Name, Odia Name, Location
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = categoryBg,
                    modifier = Modifier.size(46.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = contact.icon,
                            contentDescription = null,
                            tint = categoryColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = contact.name,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = BentoSlate900
                    )
                    Text(
                        text = contact.odiaName,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        fontSize = 12.sp,
                        color = BentoPrimaryBlue
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${contact.role} • ${contact.location}",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.sp,
                        color = BentoSlate500,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Action Row: Phone Display & One-Tap Call Button
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = BentoSlate100,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhoneInTalk,
                            contentDescription = null,
                            tint = BentoSlate700,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = contact.phone,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = BentoSlate900
                        )
                        if (contact.tollFree != null) {
                            Text(
                                text = "(${contact.tollFree})",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = BentoPrimaryBlue,
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Direct One-Tap Call Button
                    Button(
                        onClick = onCallClick,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BentoPrimaryBlue,
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier
                            .height(38.dp)
                            .testTag("call_btn_${contact.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call ${contact.name}",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Call",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BentoTransitItem(
    icon: ImageVector,
    title: String,
    desc: String
) {
    Row(verticalAlignment = Alignment.Top) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = BentoBlueLight,
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = BentoPrimaryBlue,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = BentoSlate900
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall,
                color = BentoSlate500
            )
        }
    }
}

