package com.example.ui.features.agro

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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Thermostat
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
 * Betel Vine ("Pan Baraja") & Agro-Tourism Explorer Sheet.
 */
@Composable
fun PanBarajaAgroSheet(
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("pan_baraja_agro_sheet")
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
                                listOf(Color(0xFF16A34A), Color(0xFF15803D))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🌿", fontSize = 22.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Pan Baraja Agro-Tourism",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "ଭୋଗରାଇ ପାନ ବରଜ ଓ କୃଷି ପର୍ଯ୍ୟଟନ",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF15803D),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFDCFCE7)
            ) {
                Text(
                    text = "Bhograi & Baliapal",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF166534)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Overview
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "The Living Bamboo Tunnels of Balasori Betel",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Balasore’s coastal delta along Bhograi, Baliapal, and Jaleswar cultivates the celebrated 'Mitha Pana' (Sweet Betel Leaf). Farmers construct 'Barajas'—intricate, cathedral-like green tunnels made of bamboo splints, dried paddy straw, and jute stalks that create a cool, 85% humidity microclimate.",
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp, color = MaterialTheme.colorScheme.onSurface)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Microclimate Engineering inside the Baraja
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
            border = BorderStroke(1.dp, Color(0xFFBBF7D0))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Thermostat, null, tint = Color(0xFF16A34A), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Inside the Microclimate Tunnel",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFF166534)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("• Temperature Control: Straw-thatched roofs filter 70% of tropical solar heat, maintaining a pleasant 24°C - 28°C year-round.", fontSize = 11.sp, color = Color(0xFF14532D))
                Spacer(modifier = Modifier.height(4.dp))
                Text("• Organic Irrigation: Vines are nurtured with Subarnarekha river silt, mustard oil cake (ପିଡ଼ିଆ), and pure well water.", fontSize = 11.sp, color = Color(0xFF14532D))
                Spacer(modifier = Modifier.height(4.dp))
                Text("• Sacred Cleanliness: Visitors remove footwear before stepping inside the Baraja to protect delicate roots from pathogens.", fontSize = 11.sp, color = Color(0xFF14532D))
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Traditional Odia Meetha Paan Recipes
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
            border = BorderStroke(1.dp, Color(0xFFFDE68A))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.RestaurantMenu, null, tint = Color(0xFFD97706), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Authentic Balasore Mitha Paan Preparation",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFF92400E)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("• Balasori Sweet Leaf: Tender green leaf with subtle natural sweetness and zero bitter astringency.", fontSize = 11.sp, color = Color(0xFF78350F))
                Spacer(modifier = Modifier.height(4.dp))
                Text("• Traditional Fillings: Gulkand (Damask rose petal jam), roasted fennel seeds (Mauri), sweet grated coconut flakes, clove (Labaṅga), and cardamom (Gujurati). Zero tobacco or artificial chemicals.", fontSize = 11.sp, color = Color(0xFF78350F))
                Spacer(modifier = Modifier.height(4.dp))
                Text("• Famous Tasting Spot: Sri Jagannath Paan Dokan, Cinema Chhak, Balasore & Bhograi Market Chowk.", fontSize = 11.sp, color = Color(0xFF78350F), fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Farmer Cooperative Contact & Directions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:9437456789") // Bhograi Pan Farmers Society
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier.weight(1f).height(42.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Call, null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Pan Farmers Union", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = {
                    openInGoogleMaps(context, 21.6850, 87.3820, "Bhograi Betel Vine Baraja Farm")
                },
                modifier = Modifier.weight(1f).height(42.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Navigation, null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Bhograi Farm (64 km)", fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Done with Pan Baraja", fontWeight = FontWeight.Bold)
        }
    }
}
