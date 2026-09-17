package com.example.ui.features.artisans

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
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material.icons.filled.Verified
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

data class ArtisanCluster(
    val craftName: String,
    val odiaName: String,
    val location: String,
    val artisanLeader: String,
    val contactNumber: String,
    val specialtyItems: String,
    val priceRange: String,
    val lat: Double,
    val lng: Double
)

/**
 * Nilagiri Green Stone Craft & Sabai Grass Artisan Marketplace Sheet.
 */
@Composable
fun NilagiriStoneSabaiSheet(
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val artisanClusters = listOf(
        ArtisanCluster(
            craftName = "Nilagiri Green Stone Utensils",
            odiaName = "ନୀଳଗିରି ପଥର କୁଣ୍ଡି ଓ ବାସନ",
            location = "Nilagiri Artisan Colony (32 km from Balasore)",
            artisanLeader = "Nilamani Maharana (Master Sculptor)",
            contactNumber = "9437145210",
            specialtyItems = "Natural Pyrophyllite Dal Bowls (Kundi), Mortar-Pestle (Sil-Bata), Lord Jagannath Carvings",
            priceRange = "₹250 - ₹2,500",
            lat = 21.4608,
            lng = 86.7645
        ),
        ArtisanCluster(
            craftName = "Baliapal Sabai (Golden Grass) Craft",
            odiaName = "ବାଲିଆପାଳ ସବାଇ ଘାସ ହସ୍ତଶିଳ୍ପ",
            location = "Baliapal Weaver Cooperative (48 km)",
            artisanLeader = "Maa Tarini Sabai Mahila Samiti",
            contactNumber = "9438234890",
            specialtyItems = "Braided Storage Baskets, Dining Table Runners, Eco-Planters, Beach Hats",
            priceRange = "₹120 - ₹1,800",
            lat = 21.6512,
            lng = 87.2845
        ),
        ArtisanCluster(
            craftName = "Remuna Bell Metal & Brass",
            odiaName = "ରେମୁଣା କଂସା ଓ ପିତ୍ତଳ ଶିଳ୍ପ",
            location = "Kansari Sahi, Remuna (9 km)",
            artisanLeader = "Bikram Kansari",
            contactNumber = "9439123847",
            specialtyItems = "Traditional Gopinath Bhog Kansa Thali, Temple Bells, Engraved Water Jugs",
            priceRange = "₹450 - ₹4,500",
            lat = 21.5284,
            lng = 86.8712
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("nilagiri_stone_sabai_sheet")
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
                                listOf(Color(0xFF0D9488), Color(0xFF0F766E))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🪨", fontSize = 22.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Nilagiri Stone & Sabai Craft",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "ନୀଳଗିରି ପଥର ଓ ସବାଇ ହସ୍ତକଳା ବଜାର",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF0F766E),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFCCFBF1)
            ) {
                Text(
                    text = "Fair Trade",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF115E59)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Info Banner
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF0FDFA),
            border = BorderStroke(1.dp, Color(0xFF99F6E4)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Verified, null, tint = Color(0xFF0D9488), modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Direct from Indigenous Artisans: Zero middlemen commissions. All purchases directly sustain tribal families in Nilagiri foothills & Baliapal weavers.",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF134E4A), lineHeight = 16.sp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Artisan Clusters List
        Text(
            text = "Geo-Mapped Artisan Workshop Clusters",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(8.dp))

        artisanClusters.forEach { cluster ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(cluster.craftName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(cluster.odiaName, fontSize = 11.sp, color = Color(0xFF0D9488), fontWeight = FontWeight.SemiBold)
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFF0FDF4)
                        ) {
                            Text(
                                cluster.priceRange,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF166534)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text("📍 ${cluster.location}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("👤 ${cluster.artisanLeader}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("🛍️ ${cluster.specialtyItems}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:${cluster.contactNumber}")
                                }
                                context.startActivity(intent)
                            },
                            modifier = Modifier.weight(1f).height(38.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D9488)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Call, null, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Call Artisan", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                openInGoogleMaps(context, cluster.lat, cluster.lng, cluster.craftName)
                            },
                            modifier = Modifier.weight(1f).height(38.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Navigation, null, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Map Route", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Traditional Stone Cookware Curing Guide
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
            border = BorderStroke(1.dp, Color(0xFFFDE68A))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.TipsAndUpdates, null, tint = Color(0xFFB45309), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "How to Season Nilagiri Stone Cookware (ପଥର ବାସନ ଶୋଧନ)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFF92400E)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("1. Rice Gruel Soak: Soak the new green stone bowl in warm rice starch water (ତୋରାଣି) for 48 hours to seal natural micropores.", fontSize = 11.sp, color = Color(0xFF78350F))
                Spacer(modifier = Modifier.height(4.dp))
                Text("2. Mustard Oil Rub: Coat both surfaces with warm pure mustard oil and dry under gentle sunlight for 2 consecutive days.", fontSize = 11.sp, color = Color(0xFF78350F))
                Spacer(modifier = Modifier.height(4.dp))
                Text("3. Culinary Magic: Seasoned stone keeps Odia Dalma and Machha Besara warm for hours while imparting rich alkaline trace minerals.", fontSize = 11.sp, color = Color(0xFF78350F))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D9488)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Done with Artisan Directory", fontWeight = FontWeight.Bold)
        }
    }
}
