package com.example.ui.features.nature

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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Visibility
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
 * 3. Kuldiha Elephant Corridor Advisory & Sanctuary Watchtower Radar Sheet
 * Real-time corridor alert level, night transit advisories, and watchtower viewing windows.
 */
@Composable
fun KuldihaElephantCorridorSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val pulse = remember { ExtendedDailyUpdateEngine.computeExtendedPulse().kuldihaCorridorPulse }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag("kuldiha_elephant_corridor_sheet")
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
                                listOf(Color(0xFF15803D), Color(0xFF166534))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🐘", fontSize = 22.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Kuldiha Elephant Corridor",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "କୁଲଡିହା ହାତୀ କରିଡର ସୁରକ୍ଷା ଓ ରାଡାର୍",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF15803D),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Daily Auto-Updated Alert Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
            border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
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
                        color = Color(0xFF15803D).copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "SANCTUARY RADAR ADVISORY",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF15803D),
                                letterSpacing = 0.8.sp
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = pulse.activeAlertLevel,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = if (pulse.activeAlertLevel.startsWith("GREEN")) Color(0xFF15803D) else Color(0xFFD97706)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = pulse.corridorSectorAdvisory,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Current Herd Activity: ${pulse.elephantHerdLocation}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF334155),
                        lineHeight = 18.sp
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "Watchtowers",
                        tint = Color(0xFF15803D),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = pulse.watchtowerVisibilityStatus,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF15803D),
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Commuter & Tourist Safety Rules
        Text(
            text = "Human-Elephant Coexistence Safety Protocol",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Kuldiha Wildlife Sanctuary forms a vital migratory link between Similipal Biosphere and Hadgarh Wildlife Sanctuary. When passing through Nilagiri or Oupada forest stretches after 06:00 PM, observe the following rules strictly:",
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
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Speed, contentDescription = null, tint = Color(0xFF15803D), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Strict 25 km/h Speed Limit on Nilagiri-Mitrapur road", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Never use high-beam headlights or sound horn at crossings", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Call, contentDescription = null, tint = Color(0xFF15803D), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Balasore Forest Division Control: 1800-345-6789", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold))
                }
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
                        latitude = 21.4167,
                        longitude = 86.6833,
                        label = "Kuldiha Wildlife Sanctuary Gate, Nilagiri, Balasore"
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF15803D)),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Sanctuary Gate")
            }
        }
    }
}
