package com.example.ui.features.civic

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.data.model.AppLanguage
import com.example.ui.theme.*

data class CivicFeederStatus(
    val areaName: String,
    val areaNameOd: String,
    val powerStatus: String, // "Active Normal 🟢", "Scheduled Cut ⚠️", "Emergency Repair 🔴"
    val powerCutWindow: String?,
    val waterSupplyTime: String,
    val subStation: String,
    val linemanPhone: String
)

val BALASORE_CIVIC_FEEDERS = listOf(
    CivicFeederStatus(
        areaName = "Motiganj & Gopalgaon (Ward 7, 8, 9)",
        areaNameOd = "ମୋତିଗଞ୍ଜ ଓ ଗୋପାଳଗାଁ (ୱାର୍ଡ ୭, ୮, ୯)",
        powerStatus = "Active Normal 🟢",
        powerCutWindow = null,
        waterSupplyTime = "06:00 - 08:15 AM & 05:30 - 07:00 PM",
        subStation = "Motiganj 33/11kV Substation",
        linemanPhone = "9437190123"
    ),
    CivicFeederStatus(
        areaName = "Angargadia & ITI Area (Ward 14, 15)",
        areaNameOd = "ଅଙ୍ଗାରଗଡ଼ିଆ ଓ ଆଇଟିଆଇ (ୱାର୍ଡ ୧୪, ୧୫)",
        powerStatus = "Active Normal 🟢",
        powerCutWindow = null,
        waterSupplyTime = "06:30 - 08:30 AM & 06:00 - 07:30 PM",
        subStation = "Angargadia Feeder No. 2",
        linemanPhone = "9438221190"
    ),
    CivicFeederStatus(
        areaName = "Sahadevkhunta & Station Road (Ward 21, 22)",
        areaNameOd = "ସହଦେବଖୁଣ୍ଟା ଓ ଷ୍ଟେସନ ରୋଡ୍",
        powerStatus = "Active Normal 🟢",
        powerCutWindow = null,
        waterSupplyTime = "05:45 - 08:00 AM & 05:00 - 07:00 PM",
        subStation = "Sahadevkhunta Main Grid",
        linemanPhone = "9861044231"
    ),
    CivicFeederStatus(
        areaName = "Remuna Gopinath & Januganj",
        areaNameOd = "ରେମୁଣା ଗୋପୀନାଥ ଓ ଜାନୁଗଞ୍ଜ",
        powerStatus = "Scheduled Cut ⚠️",
        powerCutWindow = "Tomorrow 09:30 AM - 12:30 PM (Line Clearing)",
        waterSupplyTime = "06:00 - 08:00 AM & 05:30 - 07:15 PM",
        subStation = "Remuna 11kV Feeder",
        linemanPhone = "9437332211"
    ),
    CivicFeederStatus(
        areaName = "Kuruda NOCCI & Industrial Area",
        areaNameOd = "କୁରୁଡ଼ା ନୋସି ଓ ଶିଳ୍ପାଞ୍ଚଳ",
        powerStatus = "Active Normal 🟢",
        powerCutWindow = null,
        waterSupplyTime = "24h Industrial Dedicated + 06:00 AM Municipal",
        subStation = "Kuruda Industrial Substation",
        linemanPhone = "9938091823"
    )
)

/**
 * TPNODL Power & WATCO Water Municipal Monitor.
 * Live power status, scheduled maintenance windows, piped municipal water release hours,
 * and direct lineman helpline per Balasore ward.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TpnodlWatcoMonitorSheet(
    language: AppLanguage,
    onClose: () -> Unit
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
                .testTag("tpnodl_watco_monitor_sheet")
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
                            text = if (language == AppLanguage.ODIA) "ବିଦ୍ୟୁତ୍ (TPNODL) ଓ ଜଳ (WATCO) ସୂଚୀ" else "TPNODL Power & WATCO Water",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଲାଇଭ୍ ବିଦ୍ୟୁତ୍ କାଟ୍ ସୂଚନା • ପାନୀୟ ଜଳ ଯୋଗାଣ ସମୟ" else "Feeder Shutdown Alerts • Tap Water Schedules",
                            style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate500)
                        )
                    }
                }
                IconButton(onClick = onClose) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.88f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Emergency Helplines
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1912"))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("1912 Power Help", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:06782262118"))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = OceanBlue),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.WaterDrop, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("WATCO Tanker", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Grid Status Feeders
                items(BALASORE_CIVIC_FEEDERS) { feeder ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = feeder.subStation,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(0xFFD97706),
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        text = if (language == AppLanguage.ODIA) feeder.areaNameOd else feeder.areaName,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = BentoSlate900
                                        )
                                    )
                                }

                                Surface(
                                    color = if (feeder.powerCutWindow != null) Color(0xFFFEF3C7) else Color(0xFFDCFCE7),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = feeder.powerStatus,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            color = if (feeder.powerCutWindow != null) Color(0xFFB45309) else Color(0xFF15803D)
                                        )
                                    )
                                }
                            }

                            if (feeder.powerCutWindow != null) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Surface(
                                    color = Color(0xFFFFFBEB),
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(1.dp, Color(0xFFFDE68A))
                                ) {
                                    Text(
                                        text = "⚠️ Notice: ${feeder.powerCutWindow}",
                                        modifier = Modifier.padding(8.dp),
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = Color(0xFF92400E),
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "🚰 WATCO Tap Supply: ${feeder.waterSupplyTime}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF0369A1),
                                    fontWeight = FontWeight.SemiBold
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Feeder Lineman on Duty",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = BentoSlate500,
                                        fontSize = 11.sp
                                    )
                                )

                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${feeder.linemanPhone}"))
                                        context.startActivity(intent)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F766E)),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Call Lineman", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}
