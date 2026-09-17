package com.example.ui.features.coastal

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.WbSunny
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
import com.example.data.daily.DailyUpdateEngine
import com.example.ui.components.openInGoogleMaps

/**
 * Kasafal & Dublagadi Red Ghost Crab Trail Component with daily auto-updated tide window.
 */
@Composable
fun KasafalRedCrabsSheet(
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dailyPulse = remember { DailyUpdateEngine.getDailyPulse() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("kasafal_red_crabs_sheet")
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
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFFE11D48), Color(0xFFBE123C))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🦀", fontSize = 22.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Kasafal Red Crab Trail",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "କାସାଫଳ ଓ ଦୁବଳାଗଡ଼ି ଲାଲ କଙ୍କଡ଼ା ସୈକତ",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFBE123C),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFFFE4E6)
            ) {
                Text(
                    text = "Daily Auto-Tide",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF9F1239)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Live Today Emergence Status Card (Daily Auto Updated)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (dailyPulse.isRedCrabWindowActive) Color(0xFFFFF1F2) else Color(0xFFF8FAFC)
            ),
            border = BorderStroke(
                1.5.dp,
                if (dailyPulse.isRedCrabWindowActive) Color(0xFFFDA4AF) else Color(0xFFE2E8F0)
            )
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = if (dailyPulse.isRedCrabWindowActive) Color(0xFFE11D48) else Color(0xFF64748B),
                            modifier = Modifier.size(8.dp)
                        ) {}
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (dailyPulse.isRedCrabWindowActive) "ACTIVE EMERGENCE WINDOW" else "RESTING AT HIGH TIDE",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = if (dailyPulse.isRedCrabWindowActive) Color(0xFFBE123C) else Color(0xFF475569)
                        )
                    }

                    Text(
                        text = dailyPulse.formattedDate,
                        fontSize = 10.sp,
                        color = Color(0xFF64748B)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = dailyPulse.crabWindowStatusMessage,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1E293B)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, Color(0xFFFECDD3)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🌅 Morning Window", fontSize = 10.sp, color = Color(0xFF64748B))
                            Text(dailyPulse.redCrabMorningWindow, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFBE123C))
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, Color(0xFFFECDD3)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🌇 Sunset Window", fontSize = 10.sp, color = Color(0xFF64748B))
                            Text(dailyPulse.redCrabEveningWindow, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFBE123C))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // About the Phenomenon Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "The Red Carpet of Dublagadi Sands",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Located 42 km east of Balasore, Kasafal and Dublagadi virgin beaches host billions of scarlet red ghost crabs (Ocypode macrocera) and fiddler crabs. As the intertidal seawater recedes during low tide, the crabs exit their deep cylindrical burrows to feed on micro-algae, transforming kilometers of golden sand into an undulating scarlet-crimson carpet.",
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp, color = MaterialTheme.colorScheme.onSurface)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Photography & Eco-Etiquette Guide
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
            border = BorderStroke(1.dp, Color(0xFFBBF7D0))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Eco, null, tint = Color(0xFF166534), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Eco-Etiquette & Photography Protocol",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFF166534)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("• Silent Tread: Walk gently along the wet shoreline; ground vibrations cause entire colonies to retreat into burrows in seconds.", fontSize = 11.sp, color = Color(0xFF14532D))
                Spacer(modifier = Modifier.height(4.dp))
                Text("• Low-Angle Macro: Crouch low at ground level with a telephoto/macro lens (70-200mm) for mesmerizing high-contrast shots against blue ocean spray.", fontSize = 11.sp, color = Color(0xFF14532D))
                Spacer(modifier = Modifier.height(4.dp))
                Text("• Zero Plastic & No Trampling: Never step on burrow sand-balls or leave debris. This is a fragile nesting estuary.", fontSize = 11.sp, color = Color(0xFF14532D))
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Local Village Guide & Navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:9437198765") // Kasafal Eco-Tourism Committee
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier.weight(1f).height(42.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Call, null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Local Eco Guide", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = {
                    openInGoogleMaps(context, 21.4682, 87.1124, "Kasafal Beach Red Crab Colony")
                },
                modifier = Modifier.weight(1f).height(42.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Navigation, null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Directions (42 km)", fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Done with Red Crab Trail", fontWeight = FontWeight.Bold)
        }
    }
}
