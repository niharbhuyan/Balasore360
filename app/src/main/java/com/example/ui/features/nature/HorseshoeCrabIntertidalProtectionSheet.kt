package com.example.ui.features.nature

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Water
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.data.daily.DailyBalasorePulse
import com.example.data.model.AppLanguage
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate200
import com.example.ui.theme.BentoSlate600
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate900
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark

data class HorseshoeCrabSite(
    val siteId: String,
    val locationNameEn: String,
    val locationNameOd: String,
    val speciesFound: String,
    val habitatType: String,
    val recentSightingsCount: Int,
    val bestObservationWindow: String,
    val conservationStatus: String,
    val safetyGuidelines: String
)

val DEFAULT_CRAB_SITES = listOf(
    HorseshoeCrabSite(
        siteId = "crab_1",
        locationNameEn = "Balaramgadi River Estuary & Mudflats",
        locationNameOd = "ବଳରାମଗଡ଼ି ନଦୀ ମୁହାଣ ଓ କାଦୁଅ ଚରା",
        speciesFound = "Tachypleus gigas (Indo-Pacific Horseshoe Crab)",
        habitatType = "Intertidal brackish mudflat with rich organic sediment",
        recentSightingsCount = 48,
        bestObservationWindow = "Full Moon / New Moon High Tide Ebb (06:00 AM - 08:30 AM)",
        conservationStatus = "Schedule IV Wildlife Protection Act & IUCN Vulnerable",
        safetyGuidelines = "Do not flip or pick up by the telson (tail spine). Never remove from wet sand."
    ),
    HorseshoeCrabSite(
        siteId = "crab_2",
        locationNameEn = "Chandipur Receding Intertidal Sands",
        locationNameOd = "ଚାନ୍ଦିପୁର ଜୁଆର-ଭଟ୍ଟା ବାଲିଚରା",
        speciesFound = "Carcinoscorpius rotundicauda (Mangrove Horseshoe Crab)",
        habitatType = "Vast vanishing intertidal seabed (1.5 km to 3.0 km zone)",
        recentSightingsCount = 32,
        bestObservationWindow = "Low-Tide Receding Phase (10:30 AM - 01:00 PM)",
        conservationStatus = "Strictly Protected Marine Bio-Indicator",
        safetyGuidelines = "Photograph from distance. Observe gentle burrows. Keep dogs away."
    ),
    HorseshoeCrabSite(
        siteId = "crab_3",
        locationNameEn = "Khandia Estuary & Chaumukh Shore",
        locationNameOd = "ଖାଣ୍ଡିଆ ମୁହାଣ ଓ ଚୌମୁଖ ବେଳା",
        speciesFound = "Tachypleus gigas Pair Nesting",
        habitatType = "Mangrove fringe fine sandbars",
        recentSightingsCount = 27,
        bestObservationWindow = "Spring Tide Morning Slack",
        conservationStatus = "ZSI Active Tagging & Monitoring Habitat",
        safetyGuidelines = "Report tagged crabs with identification code to Forest Guard."
    )
)

/**
 * Horseshoe Crab & Intertidal Bio-Reserve Marine Protection Watch Sheet.
 */
@Composable
fun HorseshoeCrabIntertidalProtectionSheet(
    dailyPulse: DailyBalasorePulse,
    language: AppLanguage = AppLanguage.ENGLISH,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BentoSlate100)
            .testTag("horseshoe_crab_protection_sheet")
    ) {
        // Hero Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF0F172A), Color(0xFF1E293B), OceanBlueDark)
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF38BDF8).copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "450-MILLION-YEAR LIVING FOSSIL • ZSI MONITOR",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp,
                                    fontSize = 9.5.sp,
                                    color = Color(0xFF7DD3FC)
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (language == AppLanguage.ODIA) "ଜୀବନ୍ତ ଜୀବାଶ୍ମ ରାଜକଙ୍କଡ଼ା ସଂରକ୍ଷଣ ୱାଚ୍" else "Horseshoe Crab Marine Bio-Reserve",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (language == AppLanguage.ODIA)
                            "୪୫ କୋଟି ବର୍ଷ ପୁରାତନ ନୀଳରକ୍ତ ରାଜକଙ୍କଡ଼ାର ଚାନ୍ଦିପୁର-ବଳରାମଗଡ଼ିରେ ବସାବାନ୍ଧିବା ଓ ସୁରକ୍ଷା ଟେଲିମେଟ୍ରି"
                        else
                            "Track Chandipur's 450-million-year-old living fossils with blue copper blood (LAL), essential for global medical testing.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFE2E8F0),
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Bio-Scientific Fact Card
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.12f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Water, contentDescription = null, tint = Color(0xFF38BDF8), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Miracle of Blue Blood (Limulus Amebocyte Lysate)",
                                    color = Color(0xFF7DD3FC),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Their hemocyanin copper-based blue blood clots instantly around bacterial endotoxins, safeguarding all human injectable vaccines worldwide.",
                                color = Color(0xFFCBD5E1),
                                fontSize = 10.5.sp,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }
        }

        items(DEFAULT_CRAB_SITES) { site ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BentoSlate200),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (language == AppLanguage.ODIA) site.locationNameOd else site.locationNameEn,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BentoSlate900
                                )
                            )
                            Text(
                                text = site.speciesFound,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = OceanBlue,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = EmeraldGreen.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, EmeraldGreen)
                        ) {
                            Text(
                                text = "${site.recentSightingsCount} Sightings",
                                color = EmeraldGreen,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Habitat: ${site.habitatType}",
                        fontSize = 11.sp,
                        color = BentoSlate700
                    )
                    Text(
                        text = "Best Observation: ${site.bestObservationWindow}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BentoSlate900
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = BentoSlate100,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Guidelines: ${site.safetyGuidelines}",
                            fontSize = 10.5.sp,
                            color = BentoSlate700,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }

        // Marine Rescue Hotline
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BentoSlate200)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Report Injured or Stranded Horseshoe Crabs",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = BentoSlate900
                    )
                    Text(
                        text = "Balasore Wildlife Division & Marine Police 24x7 Hotline",
                        fontSize = 11.sp,
                        color = BentoSlate600
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:06782262148")
                            }
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OceanBlue)
                    ) {
                        Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Call Marine Forest Hotline (06782-262148)", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
