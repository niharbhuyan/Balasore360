package com.example.ui.features.nature

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.NaturePeople
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VolumeUp
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
 * Kuldiha Elephant Corridor Early-Warning Sentinel & Eco-Safari Companion.
 * Tracks elephant herd movement sectors along the Nilagiri-Kuldiha-Similipal fringe,
 * issues safety advisories for forest fringe villages, and manages safari checkpost info.
 */
@Composable
fun KuldihaElephantCorridorSentinelSheet(
    dailyPulse: DailyBalasorePulse,
    language: AppLanguage = AppLanguage.ENGLISH,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
            .testTag("kuldiha_elephant_corridor_sentinel_sheet")
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
                    color = Color(0xFF059669).copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, Color(0xFF059669)),
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🐘", fontSize = 20.sp)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (language == AppLanguage.ODIA) "କୁଲଡ଼ିହା ହାତୀ କରିଡର ଓ ସଫାରି" else "Kuldiha Elephant Sentinel",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = "Nilagiri Forest Division • Wildlife Early-Warning",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF059669),
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

        // Live Corridor Sighting & Safety Advisory Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (dailyPulse.isElephantFringeSafeNow) Color(0xFFF0FDF4) else Color(0xFFFEF3C7)
            ),
            border = BorderStroke(
                1.5.dp,
                if (dailyPulse.isElephantFringeSafeNow) Color(0xFF10B981) else Color(0xFFF59E0B)
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
                        color = if (dailyPulse.isElephantFringeSafeNow) Color(0xFF059669) else Color(0xFFD97706)
                    ) {
                        Text(
                            text = if (dailyPulse.isElephantFringeSafeNow) "FRINGE TRANSIT SAFE 🟢" else "ELEPHANT ALERT 🟡",
                            color = Color.White,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                    Text("Nilagiri Range", fontSize = 11.sp, color = Color(0xFF065F46))
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = dailyPulse.kuldihaHerdSightingSector,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (dailyPulse.isElephantFringeSafeNow) Color(0xFF065F46) else Color(0xFF92400E)
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (language == AppLanguage.ODIA)
                        "ତେନ୍ତେଇ ଓ ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ପାହାଡ଼ ପାଦଦେଶ ରାସ୍ତାରେ ସନ୍ଧ୍ୟା ପରେ ବାଇକ୍ ଓ ପଦଯାତ୍ରା ପାଇଁ ବନ ବିଭାଗ ସାବଧାନତା ଜାରି କରିଛି।"
                    else
                        "Forest beat guards stationed at Tentei watchtower. Daytime safari corridor open. Night transit on fringe roads restricted.",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF047857), fontSize = 11.5.sp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Safari Permits & Checkpost Status
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Kuldiha Wildlife Sanctuary Eco-Safari Desk", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF059669).copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, Color(0xFF059669).copy(alpha = 0.3f)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Safari Slots Available", fontSize = 10.sp, color = Color(0xFF059669))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("${dailyPulse.kuldihaSafariSlotsAvailable} Jeeps", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF065F46))
                            Text("Gohirabhani Gate 🟢", fontSize = 9.5.sp, color = Color(0xFF047857))
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF0284C7).copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, Color(0xFF0284C7).copy(alpha = 0.3f)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Timings & Entry", fontSize = 10.sp, color = Color(0xFF0284C7))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("06:00 - 16:30", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0369A1))
                            Text("Eco-Cottage Booking Open", fontSize = 9.5.sp, color = Color(0xFF0284C7))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Forest Beat Officer & Conflict Mitigation SOS
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Forest Division Emergency Contacts", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Kuldiha Forest Range Officer", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("Nilagiri Wildlife Division Desk", fontSize = 10.sp, color = Color(0xFF64748B))
                    }
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:06782-233215"))
                            context.startActivity(intent)
                        },
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("06782-233215", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Done / Close Elephant Sentinel", fontWeight = FontWeight.Bold)
        }
    }
}
