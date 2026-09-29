package com.example.ui.features.resilience

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.daily.DailyBalasorePulse
import com.example.data.model.AppLanguage
import com.example.ui.theme.*

data class OfflineShelterInfo(
    val id: String,
    val name: String,
    val nameOd: String,
    val block: String,
    val capacityPersons: Int,
    val elevationMeters: Float,
    val landmark: String,
    val contactNumber: String
)

data class OfflineSurvivalItem(
    val id: String,
    val title: String,
    val titleOd: String,
    var isChecked: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfflineCycloneSafetyToolkitSheet(
    language: AppLanguage,
    dailyPulse: DailyBalasorePulse,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val isOdia = language == AppLanguage.ODIA
    var isStrobeActive by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "strobe")
    val strobeColor by infiniteTransition.animateColor(
        initialValue = Color(0xFFEF4444),
        targetValue = Color(0xFFF59E0B),
        animationSpec = infiniteRepeatable(
            animation = tween<Color>(250, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "strobe_color"
    )

    val shelters = remember {
        listOf(
            OfflineShelterInfo(
                id = "sh_1",
                name = "Chandipur Multi-Purpose Cyclone Shelter",
                nameOd = "ଚାନ୍ଦିପୁର ବହୁମୁଖୀ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳୀ",
                block = "Balasore Sadar",
                capacityPersons = 1200,
                elevationMeters = 8.5f,
                landmark = "Near Chandipur High School & Beach Road",
                contactNumber = "06782-262244"
            ),
            OfflineShelterInfo(
                id = "sh_2",
                name = "Kasafal Coastal Flood & Storm Shelter",
                nameOd = "କାସାଫାଳ ଉପକୂଳ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳୀ",
                block = "Bahanaga / Kasafal",
                capacityPersons = 950,
                elevationMeters = 7.2f,
                landmark = "Near Kasafal Primary Health Center",
                contactNumber = "06782-271233"
            ),
            OfflineShelterInfo(
                id = "sh_3",
                name = "Talasari Estuary Cyclone Center",
                nameOd = "ତାଳସାରୀ ମୁହାଣ ବାତ୍ୟା ଆଶ୍ରୟକେନ୍ଦ୍ର",
                block = "Bhograi Block",
                capacityPersons = 1100,
                elevationMeters = 9.0f,
                landmark = "Near Talasari Forest Checkpost",
                contactNumber = "06781-233400"
            ),
            OfflineShelterInfo(
                id = "sh_4",
                name = "Bahanaga Multipurpose Community Shelter",
                nameOd = "ବାହାନଗା ବହୁମୁଖୀ ସମୁଦାୟ ଆଶ୍ରୟସ୍ଥଳୀ",
                block = "Bahanaga",
                capacityPersons = 800,
                elevationMeters = 11.0f,
                landmark = "Near Bahanaga Bazar Railway Crossing",
                contactNumber = "06782-284100"
            )
        )
    }

    var survivalChecklist by remember {
        mutableStateOf(
            listOf(
                OfflineSurvivalItem("c_1", "Battery Torch & Extra Cells", "ବ୍ୟାଟେରୀ ଲାଇଟ୍ ଓ ଅତିରିକ୍ତ ସେଲ୍", true),
                OfflineSurvivalItem("c_2", "Dry Food: Chuda, Mudhi & Biscuits (3 Days)", "ଶୁଖିଲା ଖାଦ୍ୟ: ଚୁଡ଼ା, ମୁଢ଼ି ଓ ବିସ୍କୁଟ", true),
                OfflineSurvivalItem("c_3", "Boiled Water Bottles & Chlorine Halazone Tablets", "ଫୁଟା ପାଣି ଓ ହାଲାଜୋନ୍ ବଟିକା", true),
                OfflineSurvivalItem("c_4", "Important Documents Wrapped in Waterproof Polybag", "ଜରୁରୀ କାଗଜପତ୍ର ପଲିଥିନ୍ ସୁରକ୍ଷିତ", false),
                OfflineSurvivalItem("c_5", "Charged Power Bank & Emergency FM Radio", "ଚାର୍ଜ୍ ଥିବା ପାୱାର ବ୍ୟାଙ୍କ ଓ ରେଡିଓ", false),
                OfflineSurvivalItem("c_6", "Basic First-Aid: Bandages, Antiseptic, ORS", "ପ୍ରାଥମିକ ଚିକିତ୍ସା ସରଞ୍ଜାମ ଓ ଓଆରଏସ୍", false)
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag("offline_cyclone_safety_toolkit_sheet")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFEF3C7),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🛡️", fontSize = 24.sp)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (isOdia) "ଅଫଲାଇନ୍ ବାତ୍ୟା ଓ ବନ୍ୟା ସୁରକ୍ଷା ଟୁଲକିଟ୍" else "Offline Cyclone & Flood Toolkit",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = BentoSlate900
                    )
                    Text(
                        text = if (isOdia) "ଇଣ୍ଟରନେଟ୍ ବିନା କାର୍ଯ୍ୟକ୍ଷମ • ଜରୁରୀ SOS ବିକନ୍" else "100% Offline Ready • SOS Strobe Beacon",
                        style = MaterialTheme.typography.bodySmall,
                        color = BentoSlate500
                    )
                }
            }
            IconButton(
                onClick = onClose,
                modifier = Modifier.testTag("btn_close_cyclone_toolkit_sheet")
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = BentoSlate600)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // SOS High Visibility Strobe Beacon
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isStrobeActive) strobeColor else Color(0xFFFEF2F2)
            ),
            border = BorderStroke(1.dp, Color(0xFFFCA5A5)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isStrobeActive) "🚨 SOS SEARCH & RESCUE STROBE ON" else "Emergency SOS Strobe Beacon",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isStrobeActive) Color.White else Color(0xFF991B1B)
                        )
                        Text(
                            text = "High-visibility flashing beacon for aerial & ground search teams.",
                            fontSize = 11.sp,
                            color = if (isStrobeActive) Color.White.copy(alpha = 0.9f) else BentoSlate600
                        )
                    }
                    Button(
                        onClick = { isStrobeActive = !isStrobeActive },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isStrobeActive) Color.Black else Color(0xFFDC2626)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(if (isStrobeActive) "STOP" else "ACTIVATE", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Direct Control Room Calling Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:1077") }
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB91C1C)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Dial 1077 Disaster", fontSize = 12.sp)
            }

            OutlinedButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:112") }
                    context.startActivity(intent)
                },
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, OceanBlue),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Shield, contentDescription = null, tint = OceanBlue, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("State Helpline 112", fontSize = 12.sp, color = OceanBlue)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Survival Checklist Section
        Text(
            text = if (isOdia) "ଜରୁରୀ ସାମଗ୍ରୀ ତାଲିକା (୭୨-ଘଣ୍ଟା କିଟ୍)" else "Offline 72-Hour Survival Checklist",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = BentoSlate800
        )

        Spacer(modifier = Modifier.height(8.dp))

        survivalChecklist.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        survivalChecklist = survivalChecklist.map {
                            if (it.id == item.id) it.copy(isChecked = !it.isChecked) else it
                        }
                    }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = item.isChecked,
                    onCheckedChange = { checked ->
                        survivalChecklist = survivalChecklist.map {
                            if (it.id == item.id) it.copy(isChecked = checked) else it
                        }
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isOdia) item.titleOd else item.title,
                    fontSize = 12.sp,
                    color = if (item.isChecked) BentoSlate400 else BentoSlate800,
                    fontWeight = if (item.isChecked) FontWeight.Normal else FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = if (isOdia) "ନିକଟତମ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳୀ (ଅଫଲାଇନ୍ ତଥ୍ୟ)" else "Designated Cyclone Shelters (Offline Directory)",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = BentoSlate800
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(shelters, key = { it.id }) { sh ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    border = BorderStroke(1.dp, BentoSlate200),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isOdia) sh.nameOd else sh.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = BentoSlate900
                            )
                            Text(
                                text = "Cap: ${sh.capacityPersons}",
                                fontSize = 11.sp,
                                color = Color(0xFF15803D),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "📍 ${sh.landmark} (${sh.block}) • Elev: ${sh.elevationMeters}m",
                            fontSize = 11.sp,
                            color = BentoSlate600
                        )
                    }
                }
            }
        }
    }
}
