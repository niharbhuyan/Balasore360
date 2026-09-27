package com.example.ui.features.coastal

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

/**
 * Subarnarekha River Basin Flood Early Warning & Sluice Gate Telemetry.
 * Tracks Rajghat hydrometric station gauge levels, Jamshedpur dam upstream inflow/release (cusecs),
 * 18 coastal sluice gates in Bhograi, Baliapal & Jaleswar blocks, and ODRAF emergency evacuation shelters.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubarnarekhaFloodSluiceTelemetrySheet(
    dailyPulse: DailyBalasorePulse,
    language: AppLanguage = AppLanguage.ENGLISH,
    onClose: () -> Unit = {}
) {
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .testTag("subarnarekha_flood_sluice_telemetry_sheet")
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
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0F2FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🌊", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ସୁବର୍ଣ୍ଣରେଖା ବନ୍ୟା ଓ ସ୍ଲୁଇସ୍ ଗେଟ୍ ରାଡାର୍" else "Subarnarekha River Flood Radar",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ରାଜଘାଟ ଜଳସ୍ତର (୯.୪୫ ମି. ବିପଦ) ଓ ୧୮ଟି ଉପକୂଳ ସ୍ଲୁଇସ୍ ଗେଟ୍" else "Rajghat Gauge (Danger: 9.45m) & 18 Sluice Gates",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = BentoSlate500,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.testTag("btn_close_flood_telemetry_sheet")
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Gauge Telemetry Banner
            val isAboveDanger = dailyPulse.subarnarekhaRajghatGaugeMeters >= dailyPulse.subarnarekhaDangerMarkMeters
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isAboveDanger) Color(0xFF991B1B) else Color(0xFF0F172A)
                ),
                modifier = Modifier.fillMaxWidth()
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
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (isAboveDanger) Color(0xFFEF4444) else Color(0xFF38BDF8))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "RAJGHAT HYDROLOGICAL GAUGE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isAboveDanger) Color(0xFFFCA5A5) else Color(0xFF38BDF8),
                                    letterSpacing = 1.sp
                                )
                            )
                        }

                        Text(
                            text = if (isAboveDanger) "DANGER LEVEL EXCEEDED ⚠️" else "SAFE WATER BAND 🟢",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Current Gauge", fontSize = 11.sp, color = Color(0xFF94A3B8))
                            Text(
                                text = "${"%.2f".format(dailyPulse.subarnarekhaRajghatGaugeMeters)} m",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Column {
                            Text(text = "Danger Level", fontSize = 11.sp, color = Color(0xFF94A3B8))
                            Text(
                                text = "${dailyPulse.subarnarekhaDangerMarkMeters} m",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF87171)
                            )
                        }
                        Column {
                            Text(text = "Upstream Release", fontSize = 11.sp, color = Color(0xFF94A3B8))
                            Text(
                                text = "${dailyPulse.subarnarekhaUpstreamReleaseCusecs} cusecs",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFBAE6FD)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth().weight(1f, fill = false)
            ) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                        border = BorderStroke(1.dp, Color(0xFFBBF7D0))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🚪", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଭୋଗରାଇ ଓ ବାଲିଆପାଳ ସ୍ଲୁଇସ୍ ଗେଟ୍ ସ୍ଥିତି" else "Bhograi & Baliapal Sluice Gates Status",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Active Sluice Gates Operating: ${dailyPulse.bhograiActiveSluiceGatesCount}/18. Gates open at low tide to drain tidal backwaters into the Bay of Bengal, automatically locking during high-water surges.",
                                style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate600, fontSize = 12.sp)
                            )
                        }
                    }
                }

                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                        border = BorderStroke(1.dp, Color(0xFFBFDBFE))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = if (language == AppLanguage.ODIA) "ଓଡ୍ରାଫ୍ ଓ ଜିଲ୍ଲା ଜରୁରୀକାଳୀନ ନିୟନ୍ତ୍ରଣ କକ୍ଷ" else "ODRAF & District Disaster Control Room",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "• Balasore District Emergency Operations Center (DEOC): 06782-262286\n• ODRAF Rapid Response Team: 06782-261200\n• State Disaster Management (OSDMA): 1077 (Toll Free)",
                                style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate700, fontSize = 12.sp, lineHeight = 18.sp)
                            )
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1077"))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.weight(1f).testTag("btn_call_flood_1077"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Disaster SOS 1077", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                val gmmIntentUri = Uri.parse("geo:21.8000,87.1800?q=Rajghat+River+Gauge+Balasore")
                                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                context.startActivity(mapIntent)
                            },
                            modifier = Modifier.weight(1f).testTag("btn_navigate_rajghat"),
                            colors = ButtonDefaults.buttonColors(containerColor = OceanBlue),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Rajghat Gauge", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(18.dp)) }
            }
        }
    }
}
