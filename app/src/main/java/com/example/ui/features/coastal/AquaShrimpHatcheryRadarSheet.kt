package com.example.ui.features.coastal

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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ShrimpCluster(
    val clusterId: String,
    val nameEn: String,
    val nameOd: String,
    val block: String,
    val activePonds: Int,
    val salinityPpt: Double,
    val phLevel: Double,
    val dissolvedOxygenPpm: Double,
    val waterTempCelsius: Double,
    val vannameiRate30Count: Int,
    val blackTigerRateKg: Int,
    val mpedaCertifiedHatcheries: Int,
    val diseaseAlertStatus: String // "Green (Optimal)", "Yellow (Salinity Fluctuation)", "Alert"
)

val DEFAULT_SHRIMP_CLUSTERS = listOf(
    ShrimpCluster(
        clusterId = "aqua_1",
        nameEn = "Baliapal Subarnarekha Estuary Aqua Belt",
        nameOd = "ବାଲିଆପାଳ ସୁବର୍ଣ୍ଣରେଖା ମୁହାଣ ଚିଙ୍ଗୁଡ଼ି ବଳୟ",
        block = "Baliapal",
        activePonds = 1420,
        salinityPpt = 18.5,
        phLevel = 7.9,
        dissolvedOxygenPpm = 6.8,
        waterTempCelsius = 28.4,
        vannameiRate30Count = 420,
        blackTigerRateKg = 780,
        mpedaCertifiedHatcheries = 14,
        diseaseAlertStatus = "Green (Optimal)"
    ),
    ShrimpCluster(
        clusterId = "aqua_2",
        nameEn = "Bhograi Talsari Coastal Brackish Hub",
        nameOd = "ଭୋଗରାଇ ତାଳସାରୀ ଉପକୂଳ ଲୁଣିଜଳ ହବ୍",
        block = "Bhograi",
        activePonds = 980,
        salinityPpt = 22.1,
        phLevel = 8.1,
        dissolvedOxygenPpm = 6.4,
        waterTempCelsius = 29.0,
        vannameiRate30Count = 415,
        blackTigerRateKg = 810,
        mpedaCertifiedHatcheries = 9,
        diseaseAlertStatus = "Green (Optimal)"
    ),
    ShrimpCluster(
        clusterId = "aqua_3",
        nameEn = "Kasafal & Panchapada Estuarine Farms",
        nameOd = "କସାଫଳ ଓ ପଞ୍ଚପଡ଼ା ମୁହାଣ ଫାର୍ମ",
        block = "Balasore Sadar",
        activePonds = 760,
        salinityPpt = 16.2,
        phLevel = 7.8,
        dissolvedOxygenPpm = 6.2,
        waterTempCelsius = 28.1,
        vannameiRate30Count = 395,
        blackTigerRateKg = 750,
        mpedaCertifiedHatcheries = 7,
        diseaseAlertStatus = "Yellow (Salinity Fluctuation)"
    ),
    ShrimpCluster(
        clusterId = "aqua_4",
        nameEn = "Bahanaga & Soro Marine Aquaculture Corridor",
        nameOd = "ବାହାନଗା ଓ ସୋର ସାମୁଦ୍ରିକ ମତ୍ସ୍ୟ ପାଳନ କରିଡର",
        block = "Bahanaga",
        activePonds = 640,
        salinityPpt = 20.4,
        phLevel = 8.0,
        dissolvedOxygenPpm = 6.5,
        waterTempCelsius = 28.7,
        vannameiRate30Count = 405,
        blackTigerRateKg = 765,
        mpedaCertifiedHatcheries = 5,
        diseaseAlertStatus = "Green (Optimal)"
    )
)

/**
 * Subarnarekha Estuary Brackish Aqua-Culture & Shrimp Hatchery Radar.
 * Real-time salinity, pH, water quality telemetry, export mandi prices, and disease alerts.
 */
