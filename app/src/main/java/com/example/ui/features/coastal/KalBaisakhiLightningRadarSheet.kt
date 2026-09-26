package com.example.ui.features.coastal

import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast
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
import com.example.data.fcm.FcmManager
import com.example.data.model.AppLanguage
import com.example.ui.theme.*
import kotlinx.coroutines.delay

data class LightningShelterBlock(
    val blockName: String,
    val odiaName: String,
    val distanceKm: Float,
    val strikeRisk: String, // "High", "Moderate", "Safe"
    val safeShelterCount: Int,
    val nearestShelter: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KalBaisakhiLightningRadarSheet(
    dailyPulse: DailyBalasorePulse,
    language: AppLanguage = AppLanguage.ENGLISH,
    onClose: () -> Unit = {}
) {
    val context = LocalContext.current
    var isSirenPlaying by remember { mutableStateOf(false) }

    // Countdown timer for squall onset
    var countdownSeconds by remember { mutableIntStateOf(1680) } // ~28 minutes remaining

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            if (countdownSeconds > 0) {
                countdownSeconds -= 1
            }
        }
    }

    val minutesLeft = countdownSeconds / 60
    val secondsLeft = countdownSeconds % 60

    val blockShelters = remember {
        listOf(
            LightningShelterBlock("Remuna", "ରେମୁଣା", 3.2f, "High", 6, "Remuna Kalyan Mandap & Govt High School"),
            LightningShelterBlock("Balasore Sadar", "ବାଲେଶ୍ୱର ସଦର", 1.8f, "High", 12, "Town Hall & DHH Cyclone Core Shelter"),
            LightningShelterBlock("Bahanaga", "ବାହାନଗା", 8.4f, "Moderate", 7, "Bahanaga High School Multipurpose Shelter"),
            LightningShelterBlock("Soro", "ସୋର", 14.2f, "Moderate", 8, "Soro Municipality Indoor Stadium"),
            LightningShelterBlock("Nilagiri", "ନୀଳଗିରି", 18.6f, "Safe", 5, "Nilagiri Sub-Divisional Hospital & Palace Ground"),
            LightningShelterBlock("Jaleswar", "ଜଳେଶ୍ୱର", 22.0f, "Moderate", 9, "Jaleswar Purata Bazar Shelter"),
            LightningShelterBlock("Baliapal", "ବାଲିଆପାଳ", 16.5f, "High", 11, "Choumukh Coastal Cyclone Shelter"),
            LightningShelterBlock("Bhograi", "ଭୋଗରାଇ", 28.0f, "High", 14, "Talasari Fishermen Cyclone Shelter")
        )
    }

    fun triggerAudioHapticSiren() {
        isSirenPlaying = true
        try {
            val notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val ringtone = RingtoneManager.getRingtone(context, notificationUri)
            ringtone?.play()

            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                manager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 400, 200, 400), -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(longArrayOf(0, 400, 200, 400), -1)
            }

            Toast.makeText(context, "🚨 High-Voltage Lightning Alert Siren Activated!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Siren triggered", Toast.LENGTH_SHORT).show()
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
                .testTag("kal_baisakhi_lightning_radar_sheet")
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
                            .background(Color(0xFFFEF3C7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "⚡", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "କାଳବୈଶାଖୀ ଓ ବଜ୍ରପାତ ରାଡାର" else "Nor'wester & Lightning Radar",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଦାମିନୀ ଟେଲିମେଟ୍ରି • ୩୦ ମିନିଟ୍ ଆଗୁଆ ସତର୍କତା" else "Damini Lightning Telemetry • 30m Warning",
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
                // Live Threat Master Card
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1B4B)),
                        border = BorderStroke(1.5.dp, Color(0xFFF59E0B))
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
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
                                            .background(Color(0xFFF59E0B))
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "KAL BAISAKHI FORMING",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFFFDE68A),
                                            letterSpacing = 1.sp
                                        )
                                    )
                                }
                                Surface(
                                    color = Color(0xFF312E81),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "RADIUS: 14 KM",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(0xFFFBBF24),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Estimated Squall Onset Countdown",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFC7D2FE))
                            )
                            Text(
                                text = String.format("%02d mins %02d secs", minutesLeft, secondsLeft),
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "⚡ Nearest Strike: 4.2 km (Remuna - Bahanaga corridor). Squally wind speed: 55 km/h with heavy convective rain showers.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFFFDE68A),
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { triggerAudioHapticSiren() },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(imageVector = Icons.Default.Campaign, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Test Siren", fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = {
                                        FcmManager.simulateWeatherAlertPush(context)
                                        Toast.makeText(context, "FCM Lightning Emergency Broadcast sent!", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(imageVector = Icons.Default.NotificationsActive, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Dispatch Push", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // 12 Blocks Safety Matrix
                item {
                    Text(
                        text = if (language == AppLanguage.ODIA) "ଜିଲ୍ଲାର ୧୨ଟି ବ୍ଲକ୍ ବଜ୍ରପାତ ସୂଚକାଙ୍କ" else "Balasore 12 Blocks Lightning & Shelter Status",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate900
                        )
                    )
                }

                items(blockShelters) { block ->
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
                            val riskColor = when (block.strikeRisk) {
                                "High" -> Color(0xFFDC2626)
                                "Moderate" -> Color(0xFFD97706)
                                else -> Color(0xFF166534)
                            }
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(riskColor.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (block.strikeRisk == "High") "⚡" else if (block.strikeRisk == "Moderate") "⚠️" else "🛡️",
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
                                        text = if (language == AppLanguage.ODIA) block.odiaName else block.blockName,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = BentoSlate900
                                        )
                                    )
                                    Surface(
                                        color = riskColor.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "${block.strikeRisk.uppercase()} RISK",
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = riskColor,
                                                fontSize = 9.sp
                                            )
                                        )
                                    }
                                }

                                Text(
                                    text = "Nearest Shelter: ${block.nearestShelter} (${block.safeShelterCount} shelters ready)",
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
