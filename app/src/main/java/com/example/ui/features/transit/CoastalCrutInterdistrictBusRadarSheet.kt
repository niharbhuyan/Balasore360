package com.example.ui.features.transit

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.ui.theme.BentoSlate800
import com.example.ui.theme.BentoSlate900
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark

data class CoastalBusRoute(
    val routeCode: String,
    val originDestinationEn: String,
    val originDestinationOd: String,
    val operator: String,
    val nextDeparture: String,
    val frequencyMins: Int,
    val currentGpsLocation: String,
    val fareAcRs: Int,
    val fareNonAcRs: Int,
    val stopsCount: Int
)

val DEFAULT_COASTAL_BUS_ROUTES = listOf(
    CoastalBusRoute(
        routeCode = "Route 81",
        originDestinationEn = "Balasore Sahadevkhunta ⇄ Chandipur Beach",
        originDestinationOd = "ବାଲେଶ୍ୱର ସହଦେବଖୁଣ୍ଟା ⇄ ଚାନ୍ଦିପୁର ବେଳାଭୂମି",
        operator = "CRUT Mo Bus AC Electric Fleet",
        nextDeparture = "In 12 mins (03:15 PM)",
        frequencyMins = 20,
        currentGpsLocation = "Near Proof Road Crossing",
        fareAcRs = 25,
        fareNonAcRs = 15,
        stopsCount = 9
    ),
    CoastalBusRoute(
        routeCode = "Route 82",
        originDestinationEn = "Balasore Junction ⇄ Talsari & Bichitrapur",
        originDestinationOd = "ବାଲେଶ୍ୱର ଷ୍ଟେସନ ⇄ ତାଳସାରୀ ଓ ବିଚିତ୍ରପୁର",
        operator = "OSRTC Coastal Express",
        nextDeparture = "In 25 mins (03:30 PM)",
        frequencyMins = 45,
        currentGpsLocation = "Crossing Basta Bypass",
        fareAcRs = 90,
        fareNonAcRs = 60,
        stopsCount = 14
    ),
    CoastalBusRoute(
        routeCode = "Route 83",
        originDestinationEn = "Balasore Bus Stand ⇄ Kasafal Fishing Port",
        originDestinationOd = "ବାଲେଶ୍ୱର ବସ୍ ଷ୍ଟାଣ୍ଡ ⇄ କସାଫଳ ମତ୍ସ୍ୟ ବନ୍ଦର",
        operator = "Mo Bus Rural Feeder",
        nextDeparture = "In 18 mins (03:20 PM)",
        frequencyMins = 30,
        currentGpsLocation = "Departed Haladipada",
        fareAcRs = 35,
        fareNonAcRs = 20,
        stopsCount = 11
    ),
    CoastalBusRoute(
        routeCode = "Route 84",
        originDestinationEn = "Balasore ⇄ Nilagiri Panchalingeswar",
        originDestinationOd = "ବାଲେଶ୍ୱର ⇄ ନୀଳଗିରି ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର",
        operator = "CRUT Mo Bus Green Corridor",
        nextDeparture = "In 8 mins (03:10 PM)",
        frequencyMins = 25,
        currentGpsLocation = "Near Sergarh Toll Gate",
        fareAcRs = 40,
        fareNonAcRs = 25,
        stopsCount = 12
    )
)

/**
 * Balasore Coastal Express & CRUT Mo Bus Live Fleet Radar Sheet.
 */
@Composable
fun CoastalCrutInterdistrictBusRadarSheet(
    dailyPulse: DailyBalasorePulse,
    language: AppLanguage = AppLanguage.ENGLISH,
    onDismiss: () -> Unit
) {
    var lastSync by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BentoSlate100)
            .testTag("coastal_bus_radar_sheet")
    ) {
        // Hero Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF0F172A), OceanBlueDark, OceanBlue)
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
                                text = "CRUT MO BUS & OSRTC • LIVE FLEET GPS",
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
                        text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ଉପକୂଳ ମୋ ବସ୍ ଓ ଏକ୍ସପ୍ରେସ୍ ରାଡାର୍" else "Balasore Coastal Bus & Fleet Radar",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (language == AppLanguage.ODIA)
                            "ଚାନ୍ଦିପୁର, ତାଳସାରୀ, କସାଫଳ ଓ ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ବସ୍ ରୁଟ୍, ଜିପିଏସ୍ ଲାଇଭ୍ ଟ୍ରାକିଂ ଓ ଡିଜିଟାଲ୍ ଟିକେଟ୍"
                        else
                            "Real-time GPS bus arrivals, digital fares, and frequency for Chandipur, Talsari, Kasafal, and Nilagiri routes.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFE2E8F0),
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.12f))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Fleet Status: All 28 Coastal Buses on Schedule",
                            color = EmeraldGreen,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(
                            onClick = { lastSync = System.currentTimeMillis() },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        items(DEFAULT_COASTAL_BUS_ROUTES) { route ->
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.DirectionsBus, contentDescription = null, tint = OceanBlue, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = route.routeCode,
                                fontWeight = FontWeight.ExtraBold,
                                color = OceanBlue,
                                fontSize = 14.sp
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = EmeraldGreen.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, EmeraldGreen)
                        ) {
                            Text(
                                text = route.nextDeparture,
                                color = EmeraldGreen,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (language == AppLanguage.ODIA) route.originDestinationOd else route.originDestinationEn,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate900
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${route.operator} • Every ${route.frequencyMins} mins • ${route.stopsCount} stops",
                        style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate600)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = BentoSlate200)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.GpsFixed, contentDescription = null, tint = AmberGold, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = route.currentGpsLocation,
                                fontSize = 11.sp,
                                color = BentoSlate800,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Text(
                            text = "AC: ₹${route.fareAcRs} | Non-AC: ₹${route.fareNonAcRs}",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = OceanBlue
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
