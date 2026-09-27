package com.example.ui.features.resilience

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.daily.DailyBalasorePulse
import com.example.data.model.AppLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TpnodlPowerOutageRadarSheet(
    language: AppLanguage,
    dailyPulse: DailyBalasorePulse,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    val feederStatusList = listOf(
        FeederItem("Feeder #1 (Balasore Town Central - 33kV)", "Town Substation", "NORMAL 🟢", "All wards stable; 230V voltage output."),
        FeederItem("Feeder #2 (FM MCH & Remuna Institutional)", "Remuna Substation", "PRIORITY NORMAL 🟢", "Dedicated hospital dual-line feeder active."),
        FeederItem("Feeder #3 (Chandipur Defence & Port Corridor)", "Chandipur Substation", "NORMAL 🟢", "Underground cabling shielded from salt fog."),
        FeederItem("Feeder #4 (Basta & Jaleswar Rural Line)", "Jaleswar Grid", if (dailyPulse.tpnodlOutageFeedersCount > 0) "MAINTENANCE 🟡" else "NORMAL 🟢", "Scheduled conductor stringing & tree branch trimming."),
        FeederItem("Feeder #5 (Bhograi Coastal Saline Sector)", "Bhograi Substation", "NORMAL 🟢", "High-tension sea wind insulators active.")
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
                            text = if (language == AppLanguage.ODIA) "TPNODL ବିଦ୍ୟୁତ୍ ସରବରାହ ଓ କଟ୍ ରାଡାର୍ ⚡" else "TPNODL Power Outage & Grid Radar ⚡",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD97706)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "୩୩/୧୧ କେଭି ସବ୍-ଷ୍ଟେସନ୍ ସ୍ଥିତି, ଟ୍ରାନ୍ସଫର୍ମର ମରାମତି ଓ ୨୪x୭ ହେଲ୍ପଲାଇନ୍" else "TP Northern Odisha Distribution Ltd Feeder Health & 1912 SOS",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            }

            // Grid Status Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF451A03)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("TPNODL GRID TELEMETRY", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFDE68A))
                            Badge(containerColor = if (dailyPulse.tpnodlOutageFeedersCount > 0) Color(0xFFF59E0B) else Color(0xFF16A34A)) {
                                Text(if (dailyPulse.tpnodlOutageFeedersCount > 0) "${dailyPulse.tpnodlOutageFeedersCount} FEEDER ALERT" else "ALL FEEDERS NORMAL", color = Color.White, fontSize = 10.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(dailyPulse.tpnodlRestorationEta, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1912"))
                                    context.startActivity(dialIntent)
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706))
                            ) {
                                Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Toll-Free 1912")
                            }

                            Button(
                                onClick = {
                                    val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:18003456718"))
                                    context.startActivity(dialIntent)
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF78350F))
                            ) {
                                Icon(Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Balasore Control")
                            }
                        }
                    }
                }
            }

            // Feeders Status List
            item {
                Text(
                    text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ଜିଲ୍ଲା ଫିଡର୍ ସ୍ଥିତି" else "33/11kV Distribution Feeders Status",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            items(feederStatusList) { feeder ->
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
                            Text(feeder.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.weight(1f))
                            Badge(containerColor = MaterialTheme.colorScheme.surfaceVariant) {
                                Text(feeder.status, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Text("Substation: ${feeder.substation}", fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(feeder.remarks, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

private data class FeederItem(
    val name: String,
    val substation: String,
    val status: String,
    val remarks: String
)
