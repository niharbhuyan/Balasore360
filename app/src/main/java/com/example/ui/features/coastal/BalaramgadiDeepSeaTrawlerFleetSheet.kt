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
 * Deep-Sea Mechanized Trawler Fleet & Marine Catch Telemetry at Balaramgadi Fishing Harbor
 * (Budhabalanga River Estuary, Chandipur). Tracks high-seas trawler departures, daily prawn & Hilsa landings,
 * marine diesel subsidies, and VHF sea safety channels.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalaramgadiDeepSeaTrawlerFleetSheet(
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
                .testTag("balaramgadi_deep_sea_trawler_fleet_sheet")
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
                        Text(text = "🍤", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବଳରାମଗଡ଼ି ଗଭୀର ସମୁଦ୍ର ଟ୍ରଲର ଫ୍ଲିଟ୍" else "Balaramgadi Trawler Fleet Radar",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବୁଢ଼ାବଳଙ୍ଗ ମୁହାଣ ମତ୍ସ୍ୟ ବନ୍ଦର ଓ ସାମୁଦ୍ରିକ ଧରା ତଥ୍ୟ" else "Budhabalanga Estuary Fleet & Marine Landings",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = BentoSlate500,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.testTag("btn_close_trawler_sheet")
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Fleet Status Banner
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
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
                                    .background(Color(0xFF38BDF8))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ESTUARY FLEET TELEMETRY",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF38BDF8),
                                    letterSpacing = 1.sp
                                )
                            )
                        }

                        Text(
                            text = "${dailyPulse.balaramgadiActiveTrawlersCount} Trawlers Active",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
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
                            Text(text = "Tiger Prawn Landings", fontSize = 11.sp, color = Color(0xFF94A3B8))
                            Text(text = "${dailyPulse.balaramgadiTigerPrawnLandingKg} kg today", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFED7AA))
                        }
                        Column {
                            Text(text = "Hilsa Sea Catch", fontSize = 11.sp, color = Color(0xFF94A3B8))
                            Text(text = "${dailyPulse.balaramgadiHilsaSeaCatchKg} kg landed", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFBAE6FD))
                        }
                        Column {
                            Text(text = "Marine Fuel Subsidy", fontSize = 11.sp, color = Color(0xFF94A3B8))
                            Text(
                                text = if (dailyPulse.balaramgadiDieselSubsidyActive) "Active 🟢" else "Offline 🔴",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF86EFAC)
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
                                Text(text = "📻", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ସାମୁଦ୍ରିକ ରେଡିଓ ଓ ଜରୁରୀକାଳୀନ ଚ୍ୟାନେଲ" else "Marine VHF & Coast Guard SOS",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Emergency VHF Channel: 16 (Distress Calling) & Channel 68 (Harbor Master). Indian Coast Guard Paradip/Chandipur Station Helpline: 1554.",
                                style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate600, fontSize = 12.sp)
                            )
                        }
                    }
                }

                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                        border = BorderStroke(1.dp, Color(0xFFFDE68A))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = if (language == AppLanguage.ODIA) "ଦୈନିକ ହୋଲସେଲ ମତ୍ସ୍ୟ ନିଲାମ ଦର" else "Harbor Dock Wholesale Spot Rates",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Tiger Prawn (Grade A): ₹${dailyPulse.aquacultureTigerPrawnRateKg}/kg", fontSize = 12.sp, color = BentoSlate700)
                                Text(text = "Vannamei: ₹${dailyPulse.aquacultureVannameiRateKg}/kg", fontSize = 12.sp, color = BentoSlate700)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Hilsa (800g+): ₹1,150/kg", fontSize = 12.sp, color = BentoSlate700)
                                Text(text = "White Pomfret: ₹780/kg", fontSize = 12.sp, color = BentoSlate700)
                            }
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
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1554"))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.weight(1f).testTag("btn_call_coast_guard"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Coast Guard (1554)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                val gmmIntentUri = Uri.parse("geo:21.4644,87.0392?q=Balaramgadi+Fishing+Harbor")
                                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                context.startActivity(mapIntent)
                            },
                            modifier = Modifier.weight(1f).testTag("btn_navigate_balaramgadi"),
                            colors = ButtonDefaults.buttonColors(containerColor = OceanBlue),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Harbor Map", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(18.dp)) }
            }
        }
    }
}
