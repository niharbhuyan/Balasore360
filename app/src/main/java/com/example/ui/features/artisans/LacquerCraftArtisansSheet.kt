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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Palette
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.daily.DailyUpdateEngine
import com.example.ui.components.openInGoogleMaps

data class LacquerMasterArtisan(
    val id: String,
    val name: String,
    val odiaName: String,
    val craftSpecialty: String,
    val clusterLocation: String,
    val phone: String,
    val experienceYears: Int,
    val latitude: Double,
    val longitude: Double,
    val awards: String
)

/**
 * "Mati O Manisha" Baleswari Lac Bangles & Lacquer Craft Showcase Sheet.
 * Direct artisan directory, workshop GPS locations, and traditional handicraft storytelling.
 */
@Composable
fun LacquerCraftArtisansSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dailyPulse = remember { DailyUpdateEngine.getDailyPulse() }

    val masterArtisans = remember {
        listOf(
            LacquerMasterArtisan(
                id = "lac_1",
                name = "Guru Balaram Sahu",
                odiaName = "ଗୁରୁ ବଳରାମ ସାହୁ (ଲାଖ ଶିଳ୍ପୀ)",
                craftSpecialty = "Bridal Lac Bangles (ଲାଖ ଶଙ୍ଖା) & Mina Work",
                clusterLocation = "Motiganj Crafts Lane, Balasore Town",
                phone = "9437281920",
                experienceYears = 38,
                latitude = 21.4942,
                longitude = 86.9315,
                awards = "Odisha State Shilpi Guru Awardee"
            ),
            LacquerMasterArtisan(
                id = "lac_2",
                name = "Smt. Manorama Maharana",
                odiaName = "ଶ୍ରୀମତୀ ମନୋରମା ମହାରଣା",
                craftSpecialty = "Traditional Lacquered Wooden Toys & Kumkum Boxes",
                clusterLocation = "Remuna Artisan Basti, Balasore",
                phone = "9861453201",
                experienceYears = 26,
                latitude = 21.5280,
                longitude = 86.8650,
                awards = "District Handicraft Excellence Recognition"
            ),
            LacquerMasterArtisan(
                id = "lac_3",
                name = "Nilakantha Crafts Cooperative",
                odiaName = "ନୀଳକଣ୍ଠ ଲାଖ ଓ କଂସା ଶିଳ୍ପ ସମବାୟ",
                craftSpecialty = "Natural Resin Lac Bangles & Festival Sets",
                clusterLocation = "Soro Crafts Market, NH-16",
                phone = "9438102938",
                experienceYears = 45,
                latitude = 21.2880,
                longitude = 86.6890,
                awards = "Export Fair Trade Certified SHG Cluster"
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag("lacquer_craft_artisans_sheet")
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
                        .background(Color(0xFFFCE7F3)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Palette,
                        contentDescription = null,
                        tint = Color(0xFFDB2777),
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Mati O Manisha: Lacquer Craft",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0F172A)
                        )
                    )
                    Text(
                        text = "ବାଲେଶ୍ୱରୀ ଲାଖ ଚୁଡ଼ି ଓ ହସ୍ତଶିଳ୍ପ ସମ୍ଭାର",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFDB2777),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Heritage Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFDF2F8)),
            border = BorderStroke(1.dp, Color(0xFFFBCFE8))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "✨ CENTURIES-OLD ARTISAN HERITAGE",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFBE185D),
                            letterSpacing = 1.sp
                        )
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFFCE7F3)
                    ) {
                        Text(
                            text = "DIRECT FAIR-TRADE",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF9D174D),
                                fontSize = 9.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Balasore's traditional lacquer craft utilizes natural tree resin harvested from local Kusum and Ber trees. Master craftsmen shape molten lac over heated iron mandrels into lustrous wedding bangles (ଲାଖ ଶଙ୍ଖା) embellished with pure brass foil and vibrant mineral pigments.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF831843),
                        lineHeight = 17.sp
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Daily Auto-Updated Showcase Bulletin
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFFCE7F3),
                    border = BorderStroke(1.dp, Color(0xFFF472B6))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🎨", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "TODAY'S ARTISAN SPOTLIGHT",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF9D174D),
                                    fontSize = 9.sp
                                )
                            )
                            Text(
                                text = dailyPulse.artisanShowcaseOfTheDay,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF831843),
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Master Artisan Clusters & Workshops",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        masterArtisans.forEach { artisan ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFFCE7F3))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = artisan.name,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                            )
                            Text(
                                text = artisan.odiaName,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xFFDB2777),
                                    fontSize = 11.sp
                                )
                            )
                            Text(
                                text = artisan.craftSpecialty,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF475569),
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFDF2F8),
                            border = BorderStroke(1.dp, Color(0xFFFBCFE8))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Default.Verified, contentDescription = null, tint = Color(0xFFDB2777), modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${artisan.experienceYears} Yrs Craft",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFF9D174D),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "📍 ${artisan.clusterLocation}",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
                    )
                    Text(
                        text = "🏆 ${artisan.awards}",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFB45309), fontWeight = FontWeight.Medium)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${artisan.phone}"))
                                context.startActivity(dialIntent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDB2777)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Call Artisan", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = { openInGoogleMaps(context, artisan.latitude, artisan.longitude, "${artisan.name} Workshop") },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Directions", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
