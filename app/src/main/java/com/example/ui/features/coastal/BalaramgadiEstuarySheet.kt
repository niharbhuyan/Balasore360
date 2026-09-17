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
import androidx.compose.material.icons.filled.Anchor
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WbTwilight
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.openInGoogleMaps

/**
 * Balaramgadi Estuary & Budhabalanga River Confluence Guide Sheet.
 */
@Composable
fun BalaramgadiEstuarySheet(
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("balaramgadi_estuary_sheet")
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
                                listOf(Color(0xFF0284C7), Color(0xFF0369A1))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("⛵", fontSize = 22.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Balaramgadi River Estuary",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "ବାଳରାମଗଡ଼ି ବୁଢ଼ାବଳଙ୍ଗ ନଦୀ-ସମୁଦ୍ର ମୁହାଣ",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF0284C7),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFE0F2FE)
            ) {
                Text(
                    text = "3 km from Chandipur",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0369A1)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Overview Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Where River Budhabalanga Meets the Bay of Bengal",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Balaramgadi is the dynamic confluence where the sacred Budhabalanga river discharges into the Bay of Bengal. Hundreds of traditional wooden mechanized trawlers dock here along the mangrove sandbars, creating a dramatic maritime spectacle of colorful flags, drying nets, and sea birds.",
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp, color = MaterialTheme.colorScheme.onSurface)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Trawler Schedule & Boat Cruise Rates
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)),
            border = BorderStroke(1.dp, Color(0xFFBAE6FD))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Anchor, null, tint = Color(0xFF0284C7), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Trawler Operations & Country Boat Hire",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFF0369A1)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("• Morning Trawler Return: 05:30 AM - 07:30 AM (Highest activity; fish unloading onto harbor docks).", fontSize = 11.sp, color = Color(0xFF075985))
                Spacer(modifier = Modifier.height(4.dp))
                Text("• Afternoon Deep-Sea Departure: 02:00 PM - 04:30 PM (Trawlers set sail aligned with incoming tide).", fontSize = 11.sp, color = Color(0xFF075985))
                Spacer(modifier = Modifier.height(4.dp))
                Text("• Regulated Estuary Boat Cruise: ₹200 - ₹350 per boat (30-45 min tour along the mangrove creek and sandbar mouth). Life jackets mandatory.", fontSize = 11.sp, color = Color(0xFF075985))
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Photography & Sunset Vantage Points
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
            border = BorderStroke(1.dp, Color(0xFFFDE68A))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.WbTwilight, null, tint = Color(0xFFD97706), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Golden Hour Photography Spots (04:45 PM - 06:15 PM)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFF92400E)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("1. Estuary Sandbar Point: Panoramic view of the river mouth opening to the horizon with silhouettes of incoming trawlers.", fontSize = 11.sp, color = Color(0xFF78350F))
                Spacer(modifier = Modifier.height(4.dp))
                Text("2. Mangrove Creek Wooden Jetty: Peaceful reflection shots of colorful traditional country boats and migratory egrets.", fontSize = 11.sp, color = Color(0xFF78350F))
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Actions: Boat Union Call & Google Maps
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:9437298123") // Balaramgadi Fishermen & Boatmen Union
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier.weight(1f).height(42.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Call, null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Boatmen Union", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = {
                    openInGoogleMaps(context, 21.4920, 87.0540, "Balaramgadi River Estuary & Fish Harbor")
                },
                modifier = Modifier.weight(1f).height(42.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Navigation, null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Navigate (3 km)", fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Done with Balaramgadi Estuary", fontWeight = FontWeight.Bold)
        }
    }
}
