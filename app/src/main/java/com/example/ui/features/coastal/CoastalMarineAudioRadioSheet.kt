package com.example.ui.features.coastal

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.speech.tts.TextToSpeech
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
import androidx.compose.material.icons.automirrored.filled.VolumeUp
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
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoastalMarineAudioRadioSheet(
    language: AppLanguage,
    dailyPulse: DailyBalasorePulse,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var isSpeaking by remember { mutableStateOf(false) }
    var selectedHarborIndex by remember { mutableIntStateOf(0) }
    var ttsEngine by remember { mutableStateOf<TextToSpeech?>(null) }

    // Initialize Android TextToSpeech engine
    DisposableEffect(context) {
        val tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Default to Indian English / Hindi / Odia fallback
                ttsEngine?.language = Locale.Builder().setLanguage("en").setRegion("IN").build()
            }
        }
        ttsEngine = tts
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    val harbors = listOf(
        HarborDetail(
            nameEn = "Balaramgadi Fish Harbor",
            nameOd = "ବଳରାମଗଡ଼ି ମତ୍ସ୍ୟ ବନ୍ଦର",
            location = "Budhabalanga River Estuary",
            activeBoats = 142,
            ebbTideBarWarning = "Shallow Bar Ebb Crossing: Caution between 01:00 PM - 03:30 PM",
            emergencyHelpline = "+916782275100"
        ),
        HarborDetail(
            nameEn = "Kasafal Landing Ghat",
            nameOd = "କସାଫାଳ ମତ୍ସ୍ୟ ଅବତରଣ କେନ୍ଦ୍ର",
            location = "North Balasore Coastline",
            activeBoats = 86,
            ebbTideBarWarning = "Low swell sandbar open for country craft. High water 05:15 PM.",
            emergencyHelpline = "+916782262100"
        ),
        HarborDetail(
            nameEn = "Chaumukha & Subarnarekha Mouth",
            nameOd = "ଚୌମୁଖା ଓ ସୁବର୍ଣ୍ଣରେଖା ମୁହାଣ",
            location = "Bhograi Border Sector",
            activeBoats = 64,
            ebbTideBarWarning = "Estuary tidal rush active. Speed limit 6 knots inside mouth.",
            emergencyHelpline = "+916781254300"
        ),
        HarborDetail(
            nameEn = "Dhamra & Chudamani Port Sector",
            nameOd = "ଚୂଡ଼ାମଣି ଓ ଧାମରା ଉପକୂଳ",
            location = "South Balasore Deep Sea Channel",
            activeBoats = 195,
            ebbTideBarWarning = "Deep draught trawlers navigating navigation buoys 4 to 8.",
            emergencyHelpline = "1554"
        )
    )

    val safetyChecklist = remember {
        mutableStateListOf(
            ChecklistItem("Lifejackets (ISI Mark) for all crew members on board", true),
            ChecklistItem("NavIC / GPS marine receiver with distress button", true),
            ChecklistItem("VHF Marine Radio tuned to International Distress Channel 16", true),
            ChecklistItem("Fresh drinking water canisters (minimum 20L per crew)", false),
            ChecklistItem("Waterproof buoyant torches & red pyrotechnic hand flares", false),
            ChecklistItem("Moored boat anchor chain & spare engine fuel can", false)
        )
    }

    val bulletinText = remember(selectedHarborIndex, dailyPulse) {
        val harbor = harbors[selectedHarborIndex]
        "Balasore Coastal Marine Bulletin from INCOIS and Port Authority. Current wave swell is ${dailyPulse.marineRadioWaveMeters} meters with sea flag color ${dailyPulse.incoisSeaFlagColor}. Wind gusts at ${dailyPulse.marineRadioWindKnots} knots from South-East. At ${harbor.nameEn}, ${harbor.activeBoats} trawlers and country craft are registered. Advising all craft to observe return-to-shore window within ${dailyPulse.marineReturnCountdownMinutes} minutes before high tide reversal."
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
                            text = if (language == AppLanguage.ODIA) "ସମୁଦ୍ର ମତ୍ସ୍ୟଜୀବୀ ରେଡିଓ ବୁଲେଟିନ୍ 🎙️" else "Coastal Marine Audio Radio 🎙️",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "INCOIS ଓ ବଳରାମଗଡ଼ି ସମୁଦ୍ର ସତର୍କତା ଓ ସୁରକ୍ଷା ରିଙ୍ଗ୍" else "INCOIS Swell Alerts & Fishermen Safety Ring",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            }

            // Audio Player Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(if (isSpeaking) Color(0xFFEF4444) else Color(0xFF22C55E))
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isSpeaking) "LIVE AUDIO BROADCASTING..." else "RADIO BROADCAST READY",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "FREQ: 156.80 MHz (Ch 16)",
                                color = Color(0xFF94A3B8),
                                fontSize = 11.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = bulletinText,
                            color = Color(0xFFE2E8F0),
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    if (isSpeaking) {
                                        ttsEngine?.stop()
                                        isSpeaking = false
                                    } else {
                                        ttsEngine?.speak(
                                            bulletinText,
                                            TextToSpeech.QUEUE_FLUSH,
                                            null,
                                            "marine_bulletin_id"
                                        )
                                        isSpeaking = true
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSpeaking) Color(0xFFDC2626) else Color(0xFF0284C7)
                                )
                            ) {
                                Icon(
                                    if (isSpeaking) Icons.Default.Stop else Icons.AutoMirrored.Filled.VolumeUp,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (isSpeaking) "Stop Audio" else "Listen Broadcast")
                            }

                            OutlinedButton(
                                onClick = {
                                    val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1554"))
                                    context.startActivity(dialIntent)
                                },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFF87171)),
                                border = BorderStroke(1.dp, Color(0xFFEF4444))
                            ) {
                                Icon(Icons.Default.Sos, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("SOS 1554")
                            }
                        }
                    }
                }
            }

            // Real-Time Telemetry Ring
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Swell Height", style = MaterialTheme.typography.labelSmall)
                            Text(
                                "${dailyPulse.marineRadioWaveMeters} m",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(dailyPulse.incoisSeaFlagColor, fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Wind Knots", style = MaterialTheme.typography.labelSmall)
                            Text(
                                "${dailyPulse.marineRadioWindKnots} kts",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text("SE Direction", fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Return Window", style = MaterialTheme.typography.labelSmall)
                            Text(
                                "${dailyPulse.marineReturnCountdownMinutes} m",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text("Before Tide Flip", fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                        }
                    }
                }
            }

            // Offshore Distance Rings
            item {
                Text(
                    text = if (language == AppLanguage.ODIA) "ସମୁଦ୍ର ଦୂରତା ଜୋନ୍ ସୂଚକ" else "Offshore Distance Rings (Hazard Level)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    DistanceZoneCard(
                        zone = "0 - 5 km (Artisanal Craft)",
                        status = "SAFE BAND 🟢",
                        desc = "Suitable for Donga country boats & traditional gillnetters. Shallow sandbars navigable.",
                        color = Color(0xFFF0FDF4),
                        border = Color(0xFF86EFAC)
                    )
                    DistanceZoneCard(
                        zone = "5 - 12 km (Coastal Mechanized)",
                        status = "MODERATE SWELL 🟡",
                        desc = "Trawlers must keep AIS / VHF active. Estuary tide return advised before 05:00 PM.",
                        color = Color(0xFFFFFBEB),
                        border = Color(0xFFFDE68A)
                    )
                    DistanceZoneCard(
                        zone = "12+ km (Deep Sea Shelf)",
                        status = "CAUTION MONITORED 🟠",
                        desc = "High swell and cross-currents near Wheeler / Dhamra channel. Lifejackets strictly mandatory.",
                        color = Color(0xFFFFF1F2),
                        border = Color(0xFFFECDD3)
                    )
                }
            }

            // Fishing Harbors Selector
            item {
                Text(
                    text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ମତ୍ସ୍ୟ ବନ୍ଦର ସ୍ଥିତି" else "Balasore Fishing Harbors Roster",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            items(harbors) { harbor ->
                val isSelected = harbors.indexOf(harbor) == selectedHarborIndex
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedHarborIndex = harbors.indexOf(harbor) },
                    border = BorderStroke(
                        if (isSelected) 2.dp else 1.dp,
                        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (language == AppLanguage.ODIA) harbor.nameOd else harbor.nameEn,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Badge(containerColor = MaterialTheme.colorScheme.secondary) {
                                Text("${harbor.activeBoats} Boats", fontSize = 11.sp)
                            }
                        }
                        Text(harbor.location, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = harbor.ebbTideBarWarning,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Sea Safety Checklist
            item {
                Text(
                    text = if (language == AppLanguage.ODIA) "ସମୁଦ୍ର ଯାତ୍ରା ପୂର୍ବ ସୁରକ୍ଷା ଯାଞ୍ଚ (Offline)" else "Offline Pre-Departure Sea Safety Checklist",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            items(safetyChecklist) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val index = safetyChecklist.indexOf(item)
                            safetyChecklist[index] = item.copy(isChecked = !item.isChecked)
                        }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = item.isChecked,
                        onCheckedChange = { checked ->
                            val index = safetyChecklist.indexOf(item)
                            safetyChecklist[index] = item.copy(isChecked = checked)
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (item.isChecked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Emergency Contacts Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+916782275100"))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B))
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Marine Police", fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1554"))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB91C1C))
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Coast Guard", fontSize = 12.sp)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

private data class HarborDetail(
    val nameEn: String,
    val nameOd: String,
    val location: String,
    val activeBoats: Int,
    val ebbTideBarWarning: String,
    val emergencyHelpline: String
)

private data class ChecklistItem(
    val label: String,
    val isChecked: Boolean
)

@Composable
private fun DistanceZoneCard(
    zone: String,
    status: String,
    desc: String,
    color: Color,
    border: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color),
        border = BorderStroke(1.dp, border),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(zone, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF0F172A))
                Text(status, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF1E293B))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(desc, fontSize = 12.sp, color = Color(0xFF334155), lineHeight = 16.sp)
        }
    }
}
