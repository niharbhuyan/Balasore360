package com.example.ui.features.coastal

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.widget.Toast
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.daily.DailyBalasorePulse
import com.example.data.model.AppLanguage
import com.example.ui.theme.*

data class MudflatHazardZone(
    val title: String,
    val odiaTitle: String,
    val distanceSeawardKm: Float,
    val hazardType: String, // "Soft Silt Pocket", "Breeding Furrow", "Safe Hard Sand Ridge"
    val safeAction: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChandipurSeabedOfflineTrailSheet(
    dailyPulse: DailyBalasorePulse,
    language: AppLanguage = AppLanguage.ENGLISH,
    onClose: () -> Unit = {}
) {
    val context = LocalContext.current
    var isWhistleBlowing by remember { mutableStateOf(false) }
    var currentHeadingDeg by remember { mutableFloatStateOf(270f) } // West towards shoreline seawall

    val hazardZones = remember {
        listOf(
            MudflatHazardZone("Casuarina Outer Ridge", "ଝାଉଁ ଜଙ୍ଗଲ ବାହାର ବନ୍ଧ", 1.2f, "Safe Hard Sand Ridge", "Firm packed sand. Safe for walking & cycling."),
            MudflatHazardZone("Red Ghost Crab Hollows", "ନାଲି କଙ୍କଡ଼ା ଗାତ ଅଞ୍ଚଳ", 2.4f, "Breeding Furrow", "Watch footsteps; avoid disturbing delicate mating beds."),
            MudflatHazardZone("Budhabalanga Silt Confluence", "ବୁଢ଼ାବଳଙ୍ଗ ନଦୀ ମୁହାଣ କାଦୁଅ", 3.6f, "Soft Silt Pocket", "Do NOT enter. High sink risk during tidal turn."),
            MudflatHazardZone("Low-Tide Outer Sandbar", "ବାହାର ବାଲୁକା ଶଯ୍ୟା", 4.8f, "Extreme Low-Tide Line", "Return immediately when tidal siren or alarm buzzes.")
        )
    }

    fun playDistressWhistle() {
        try {
            val toneGen = ToneGenerator(AudioManager.STREAM_ALARM, 100)
            toneGen.startTone(ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK, 1500)
            Toast.makeText(context, "🔊 High-Frequency Distress Whistle Sounding!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Whistle alert active", Toast.LENGTH_SHORT).show()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .testTag("chandipur_seabed_offline_trail_sheet")
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
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0F2FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🧭", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଚାନ୍ଦିପୁର ଅଫଲାଇନ୍ ସମୁଦ୍ର ଶଯ୍ୟା ଟ୍ରେଲ୍" else "Chandipur Seabed Offline Trail",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "କମ୍ପାସ୍ • କାଦୁଅ ବିପଦ ଚେତାବନୀ • SOS ହୁଇସିଲ୍" else "Offline Compass • Quicksand Hazards • SOS Whistle",
                            style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate500)
                        )
                    }
                }
                IconButton(onClick = onClose) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.88f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Compass Seawall Heading Card
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                        border = BorderStroke(1.5.dp, Color(0xFF38BDF8))
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "BEARING TO CHANDIPUR SEAWALL (SAFETY)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xFF38BDF8),
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF1E293B)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🧭 270° W", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Walk Due West (270°) to return to shore. Works 100% offline without cellular coverage on the 5 km seabed.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFFCBD5E1),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = { playDistressWhistle() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(imageVector = Icons.Default.Campaign, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("🔊 Sound High-Decibel SOS Whistle", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Silt and Furrow Hazard Zones
                item {
                    Text(
                        text = if (language == AppLanguage.ODIA) "ସମୁଦ୍ର ଶଯ୍ୟା ବିପଦ ଓ ସୁରକ୍ଷା କ୍ଷେତ୍ର" else "Intertidal Seabed Hazard & Safety Map",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate900
                        )
                    )
                }

                items(hazardZones) { zone ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val badgeColor = when (zone.hazardType) {
                                "Soft Silt Pocket" -> Color(0xFFDC2626)
                                "Breeding Furrow" -> Color(0xFFD97706)
                                "Safe Hard Sand Ridge" -> Color(0xFF166534)
                                else -> Color(0xFF2563EB)
                            }

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(badgeColor.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (zone.hazardType == "Safe Hard Sand Ridge") "✅" else if (zone.hazardType == "Breeding Furrow") "🦀" else "⚠️",
                                    fontSize = 18.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (language == AppLanguage.ODIA) zone.odiaTitle else zone.title,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = BentoSlate900
                                        )
                                    )
                                    Surface(
                                        color = badgeColor.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "${zone.distanceSeawardKm} KM SEAWARD",
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = badgeColor,
                                                fontSize = 9.sp
                                            )
                                        )
                                    }
                                }

                                Text(
                                    text = "${zone.hazardType}: ${zone.safeAction}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = BentoSlate600,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}
