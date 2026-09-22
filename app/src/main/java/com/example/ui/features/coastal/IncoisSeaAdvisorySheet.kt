package com.example.ui.features.coastal

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Waves
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.data.model.AppLanguage
import com.example.data.model.CoastalHarborAdvisory
import com.example.data.model.HarborSafetyFlag
import com.example.data.model.IncoisPfZZone
import com.example.data.repository.SmartFeaturesRepository
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark

@Composable
fun IncoisSeaAdvisorySheet(
    language: AppLanguage = AppLanguage.ENGLISH,
    onClose: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val harbors = SmartFeaturesRepository.coastalHarbors
    val pfzZones = SmartFeaturesRepository.pfzZones

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("incois_sea_advisory_sheet"),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Hero Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF0F172A), Color(0xFF0369A1))
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
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "INCOIS & MOES OCEAN ADVISORY",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF7DD3FC),
                                    fontSize = 10.sp
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF22C55E),
                            modifier = Modifier.size(10.dp)
                        ) {}
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (language == AppLanguage.ODIA)
                            "ବାଲେଶ୍ୱର ସମୁଦ୍ର ତରଙ୍ଗ ଓ ମତ୍ସ୍ୟଜୀବୀ ସୁରକ୍ଷା"
                        else
                            "INCOIS Sea State & PFZ Tracker",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (language == AppLanguage.ODIA)
                            "ବଳରାମଗଡ଼ି, କାସାଫାଳ, ଚାନ୍ଦିପୁର ଓ ତାଳସାରୀ ମୁହାଣ ପାଇଁ ଲାଇଭ୍ ତରଙ୍ଗ ଉଚ୍ଚତା, ସାମ୍ଭାବ୍ୟ ମାଛଧରା କ୍ଷେତ୍ର (PFZ) ଏବଂ ସମୁଦ୍ର ସତର୍କତା।"
                        else
                            "Real-time Bay of Bengal swell heights, Potential Fishing Zones (PFZ), and harbor safety flags for Balasore coastal communities.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    )
                }
            }
        }

        // Section: Harbor Wave Telemetry & Safety Flags
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Text(
                    text = if (language == AppLanguage.ODIA) "ଉପକୂଳ ମତ୍ସ୍ୟ ବନ୍ଦର ସ୍ଥିତି ଓ ତରଙ୍ଗ ଉଚ୍ଚତା" else "Harbor Wave Telemetry & Bar-Crossing Status",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = "Live telemetry updated hourly from INCOIS coastal buoys",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                )
            }
        }

        items(harbors, key = { it.harborId }) { harbor ->
            HarborAdvisoryCard(harbor = harbor, language = language)
        }

        // Section: Potential Fishing Zones (PFZ)
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                Text(
                    text = if (language == AppLanguage.ODIA) "ସାମ୍ଭାବ୍ୟ ମତ୍ସ୍ୟ ସମୃଦ୍ଧ କ୍ଷେତ୍ର (PFZ Coordinates)" else "Potential Fishing Zones (PFZ Coordinates)",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = "Ocean satellite chlorophyll & thermal front aggregation maps",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                )
            }
        }

        items(pfzZones, key = { it.id }) { zone ->
            PfzZoneCard(zone = zone, language = language)
        }

        // Emergency Maritime Rescue Hotlines
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F2)),
                border = BorderStroke(1.dp, Color(0xFFFECDD3))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Emergency",
                            tint = Color(0xFFE11D48),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.ODIA) "ସାମୁଦ୍ରିକ ଜରୁରୀକାଳୀନ ହଟ୍‌ଲାଇନ୍" else "Maritime Search & Rescue Hotlines",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF9F1239)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "• Balaramgadi Marine Police Station: 06782-272100\n• Indian Coast Guard (Paradeep MRCC): 1554 (Toll-Free)\n• Balasore District Disaster Control Room: 1077 / 06782-262674",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF881337),
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1554"))
                            context.startActivity(dialIntent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Call",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Call Coast Guard MRCC (1554)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.5.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HarborAdvisoryCard(harbor: CoastalHarborAdvisory, language: AppLanguage) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (language == AppLanguage.ODIA) harbor.odiaName else harbor.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = "Wind: ${harbor.windSpeedKnots} kts (${harbor.windDirection})",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(harbor.safetyFlag.hexColor).copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, Color(harbor.safetyFlag.hexColor))
                ) {
                    Text(
                        text = if (language == AppLanguage.ODIA) harbor.safetyFlag.odiaLabel else harbor.safetyFlag.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = Color(harbor.safetyFlag.hexColor)
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "Wave Height", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            text = "${harbor.currentWaveHeightMeters} meters",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = OceanBlue
                        )
                        Text(text = "Period: ${harbor.swellPeriodSeconds}s", fontSize = 9.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "Tidal Phase", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            text = "Low ${harbor.lowTideTime}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFF059669)
                        )
                        Text(text = "High ${harbor.highTideTime}", fontSize = 9.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (language == AppLanguage.ODIA) harbor.odiaNotice else harbor.notice,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            )
        }
    }
}

@Composable
private fun PfzZoneCard(zone: IncoisPfZZone, language: AppLanguage) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DirectionsBoat,
                        contentDescription = "Boat",
                        tint = OceanBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.ODIA) zone.odiaHarborName else zone.harborName,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = OceanBlue.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "${zone.distanceKm} km offshore",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = OceanBlueDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Target Species: ${if (language == AppLanguage.ODIA) zone.odiaSpecies else zone.expectedSpecies}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "SST: ${zone.seaSurfaceTempCelsius}°C",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Text(
                    text = "Chlorophyll: ${zone.chlorophyllConcentration} mg/m³",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        color = Color(0xFF059669),
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = "Depth: ${zone.depthFathoms} fathoms",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}
