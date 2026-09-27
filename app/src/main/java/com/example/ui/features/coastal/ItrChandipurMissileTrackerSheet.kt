package com.example.ui.features.coastal

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
fun ItrChandipurMissileTrackerSheet(
    language: AppLanguage,
    dailyPulse: DailyBalasorePulse,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedMissileIndex by remember { mutableIntStateOf(0) }
    var showAcousticAdvisory by remember { mutableStateOf(false) }

    val missileHeritage = listOf(
        MissileHistoryItem(
            name = "Agni-V Intercontinental Ballistic Missile (ICBM)",
            range = "5,000+ km",
            significance = "Canisterized road-mobile MIRV deterrence; strategic pride of APJ Abdul Kalam Island.",
            status = "Operational User Trials"
        ),
        MissileHistoryItem(
            name = "BrahMos Supersonic Cruise Missile",
            range = "290 - 450 km (Mach 3.0)",
            significance = "Joint Indo-Russian stealth precision strike tested repeatedly at Chandipur Launch Complex-III.",
            status = "Standard Induction"
        ),
        MissileHistoryItem(
            name = "Prithvi-II Surface-to-Surface Missile",
            range = "350 km",
            significance = "First indigenous guided missile under Integrated Guided Missile Development Programme (IGMDP).",
            status = "Strategic Forces"
        ),
        MissileHistoryItem(
            name = "Akash-NG (Next Generation Air Defense)",
            range = "70 - 80 km",
            significance = "Rapid reaction surface-to-air missile intercepting hypersonic and high-maneuvering aerial threats.",
            status = "Production Phase"
        ),
        MissileHistoryItem(
            name = "Pralay Short-Range Ballistic Missile (SRBM)",
            range = "150 - 500 km",
            significance = "Solid-fuel quasi-ballistic missile with mid-air maneuverability launched from LC-IV.",
            status = "Tactical Induction"
        )
    )

    val coastalSafetyZones = listOf(
        SafetyZoneItem("Zone 1: Chandipur Launch Pad Perimeter (0 - 5 km)", "STRICT EXCLUSION ⛔", "Unauthorized boats and drones strictly barred by DRDO security & Coast Guard."),
        SafetyZoneItem("Zone 2: Coastal Fishing Ban Belt (5 - 35 km)", if (dailyPulse.itrLaunchWarningActive) "RESTRICTED FOR TEST ⚠️" else "OPEN FOR FISHING 🟢", "Balaramgadi & Kasafal fishermen must tune to VHF Ch-16 for NOTAM advisories."),
        SafetyZoneItem("Zone 3: Wheeler / Kalam Island Deep Sea Sector", "RADAR SURVEILLANCE 📡", "Indian Navy offshore patrol vessels enforce safe maritime passage corridor.")
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
                            text = if (language == AppLanguage.ODIA) "ଚାନ୍ଦିପୁର ITR କ୍ଷେପଣାସ୍ତ୍ର ପରୀକ୍ଷା ଓ ଉପକୂଳ ରାଡାର୍ 🚀" else "ITR Chandipur Missile & Airspace Radar 🚀",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଡିଆରଡିଓ ଇଣ୍ଟିଗ୍ରେଟେଡ୍ ଟେଷ୍ଟ୍ ରେଞ୍ଜ୍ ସୁରକ୍ଷା ଓ ଐତିହାସିକ କ୍ଷେପଣାସ୍ତ୍ର ମାଇଲଖୁଣ୍ଟ" else "DRDO ITR Chandipur & Abdul Kalam Island Advisory Tracker",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            }

            // Live Radar Warning Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(if (dailyPulse.itrLaunchWarningActive) Color(0xFFEF4444) else Color(0xFF22C55E))
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (dailyPulse.itrLaunchWarningActive) "NOTAM / LAUNCH WINDOW ACTIVE" else "AIRSPACE & OFFSHORE CLEAR",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                "ITR / DRDO BLS",
                                color = Color(0xFF38BDF8),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = dailyPulse.itrNextTestWindow,
                            color = Color(0xFFE2E8F0),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { showAcousticAdvisory = !showAcousticAdvisory },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7))
                            ) {
                                Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (showAcousticAdvisory) "Hide Sonic Advisory" else "Sonic Boom Advisory")
                            }

                            Button(
                                onClick = {
                                    val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+916782272144"))
                                    context.startActivity(dialIntent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155))
                            ) {
                                Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("ITR Security")
                            }
                        }
                    }
                }
            }

            // Sonic Boom & Acoustic Advisory Notice
            if (showAcousticAdvisory) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                        border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFFB45309), modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Acoustic & Vibration Notice for Coastal Residents",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFF78350F)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "During high-velocity supersonic trials, low-frequency acoustic thunderclaps or sonic booms may be felt across Chandipur, Balaramgadi, Kasafal, and Soro. It is completely safe; residents are advised to keep glass windowpanes slightly ajar to prevent resonant rattling.",
                                fontSize = 12.sp,
                                color = Color(0xFF92400E),
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }

            // Offshore Safety Zones Roster
            item {
                Text(
                    text = if (language == AppLanguage.ODIA) "ଉପକୂଳ ସୁରକ୍ଷା ଓ ମତ୍ସ୍ୟ ନିଷିଦ୍ଧାଞ୍ଚଳ" else "Coastal Maritime Exclusion & Safety Belts",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            items(coastalSafetyZones) { zone ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(zone.zoneName, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.weight(1f))
                            Badge(containerColor = MaterialTheme.colorScheme.secondary) {
                                Text(zone.status, fontSize = 10.sp, color = Color.White)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(zone.instructions, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            // Indigenous Missile Milestones
            item {
                Text(
                    text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ଚାନ୍ଦିପୁର କ୍ଷେପଣାସ୍ତ୍ର ଇତିହାସ" else "Indigenous Defense Milestones (Chandipur)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            items(missileHeritage) { missile ->
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
                            Text(missile.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.weight(1f))
                            Badge(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                                Text(missile.range, fontSize = 10.sp, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(missile.significance, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 16.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Class: ${missile.status}", fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Medium)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

private data class MissileHistoryItem(
    val name: String,
    val range: String,
    val significance: String,
    val status: String
)

private data class SafetyZoneItem(
    val zoneName: String,
    val status: String,
    val instructions: String
)
