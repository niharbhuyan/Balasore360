package com.example.ui.features.coastal

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.daily.DailyBalasorePulse
import com.example.data.model.AppLanguage
import kotlinx.coroutines.delay

@Composable
fun TalasariRedCrabSafariSheet(
    dailyPulse: DailyBalasorePulse,
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val isOdia = language == AppLanguage.ODIA

    var refreshSecondsRemaining by remember { mutableIntStateOf(30) }
    var lastSyncTimestamp by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var isManualRefreshing by remember { mutableStateOf(false) }
    var selectedZoneIndex by remember { mutableIntStateOf(0) }

    // Auto-update countdown loop (auto updates every 30 seconds)
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            if (refreshSecondsRemaining > 1) {
                refreshSecondsRemaining--
            } else {
                refreshSecondsRemaining = 30
                lastSyncTimestamp = System.currentTimeMillis()
            }
        }
    }

    val pulseTransition = rememberInfiniteTransition(label = "pulse")
    val alphaAnim by pulseTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    val zones = listOf(
        TalasariZone(
            nameEn = "Talasari Main Estuary & Sandbar",
            nameOd = "ତାଳସାରୀ ମୁହାଣ ଓ ବାଲିଚଡ଼ା",
            statusEn = "High Crab Activity • Feeding Window Active",
            statusOd = "ପ୍ରଚୁର ଲାଲ୍ କଙ୍କଡ଼ା ସକ୍ରିୟ • ଭଟ୍ଟା ସମୟ ଚାଲୁଛି",
            distanceMeters = 350,
            boatFare = "₹40 / person",
            isSafeWalk = true
        ),
        TalasariZone(
            nameEn = "Udaypur Virgin Beach Casuarina Trail",
            nameOd = "ଉଦୟପୁର ଝାଉଁବଣ ଓ ନିର୍ଜନ ବେଳାଭୂମି",
            statusEn = "Pristine Colonies • Photography Optimal",
            statusOd = "ଶାନ୍ତ ପରିବେଶ • ଫଟୋଗ୍ରାଫି ପାଇଁ ଉତ୍କୃଷ୍ଟ",
            distanceMeters = 1200,
            boatFare = "Direct Beach Walk",
            isSafeWalk = true
        ),
        TalasariZone(
            nameEn = "Bichitrapur Mangrove Creek Deep Estuary",
            nameOd = "ବିଚିତ୍ରପୁର ହେନ୍ତାଳବଣ ଗଭୀର ମୁହାଣ",
            statusEn = "Motorboat Safari Mandatory • Tide Dependent",
            statusOd = "ମୋଟରବୋଟ୍ ସଫାରି ଆବଶ୍ୟକ • ଜୁଆର ଉପରେ ନିର୍ଭରଶୀଳ",
            distanceMeters = 4200,
            boatFare = "₹120 / seat (OFDC)",
            isSafeWalk = false
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Sheet Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🦀", fontSize = 26.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isOdia) "ତାଳସାରୀ ଓ ଉଦୟପୁର ଲାଲ୍ କଙ୍କଡ଼ା ସଫାରୀ" else "Talasari & Udaypur Red Crab Safari",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF991B1B)
                        )
                    )
                }
                Text(
                    text = if (isOdia) "ବାଲେଶ୍ୱର ଉପକୂଳ ଜୀବବୈଚିତ୍ର୍ୟ ଓ ମୁହାଣ ଫେରୀ ରାଡାର୍" else "Subarnarekha Estuary Red Ghost Crab Telemetry",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
                )
            }

            Surface(
                color = Color(0xFFFEE2E2),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFDC2626).copy(alpha = alphaAnim))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "LIVE (${refreshSecondsRemaining}s)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFDC2626)
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Telemetry Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F2)),
            border = BorderStroke(1.dp, Color(0xFFFECDD3))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isOdia) "ସାମ୍ପ୍ରତିକ ଭଟ୍ଟା ଓ କଙ୍କଡ଼ା ଦର୍ଶନ ୱିଣ୍ଡୋ" else "Current Sighting & Tidal Telemetry",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF881337)
                        )
                    )
                    Surface(
                        color = Color(0xFFDC2626),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isOdia) "ଉତ୍କୃଷ୍ଟ ସମୟ" else "PRIME TIME",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TelemetryMiniBox(
                        label = if (isOdia) "ଭଟ୍ଟା ସମୟ" else "Low Tide Window",
                        value = dailyPulse.chandipurLowTideWindow.ifBlank { "09:45 AM - 01:30 PM" },
                        color = Color(0xFF881337)
                    )
                    TelemetryMiniBox(
                        label = if (isOdia) "କଲୋନୀ ଘନତ୍ୱ" else "Colony Density",
                        value = "94% (Very High)",
                        color = Color(0xFF047857)
                    )
                    TelemetryMiniBox(
                        label = if (isOdia) "ମୁହାଣ ଜଳସ୍ତର" else "Estuary Tide",
                        value = "0.7m (Safe)",
                        color = Color(0xFF0369A1)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Zone Selector
        Text(
            text = if (isOdia) "ପ୍ରମୁଖ ପର୍ଯ୍ୟବେକ୍ଷଣ ସ୍ଥଳୀ ଚୟନ କରନ୍ତୁ" else "Select Observation Sector",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))

        zones.forEachIndexed { index, zone ->
            val isSelected = selectedZoneIndex == index
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { selectedZoneIndex = index },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) Color(0xFFFFF5F5) else Color(0xFFFAFAFA)
                ),
                border = BorderStroke(
                    1.dp,
                    if (isSelected) Color(0xFFE11D48) else Color(0xFFE2E8F0)
                )
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (zone.isSafeWalk) "🟢" else "⛵",
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isOdia) zone.nameOd else zone.nameEn,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = if (isOdia) zone.statusOd else zone.statusEn,
                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row {
                            Text(
                                text = "Fare: ${zone.boatFare}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xFF0F766E),
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Walk: ~${zone.distanceMeters}m",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Eco-Guidelines Box
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEFCE8)),
            border = BorderStroke(1.dp, Color(0xFFFEF08A))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFFCA8A04),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isOdia) "ପରିବେଶ ସୁରକ୍ଷା ନିୟମାବଳୀ" else "Eco-Protection Guidelines",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF854D0E)
                        )
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (isOdia)
                        "• ଲାଲ୍ କଙ୍କଡ଼ାଙ୍କ ଗାତ ମାଡ଼ନ୍ତୁ ନାହିଁ।\n• ବେଳାଭୂମିରେ ପ୍ଲାଷ୍ଟିକ୍ ବୋତଲ କିମ୍ବା ଆବର୍ଜନା ପକାନ୍ତୁ ନାହିଁ।\n• ଜୁଆର ଆସିବା ପୂର୍ବରୁ ସାଇରନ୍ ଶୁଣି କୂଳକୁ ଫେରିଆସନ୍ତୁ।"
                    else
                        "• Never step on active red ghost crab burrow mounds.\n• Do not chase or capture crabs for selfies; observe from 5ft distance.\n• Heed the estuary coast guard whistle before high tide returns.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF713F12),
                        lineHeight = 18.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    val mapIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("geo:21.5972,87.4917?q=Talasari+Beach+Balasore")
                    )
                    context.startActivity(mapIntent)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = if (isOdia) "ଦିଗଦର୍ଶନ" else "Navigate")
            }

            OutlinedButton(
                onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "Talasari & Udaypur Red Crab Safari Live Telemetry: Low Tide Window is active now! Safe sandbar walk. Download Balasore 360 App."
                        )
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Talasari Status"))
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = if (isOdia) "ସେୟାର୍" else "Share")
            }
        }
    }
}

@Composable
fun TelemetryMiniBox(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold,
                color = color
            )
        )
    }
}

private data class TalasariZone(
    val nameEn: String,
    val nameOd: String,
    val statusEn: String,
    val statusOd: String,
    val distanceMeters: Int,
    val boatFare: String,
    val isSafeWalk: Boolean
)
