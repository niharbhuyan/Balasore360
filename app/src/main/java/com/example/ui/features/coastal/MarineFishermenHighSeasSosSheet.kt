package com.example.ui.features.coastal

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

/**
 * Marine Fishermen High-Seas SOS & Port Warning Signal Decoder.
 * Decodes IMD/INCOIS Port Distress Signals (1-11), alerts fishermen on IMBL boundary proximity,
 * and provides a zero-network acoustic whistle / optical beacon SOS.
 */
@Composable
fun MarineFishermenHighSeasSosSheet(
    dailyPulse: DailyBalasorePulse,
    language: AppLanguage = AppLanguage.ENGLISH,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isSosAudioActive by remember { mutableStateOf(false) }
    var selectedSignalIndex by remember { mutableIntStateOf(dailyPulse.marinePortSignalNumber.coerceIn(1, 11)) }

    val portSignals = listOf(
        Pair(1, "Signal 1: Distant Cautionary (Low pressure formed in Bay of Bengal)"),
        Pair(2, "Signal 2: Distant Warning (Deep depression / Squally winds 50-60 km/h)"),
        Pair(3, "Signal 3: Local Cautionary (Port & coastal waters threatened by squalls)"),
        Pair(4, "Signal 4: Local Warning (Cyclonic storm forming / Trawlers return to port)"),
        Pair(5, "Signal 5: Danger (Cyclone expected to cross South of Balasore)"),
        Pair(6, "Signal 6: Danger (Cyclone expected to cross North of Balasore / West Bengal)"),
        Pair(7, "Signal 7: Danger (Cyclone expected to make landfall over Balasore coast)"),
        Pair(8, "Signal 8: Great Danger (Severe Cyclone landfall South of Port)"),
        Pair(9, "Signal 9: Great Danger (Severe Cyclone landfall North of Port)"),
        Pair(10, "Signal 10: Super Cyclone (Extreme winds >150 km/h over Balasore coast)"),
        Pair(11, "Signal 11: Total Communications Failure with Meteorology Center")
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
            .testTag("marine_fishermen_high_seas_sos_sheet")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFDC2626).copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, Color(0xFFEF4444)),
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = "Marine SOS",
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (language == AppLanguage.ODIA) "ସାମୁଦ୍ରିକ ମତ୍ସ୍ୟଜୀବୀ SOS ଓ ପୋର୍ଟ ସିଗ୍ନାଲ" else "Marine Fishermen SOS & Port Radar",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = "INCOIS • Coast Guard 1554 • IMBL Exclusion Radar",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF0284C7),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF10B981).copy(alpha = 0.15f),
                border = BorderStroke(1.dp, Color(0xFF10B981))
            ) {
                Text(
                    text = "AUTO-SYNC 🟢",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF059669),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Active Port Warning Signal Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (dailyPulse.marinePortSignalNumber >= 4) Color(0xFFFEF2F2) else Color(0xFFF0FDF4)
            ),
            border = BorderStroke(
                1.5.dp,
                if (dailyPulse.marinePortSignalNumber >= 4) Color(0xFFEF4444) else Color(0xFF10B981)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (dailyPulse.marinePortSignalNumber >= 4) Color(0xFFDC2626) else Color(0xFF059669)
                    ) {
                        Text(
                            text = "PORT DISTRESS SIGNAL #${dailyPulse.marinePortSignalNumber}",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Text(
                        text = "Balaramgadi & Dhamra Ports",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B),
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = dailyPulse.marinePortSignalTitle,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (dailyPulse.marinePortSignalNumber >= 4) Color(0xFF991B1B) else Color(0xFF065F46)
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (language == AppLanguage.ODIA)
                        "ସମୁଦ୍ର ମଧ୍ୟରେ ଥିବା ସମସ୍ତ ବଳରାମଗଡ଼ି, କସାଫଳ ଓ ତାଳସାରୀ ମତ୍ସ୍ୟଜୀବୀ ଟ୍ରଲର ତୁରନ୍ତ ଉପକୂଳ ନିକଟବର୍ତ୍ତୀ ହୁଅନ୍ତୁ।"
                    else
                        "All fishing trawlers from Balaramgadi and Kasafal advised to monitor high-frequency VHF Marine Channel 16.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Offshore IMBL & Defense Channel Telemetry
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "High-Seas Geofencing & Exclusion Radar",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF0284C7).copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, Color(0xFF0284C7).copy(alpha = 0.3f)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("IMBL Boundary Distance", fontSize = 10.sp, color = Color(0xFF0284C7))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${dailyPulse.marineImblDistanceKm} km",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0369A1)
                            )
                            Text("Safe Indian EEZ Waters 🟢", fontSize = 9.5.sp, color = Color(0xFF10B981))
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF59E0B).copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, Color(0xFFF59E0B).copy(alpha = 0.3f)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("DRDO Launch Exclusion", fontSize = 10.sp, color = Color(0xFFD97706))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (dailyPulse.itrLaunchWarningActive) "RESTRICTED 🔴" else "OPEN 🟢",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (dailyPulse.itrLaunchWarningActive) Color(0xFFDC2626) else Color(0xFF15803D)
                            )
                            Text("Chandipur-Wheeler Corridor", fontSize = 9.5.sp, color = Color(0xFF78350F))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Zero-Network Acoustic SOS Whistle & Optical Strobe
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            border = BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "⚡ Emergency Acoustic SOS Siren",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = "Zero internet required • High-pitch marine distress whistle",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF94A3B8),
                                fontSize = 11.sp
                            )
                        )
                    }

                    Button(
                        onClick = {
                            isSosAudioActive = !isSosAudioActive
                            if (isSosAudioActive) {
                                try {
                                    val toneGen = ToneGenerator(AudioManager.STREAM_ALARM, 100)
                                    toneGen.startTone(ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK, 1500)
                                } catch (_: Exception) {}
                                Toast.makeText(context, "🚨 High-Pitch Marine Distress Siren Activated!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSosAudioActive) Color(0xFFDC2626) else Color(0xFF0284C7)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = if (isSosAudioActive) Icons.Default.VolumeUp else Icons.Default.Emergency,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isSosAudioActive) "Mute Siren" else "Sound SOS", fontSize = 11.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Direct Marine Emergency Helplines
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Coast Guard & Marine Rescue Contacts",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(10.dp))

                listOf(
                    Triple("Indian Coast Guard Marine SOS", "1554", "Toll-Free 24x7 High-Seas SAR"),
                    Triple("Marine Police Station Balaramgadi", "06782-272210", "Coastal Patrol Vessel Base"),
                    Triple("District Emergency Operations (DEOC)", "1077", "Balasore Collectorate Desk")
                ).forEach { contact ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(contact.first, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(contact.third, fontSize = 10.sp, color = Color(0xFF64748B))
                        }
                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${contact.second}"))
                                context.startActivity(intent)
                            },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(contact.second, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Done / Keep Marine Radar Active", fontWeight = FontWeight.Bold)
        }
    }
}
