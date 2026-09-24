package com.example.ui.features.coastal

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.data.model.AppLanguage
import com.example.ui.theme.*

data class EcoSpotDetail(
    val title: String,
    val titleOd: String,
    val attractionType: String,
    val tariff: String,
    val bestTime: String,
    val guidelines: String,
    val bookingPhone: String
)

val TALASARI_ECO_SPOTS = listOf(
    EcoSpotDetail(
        title = "Bichitrapur Mangrove Eco-Boat Safari",
        titleOd = "ବିଚିତ୍ରପୁର ହେନ୍ତାଳବଣ ଇକୋ-ବୋଟିଂ ସଫାରି",
        attractionType = "OFDC Tidal Creek Cruise",
        tariff = "₹1,200 per boat (up to 8 persons, 1 hr)",
        bestTime = "High Tide window (08:30 AM - 01:00 PM)",
        guidelines = "Cruise through dense virgin mangroves, see horseshoe crabs, mudskippers, and migratory pelicans. Life jackets mandatory.",
        bookingPhone = "9437299105"
    ),
    EcoSpotDetail(
        title = "Talasari Red Ghost Crab Sandbars",
        titleOd = "ତାଳସାରୀ ଲାଲ କଙ୍କଡ଼ା ବାଲୁକା ବେଳାଭୂମି",
        attractionType = "Natural Sand Spit & Estuary",
        tariff = "Free Public Access (Country boat cross ₹30)",
        bestTime = "Sunrise 05:30 AM - 08:00 AM",
        guidelines = "Millions of fiery red ghost crabs emerge on the tranquil sand spit where Subarnarekha meets Bay of Bengal. Please do not litter or disturb crab burrows.",
        bookingPhone = "06781222212"
    ),
    EcoSpotDetail(
        title = "Digha - Talasari Border Cross Transit",
        titleOd = "ଦୀଘା - ତାଳସାରୀ ସୀମାନ୍ତ ଯାତ୍ରା",
        attractionType = "Interstate Coastal Link",
        tariff = "Auto/Toto ₹150 - ₹200 / Shared ₹30",
        bestTime = "06:00 AM - 08:30 PM",
        guidelines = "Only 8 km from New Digha Railway Station. Direct scenic coastal road through Casuarina groves across Odisha checkpost.",
        bookingPhone = "06781223400"
    )
)

/**
 * Talasari & Bichitrapur Mangrove Eco-Boat Explorer.
 * OFDC Mangrove boating tariffs, tidal creek safety advisories, Red Ghost crab colonies,
 * and Digha border transit connections.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TalasariMangroveExplorerSheet(
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .testTag("talasari_mangrove_explorer_sheet")
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
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0F2FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🦀", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ତାଳସାରୀ ଓ ବିଚିତ୍ରପୁର ହେନ୍ତାଳବଣ" else "Talasari & Bichitrapur Eco-Boat",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଲାଲ୍ କଙ୍କଡ଼ା କଲୋନୀ • ଓଏଫଡିସି ବୋଟ୍ ସଫାରି • ଦୀଘା ସଂଯୋଗ" else "OFDC Mangrove Cruise • Red Ghost Crabs • Digha Link",
                            style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate500)
                        )
                    }
                }
                IconButton(onClick = onClose) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.88f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Eco-Advisory Banner
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                        border = BorderStroke(1.dp, Color(0xFFBBF7D0))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🌊", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Subarnarekha Estuary Coastal Ecology",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF166534)
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Bichitrapur is Odisha's unique northern mangrove sanctuary spread over 563 hectares on Subarnarekha delta. Home to living fossils (Horseshoe Crabs) and endangered river dolphins.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF14532D),
                                    lineHeight = 18.sp
                                )
                            )
                        }
                    }
                }

                // Spots List
                items(TALASARI_ECO_SPOTS) { spot ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = spot.attractionType,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(0xFF0284C7),
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        text = if (language == AppLanguage.ODIA) spot.titleOd else spot.title,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = BentoSlate900
                                        )
                                    )
                                }

                                Surface(
                                    color = Color(0xFFECFEFF),
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(1.dp, Color(0xFFA5F3FC))
                                ) {
                                    Text(
                                        text = spot.tariff,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFF0E7490)
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "⏰ Best Window: ${spot.bestTime}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF0369A1),
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = spot.guidelines,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = BentoSlate600,
                                    lineHeight = 18.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Eco-Tourism Desk",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = BentoSlate500,
                                        fontSize = 11.sp
                                    )
                                )

                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${spot.bookingPhone}"))
                                        context.startActivity(intent)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = OceanBlue),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Call Booking Desk", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}
