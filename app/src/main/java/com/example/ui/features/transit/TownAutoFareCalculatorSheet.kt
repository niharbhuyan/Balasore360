package com.example.ui.features.transit

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
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
fun TownAutoFareCalculatorSheet(
    dailyPulse: DailyBalasorePulse,
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val isOdia = language == AppLanguage.ODIA

    var refreshTimer by remember { mutableIntStateOf(60) }
    var isNightFare by remember { mutableStateOf(false) }
    var selectedVehicleType by remember { mutableStateOf("Auto") } // "Auto" or "Toto"
    var selectedRouteIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            if (refreshTimer > 1) {
                refreshTimer--
            } else {
                refreshTimer = 60
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

    val routes = listOf(
        AutoRoute(
            fromEn = "Balasore Railway Station",
            toEn = "Chandipur Beach",
            fromOd = "ବାଲେଶ୍ୱର ରେଳ ଷ୍ଟେସନ",
            toOd = "ଚାନ୍ଦିପୁର ବେଳାଭୂମି",
            distanceKm = 16.0f,
            reservedAutoDay = 250,
            sharedSeatDay = 40,
            totoDay = 180
        ),
        AutoRoute(
            fromEn = "Station Bazar",
            toEn = "Remuna Golei / OT Road",
            fromOd = "ଷ୍ଟେସନ ବଜାର",
            toOd = "ରେମୁଣା ଗୋଲେଇ / ଓଟି ରୋଡ୍",
            distanceKm = 6.2f,
            reservedAutoDay = 100,
            sharedSeatDay = 20,
            totoDay = 70
        ),
        AutoRoute(
            fromEn = "ITI Chhak",
            toEn = "FM Medical College (Remuna)",
            fromOd = "ଆଇଟିଆଇ ଛକ",
            toOd = "ଏଫଏମ ମେଡିକାଲ କଲେଜ କ୍ୟାମ୍ପସ",
            distanceKm = 7.5f,
            reservedAutoDay = 120,
            sharedSeatDay = 25,
            totoDay = 80
        ),
        AutoRoute(
            fromEn = "Cinema Chhak / Town Hall",
            toEn = "Motiganj Market / Phandi",
            fromOd = "ସିନେମା ଛକ / ଟାଉନ୍ ହଲ୍",
            toOd = "ମୋତିଗଞ୍ଜ ବଜାର / ଫାଣ୍ଡି",
            distanceKm = 3.0f,
            reservedAutoDay = 50,
            sharedSeatDay = 15,
            totoDay = 40
        ),
        AutoRoute(
            fromEn = "Balasore Bus Stand (Sahadevkhunta)",
            toEn = "Kuruda Industrial Area",
            fromOd = "ସହଦେବଖୁଣ୍ଟା ବସ୍ ଟର୍ମିନାଲ୍",
            toOd = "କୁରୁଡ଼ା ଶିଳ୍ପାଞ୍ଚଳ",
            distanceKm = 8.5f,
            reservedAutoDay = 140,
            sharedSeatDay = 25,
            totoDay = 90
        )
    )

    val currentRoute = routes[selectedRouteIndex]
    val multiplier = if (isNightFare) 1.25f else 1.0f

    val calculatedReserved = (currentRoute.reservedAutoDay * multiplier).toInt()
    val calculatedShared = (currentRoute.sharedSeatDay * multiplier).toInt()
    val calculatedToto = (currentRoute.totoDay * multiplier).toInt()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🛺", fontSize = 26.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isOdia) "ଟାଉନ୍ ଅଟୋ ଓ ଟୋଟୋ ଭଡ଼ା କାଲକୁଲେଟର୍" else "Town Auto & Toto Fare Calculator",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC2410C)
                        )
                    )
                }
                Text(
                    text = if (isOdia) "ବାଲେଶ୍ୱର ପୌରପାଳିକା ଓ ଆରଟିଓ ସ୍ୱୀକୃତ ନିରପେକ୍ଷ ଭଡ଼ା ସାରଣୀ" else "RTO Balasore Benchmark Route Fare Estimator",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
                )
            }

            Surface(
                color = Color(0xFFFFEDD5),
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
                            .background(Color(0xFFEA580C).copy(alpha = alphaAnim))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "RTO (${refreshTimer}s)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC2410C)
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Fare Box
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7ED)),
            border = BorderStroke(1.dp, Color(0xFFFED7AA))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isOdia) "ଆନୁମାନିକ ନ୍ୟାଯ୍ୟ ଭଡ଼ା" else "Estimated Fair Fare",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF9A3412)
                            )
                        )
                        Text(
                            text = "${currentRoute.distanceKm} km • ${if (isNightFare) "Night Surcharge (+25%)" else "Standard Day Rate"}",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
                        )
                    }

                    // Night Fare Switch
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isOdia) "ରାତ୍ରି (୯ ପରେ)" else "Night Rate",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = if (isNightFare) Color(0xFFEA580C) else Color(0xFF64748B)
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Switch(
                            checked = isNightFare,
                            onCheckedChange = { isNightFare = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFFEA580C)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FareEstimateBox(
                        title = if (isOdia) "ରିଜର୍ଭ ଅଟୋ" else "Reserved Auto",
                        amount = "₹$calculatedReserved",
                        sub = if (isOdia) "ପୂରା ଅଟୋ (୩-୪ ଜଣ)" else "Full Vehicle",
                        color = Color(0xFFC2410C)
                    )
                    FareEstimateBox(
                        title = if (isOdia) "ଶେୟାରିଂ ଅଟୋ" else "Shared Seat",
                        amount = "₹$calculatedShared",
                        sub = if (isOdia) "ପ୍ରତି ବ୍ୟକ୍ତି ସିଟ୍" else "Per Passenger",
                        color = Color(0xFF047857)
                    )
                    FareEstimateBox(
                        title = if (isOdia) "ଇ-ରିକ୍ସା (ଟୋଟୋ)" else "Toto (E-Rickshaw)",
                        amount = "₹$calculatedToto",
                        sub = if (isOdia) "ସ୍ଥାନୀୟ ରୁଟ୍ ରିଜର୍ଭ" else "Eco-Friendly",
                        color = Color(0xFF0284C7)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Select Route
        Text(
            text = if (isOdia) "ପ୍ରମୁଖ ଯାତାୟାତ ରୁଟ୍ ଚୟନ କରନ୍ତୁ" else "Select Commuter Route",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))

        routes.forEachIndexed { idx, rt ->
            val isSelected = selectedRouteIndex == idx
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { selectedRouteIndex = idx },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) Color(0xFFFFF7ED) else Color(0xFFFAFAFA)
                ),
                border = BorderStroke(
                    1.dp,
                    if (isSelected) Color(0xFFF97316) else Color(0xFFE2E8F0)
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) Color(0xFFFFEDD5) else Color(0xFFF1F5F9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapVert,
                            contentDescription = null,
                            tint = if (isSelected) Color(0xFFEA580C) else Color(0xFF64748B),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isOdia) "${rt.fromOd} ↔ ${rt.toOd}" else "${rt.fromEn} ↔ ${rt.toEn}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${rt.distanceKm} km • Reserved: ₹${rt.reservedAutoDay} | Shared: ₹${rt.sharedSeatDay}",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grievance / Anti-Overcharging Box
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
            border = BorderStroke(1.dp, Color(0xFFFECACA))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFFDC2626),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isOdia) "ଅଧିକ ଭଡ଼ା ଆଦାୟ ଅଭିଯୋଗ ହେଲ୍ପଲାଇନ୍" else "Anti-Overcharging & RTO Grievance",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF991B1B)
                        )
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isOdia)
                        "ଯଦି କୌଣସି ଅଟୋ ଚାଳକ ଅଯୌକ୍ତିକ ଭାବେ ଅତ୍ୟଧିକ ଭଡ଼ା ଦାବି କରନ୍ତି, ତେବେ ଗାଡ଼ି ନମ୍ବର ସହିତ ବାଲେଶ୍ୱର ଆରଟିଓ କିମ୍ବା ଟ୍ରାଫିକ୍ ପୋଲିସ ହେଲ୍ପଲାଇନରେ ଜଣାନ୍ତୁ।"
                    else
                        "Balasore RTO mandates meter benchmarks. In case of aggressive refusal or extortionate surge pricing, report the vehicle registration number directly to Balasore Traffic Police (06782-262024).",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF7F1D1D))
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
                    val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:06782262024"))
                    context.startActivity(callIntent)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA580C)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = if (isOdia) "ଟ୍ରାଫିକ୍ ପୋଲିସ୍" else "Traffic Police")
            }

            OutlinedButton(
                onClick = {
                    val dial112 = Intent(Intent.ACTION_DIAL, Uri.parse("tel:112"))
                    context.startActivity(dial112)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = if (isOdia) "ଡାଏଲ୍ ୧୧୨" else "Emergency 112")
            }
        }
    }
}

@Composable
fun FareEstimateBox(title: String, amount: String, sub: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = amount,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = color
            )
        )
        Text(
            text = sub,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, color = Color(0xFF94A3B8))
        )
    }
}

private data class AutoRoute(
    val fromEn: String,
    val toEn: String,
    val fromOd: String,
    val toOd: String,
    val distanceKm: Float,
    val reservedAutoDay: Int,
    val sharedSeatDay: Int,
    val totoDay: Int
)