@Composable
fun AquaShrimpHatcheryRadarSheet(
    dailyPulse: DailyBalasorePulse,
    language: AppLanguage = AppLanguage.ENGLISH,
    onDismiss: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("All") }
    var lastRefreshed by remember { mutableLongStateOf(System.currentTimeMillis()) }

    val filteredClusters = remember(selectedFilter) {
        if (selectedFilter == "All") DEFAULT_SHRIMP_CLUSTERS
        else DEFAULT_SHRIMP_CLUSTERS.filter { it.block.equals(selectedFilter, ignoreCase = true) }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BentoSlate100)
            .testTag("aqua_shrimp_hatchery_sheet")
    ) {
        // Hero Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF0F172A), OceanBlueDark, Color(0xFF0369A1))
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
                                text = "COASTAL AQUACULTURE • MPEDA & CIBA SYNC",
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
                        text = if (language == AppLanguage.ODIA) "ସୁବର୍ଣ୍ଣରେଖା ଲୁଣିଜଳ ଚିଙ୍ଗୁଡ଼ି ହେଚେରୀ ରାଡାର୍" else "Subarnarekha Estuary Brackish Shrimp Radar",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (language == AppLanguage.ODIA)
                            "ବାଲିଆପାଳ, ଭୋଗରାଇ ଓ କସାଫଳର ୩,୮୦୦+ ପୋଖରୀର ଲାଇଭ୍ ଲବଣାକ୍ତତା (ppt), pH, ଅମ୍ଳଜାନ ଓ ମଣ୍ଡି ଦର"
                        else
                            "Real-time salinity (ppt), pH, dissolved oxygen & export mandi rates across 3,800+ brackish ponds.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFE2E8F0),
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Live Telemetry Banner
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.10f))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "L. Vannamei MSP (30 Count)",
                                color = Color(0xFF94A3B8),
                                fontSize = 10.sp
                            )
                            Text(
                                text = "₹${dailyPulse.aquacultureVannameiRateKg} / kg",
                                color = Color(0xFF38BDF8),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Column {
                            Text(
                                text = "Black Tiger (Monodon)",
                                color = Color(0xFF94A3B8),
                                fontSize = 10.sp
                            )
                            Text(
                                text = "₹${dailyPulse.aquacultureTigerPrawnRateKg} / kg",
                                color = AmberGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        IconButton(
                            onClick = { lastRefreshed = System.currentTimeMillis() },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // Filter Chips
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "Baliapal", "Bhograi", "Balasore Sadar", "Bahanaga").forEach { block ->
                    FilterChip(
                        selected = selectedFilter == block,
                        onClick = { selectedFilter = block },
                        label = { Text(block, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = OceanBlue,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // List of Shrimp Farm Clusters
        items(filteredClusters) { cluster ->
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
                                text = if (language == AppLanguage.ODIA) cluster.nameOd else cluster.nameEn,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BentoSlate900
                                )
                            )
                            Text(
                                text = "Block: ${cluster.block} • Active Bio-Ponds: ${cluster.activePonds}",
                                style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate600)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (cluster.diseaseAlertStatus.contains("Green")) EmeraldGreen.copy(alpha = 0.15f) else AmberGold.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, if (cluster.diseaseAlertStatus.contains("Green")) EmeraldGreen else AmberGold)
                        ) {
                            Text(
                                text = cluster.diseaseAlertStatus,
                                color = if (cluster.diseaseAlertStatus.contains("Green")) EmeraldGreen else AmberGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = BentoSlate200)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Water Quality Parameters Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Salinity", fontSize = 10.sp, color = BentoSlate600)
                            Text("${cluster.salinityPpt} ppt", fontWeight = FontWeight.Bold, color = OceanBlue, fontSize = 13.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("pH Level", fontSize = 10.sp, color = BentoSlate600)
                            Text("${cluster.phLevel}", fontWeight = FontWeight.Bold, color = BentoSlate900, fontSize = 13.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Dissolved O₂", fontSize = 10.sp, color = BentoSlate600)
                            Text("${cluster.dissolvedOxygenPpm} ppm", fontWeight = FontWeight.Bold, color = EmeraldGreen, fontSize = 13.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Water Temp", fontSize = 10.sp, color = BentoSlate600)
                            Text("${cluster.waterTempCelsius}°C", fontWeight = FontWeight.Bold, color = BentoSlate900, fontSize = 13.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Rates and Certified Seeds
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = BentoSlate100,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Vannamei: ₹${cluster.vannameiRate30Count}/kg • Tiger: ₹${cluster.blackTigerRateKg}/kg",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = BentoSlate900
                                )
                                Text(
                                    text = "MPEDA/CAA Certified Seed Sources: ${cluster.mpedaCertifiedHatcheries} Hatcheries",
                                    fontSize = 10.sp,
                                    color = BentoSlate600
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = OceanBlue,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
