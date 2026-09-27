package com.example.ui.features.nature

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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

data class EcoCorridorBeat(
    val rangeNameEn: String,
    val rangeNameOd: String,
    val elephantStatus: String,
    val forestFireRisk: String,
    val rangeOfficerHelpline: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KuldihaEcoCorridorFireRadarSheet(
    language: AppLanguage,
    dailyPulse: DailyBalasorePulse,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    val beats = listOf(
        EcoCorridorBeat(
            rangeNameEn = "Tinikosia - Gohirabhani Fringe Beat",
            rangeNameOd = "ତିନିକୋଶିଆ - ଗୋହିରାଭାଲି ସୀମା ବିଟ୍",
            elephantStatus = "Herd of 14 elephants foraging near stream 🐘",
            forestFireRisk = "Low (Moist Deciduous 🟢)",
            rangeOfficerHelpline = "+919437012345"
        ),
        EcoCorridorBeat(
            rangeNameEn = "Jadachua Watchtower Core Zone",
            rangeNameOd = "ଜଡ଼ାଚୁଆ ୱାଚଟାୱାର୍ କେନ୍ଦ୍ରାଞ୍ଚଳ",
            elephantStatus = "Salt lick active • Safe viewing from tower",
            forestFireRisk = "Zero Thermal Anomaly 🟢",
            rangeOfficerHelpline = "+919437067890"
        ),
        EcoCorridorBeat(
            rangeNameEn = "Rissia Dam Buffer Beat",
            rangeNameOd = "ରିଷିଆ ଡ୍ୟାମ୍ ବଫର୍ ଜୋନ୍ ବିଟ୍",
            elephantStatus = "Evening waterhole crossing expected (05:00 PM)",
            forestFireRisk = "Normal Canopy Moisture 🟢",
            rangeOfficerHelpline = "+919437098765"
        ),
        EcoCorridorBeat(
            rangeNameEn = "Nilagiri Forest Foothill Beat",
            rangeNameOd = "ନୀଳଗିରି ପାଦଦେଶ ଜଙ୍ଗଲ ବିଟ୍",
            elephantStatus = "Solar anti-depredation trench operational",
            forestFireRisk = "Watchman Patrol Active 🟢",
            rangeOfficerHelpline = "+919437011223"
        )
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .testTag("kuldiha_corridor_fire_sheet")
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
                            .background(Color(0xFFECFDF5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Forest,
                            contentDescription = "Corridor Icon",
                            tint = Color(0xFF059669),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "କୁଲଡିହା କରିଡର ଓ ବନାଗ୍ନି ରାଡାର୍" else "Kuldiha Corridor & Fire Radar",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ହାତୀ ଚଳାଚଳ ସତର୍କତା ଓ ଉପଗ୍ରହ ବନାଗ୍ନି ସୂଚନା" else "Elephant Tracking & Satellite Thermal Watch",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = BentoSlate600,
                                fontSize = 11.5.sp
                            )
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("kuldiha_radar_close_btn")
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = BentoSlate600)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Status Banner
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ସାମ୍ପ୍ରତିକ ହାତୀ କରିଡର ସ୍ଥିତି" else "Current Elephant Movement",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF166534)
                            )
                        )
                        Text(
                            text = "🟢 CANOPY STABLE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF15803D)
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = dailyPulse.kuldihaElephantMovementSector,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF14532D)
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "🛰️ Satellite Thermal Watch: ${dailyPulse.kuldihaForestFireRisk}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.5.sp,
                            color = Color(0xFF166534)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = if (language == AppLanguage.ODIA) "କୁଲଡିହା ବନ ବିଟ୍ ସୂଚୀ ଓ ଫରେଷ୍ଟର ହେଲ୍ପଲାଇନ" else "Kuldiha Forest Beats & Ranger Hotlines",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = BentoSlate800
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(beats) { beat ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = BorderStroke(1.dp, BentoSlate200),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) beat.rangeNameOd else beat.rangeNameEn,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = BentoSlate900
                                    )
                                )
                                IconButton(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL).apply {
                                            data = Uri.parse("tel:${beat.rangeOfficerHelpline}")
                                        }
                                        context.startActivity(intent)
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Call,
                                        contentDescription = "Call Ranger",
                                        tint = Color(0xFF059669),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = beat.elephantStatus,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.sp,
                                    color = BentoSlate700
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "🔥 Fire Risk: ${beat.forestFireRisk}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 10.5.sp,
                                    color = BentoSlate600
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:1926") // Odisha Forest Helpline
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("forest_toll_free_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == AppLanguage.ODIA) "ଓଡ଼ିଶା ବନ୍ୟପ୍ରାଣୀ ହେଲ୍ପଲାଇନ୍ (୧୯୨୬)" else "Odisha Wildlife Helpline (1926)",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
