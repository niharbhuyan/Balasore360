package com.example.ui.features.coastal

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.daily.DailyBalasorePulse
import com.example.data.model.AppLanguage
import com.example.ui.theme.*

data class BioluminescentSightingSpot(
    val nameEn: String,
    val nameOd: String,
    val distanceKm: Float,
    val bestTimeWindow: String,
    val substrateType: String,
    val safetyAdvice: String,
    val latitude: Double,
    val longitude: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChandipurBioluminescenceExplorerSheet(
    language: AppLanguage,
    dailyPulse: DailyBalasorePulse,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var redTorchSimulated by remember { mutableStateOf(false) }

    val spots = listOf(
        BioluminescentSightingSpot(
            nameEn = "Chandipur Main Seabed (1.5 km Offshore)",
            nameOd = "ଚାନ୍ଦିପୁର ମୁଖ୍ୟ ଭଟ୍ଟା ସମୁଦ୍ର ଶଯ୍ୟା (୧.୫ କିମି)",
            distanceKm = 1.5f,
            bestTimeWindow = "08:30 PM - 11:45 PM (Ebb Tide)",
            substrateType = "Firm Silt & Sand Ripples",
            safetyAdvice = "Observe with red-spectrum light to preserve ghost crab night vision.",
            latitude = 21.4682,
            longitude = 87.0210
        ),
        BioluminescentSightingSpot(
            nameEn = "Balaramgadi Estuarine Sandbar",
            nameOd = "ବଳରାମଗଡ଼ି ମୁହାଣ ବାଲୁକା ଶଯ୍ୟା",
            distanceKm = 3.2f,
            bestTimeWindow = "09:00 PM - Midnight",
            substrateType = "Brackish Tidal Pools",
            safetyAdvice = "High density of Noctiluca scintillans micro-algae blooms.",
            latitude = 21.4725,
            longitude = 87.0345
        ),
        BioluminescentSightingSpot(
            nameEn = "Mirzapur Mangrove Creek Edge",
            nameOd = "ମିର୍ଜାପୁର ହେନ୍ତାଳବଣ ଖାଲ",
            distanceKm = 4.8f,
            bestTimeWindow = "10:00 PM - 01:00 AM",
            substrateType = "Mudflats with Phosphorescent Plankton",
            safetyAdvice = "Wear rubber booties; observe phosphorescence when stirring water.",
            latitude = 21.4810,
            longitude = 87.0490
        )
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color(0xFF0B1329),
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .testTag("chandipur_bioluminescence_sheet")
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
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0369A1).copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.NightsStay,
                            contentDescription = "Bioluminescence Icon",
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଚାନ୍ଦିପୁର ସମୁଦ୍ର ନୀଳ ଆଲୋକ ଓ ରାତ୍ରି ୱାଚ୍" else "Chandipur Bioluminescence Radar",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଜୈବ-ଆଲୋକିତ ନୋକ୍ଟିଲୁକା ଓ ଅନ୍ଧକାର ଆକାଶ" else "Noctiluca Scintillans & Night Low-Tide Trail",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF94A3B8),
                                fontSize = 11.5.sp
                            )
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("bioluminescence_close_btn")
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Glow Probability Index Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF0369A1).copy(alpha = 0.6f), Color(0xFF0F172A))
                        )
                    )
                    .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.4f), RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (language == AppLanguage.ODIA) "ଜୈବ-ଆଲୋକ ଦୃଶ୍ୟମାନତା ସମ୍ଭାବନା" else "Nocturnal Glow Probability",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = Color(0xFFBAE6FD),
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "${dailyPulse.chandipurBioluminescenceIndex.toInt()}% Glow Index",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    color = Color(0xFF38BDF8),
                                    fontWeight = FontWeight.Black
                                )
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF0284C7).copy(alpha = 0.3f),
                            border = BorderStroke(1.dp, Color(0xFF38BDF8))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = Color(0xFF7DD3FC),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "HIGH GLOW",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFBAE6FD)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = dailyPulse.bioluminescenceVisibilityText,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFE2E8F0),
                            fontSize = 12.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "⏳ Safe Walk Window: ${dailyPulse.nightTideSafeWalkingMinutes} min",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFFDE047))
                        )
                        Text(
                            text = "🌊 Water Temp: 27.5°C",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF93C5FD))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Red Torch Night-Vision Toggle
            Surface(
                onClick = { redTorchSimulated = !redTorchSimulated },
                shape = RoundedCornerShape(14.dp),
                color = if (redTorchSimulated) Color(0xFF7F1D1D) else Color(0xFF1E293B),
                border = BorderStroke(1.dp, if (redTorchSimulated) Color(0xFFEF4444) else Color(0xFF334155)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("red_torch_toggle_btn")
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FlashlightOn,
                            contentDescription = "Torch Mode",
                            tint = if (redTorchSimulated) Color(0xFFF87171) else Color(0xFF94A3B8),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (language == AppLanguage.ODIA) "ଲାଲ୍ ଆଲୋକ ଇକୋ-ସୁରକ୍ଷା ଟର୍ଚ୍ଚ" else "Eco Red-Light Night Vision Mode",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = if (redTorchSimulated) "Red wavelength active • Harmless to marine fauna" else "Tap to switch to red-spectrum night safety filter",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (redTorchSimulated) Color(0xFFFCA5A5) else Color(0xFF94A3B8),
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    Switch(
                        checked = redTorchSimulated,
                        onCheckedChange = { redTorchSimulated = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFFEF4444),
                            checkedTrackColor = Color(0xFF991B1B)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = if (language == AppLanguage.ODIA) "ପ୍ରମୁଖ ନୀଳ ଆଲୋକ ଦର୍ଶନ ସ୍ଥାନ" else "Prime Nocturnal Sighting Spots",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(spots) { spot ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF131D38)),
                        border = BorderStroke(1.dp, Color(0xFF1E293B)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) spot.nameOd else spot.nameEn,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF7DD3FC)
                                    )
                                )
                                Text(
                                    text = "${spot.distanceKm} km",
                                    fontSize = 11.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "🕒 ${spot.bestTimeWindow} • ${spot.substrateType}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.sp,
                                    color = Color(0xFFCBD5E1)
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "💡 ${spot.safetyAdvice}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 10.5.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action: Chandipur Coast Guard / Tourist Police Helpline
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:112")
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("bioluminescence_emergency_call_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == AppLanguage.ODIA) "ଚାନ୍ଦିପୁର ମେରାଇନ୍ ଥାନା ଓ ୧୧୨ ସହାୟତା" else "Call Chandipur Marine Police (112)",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
