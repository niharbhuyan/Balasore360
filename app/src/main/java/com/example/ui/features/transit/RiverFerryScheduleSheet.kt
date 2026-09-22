package com.example.ui.features.transit

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Water
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
import androidx.compose.runtime.remember
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
import com.example.data.daily.ExtendedDailyUpdateEngine
import com.example.ui.components.openInGoogleMaps

/**
 * 5. Subarnarekha & Budhabalanga Passenger Ferry Schedule Sheet
 * Real-time tidal crossability, operational country-craft ghats, and standard passenger rates.
 */
@Composable
fun RiverFerryScheduleSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val pulse = remember { ExtendedDailyUpdateEngine.computeExtendedPulse().riverFerryPulse }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag("river_ferry_schedule_sheet")
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
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF0D9488), Color(0xFF0F766E))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("⛴️", fontSize = 22.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "River Ferry & Ghat Schedule",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "ନଦୀ ଡଙ୍ଗା ଘାଟ ଓ ନୌକା ଚଳାଚଳ ସମୟସାରଣୀ",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF0D9488),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Daily Auto-Updated Telemetry Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDFA)),
            border = BorderStroke(1.dp, Color(0xFF99F6E4)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF0D9488).copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "LIVE GHAT TELEMETRY",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF0D9488),
                                letterSpacing = 0.8.sp
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = pulse.tidalCurrentCrossability,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D9488)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Subarnarekha Crossing: ${pulse.subarnarekhaFerryStatus}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Budhabalanga Creek: ${pulse.budhabalangaFerryStatus}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF334155),
                        fontWeight = FontWeight.Medium
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Pedestrian Fare", style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B))
                        Text(pulse.standardPedestrianFare, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color(0xFF0D9488))
                    }
                    Column {
                        Text("2-Wheeler Fare", style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B))
                        Text(pulse.twoWheelerFare, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Ghat Network Details
        Text(
            text = "Active Estuarine Ghat Points",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "For coastal residents and travelers between Baliapal, Bhograi, and Balasore town, country wooden boats provide short cuts cutting road detour by up to 28 km.",
            style = MaterialTheme.typography.bodySmall.copy(
                lineHeight = 19.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DirectionsBoat, contentDescription = null, tint = Color(0xFF0D9488), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Operational River Ghats", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = pulse.activeCrossingGhats,
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f)
            ) {
                Text("Close")
            }
            Button(
                onClick = {
                    openInGoogleMaps(
                        context = context,
                        latitude = 21.5600,
                        longitude = 87.3200,
                        label = "Chaumukh River Ferry Ghat, Subarnarekha, Balasore"
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D9488)),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Chaumukh Ghat")
            }
        }
    }
}
