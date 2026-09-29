package com.example.ui.features.nature

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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.icons.filled.WaterDrop
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
fun PanchalingeswarWaterfallTrekSheet(
    dailyPulse: DailyBalasorePulse,
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val isOdia = language == AppLanguage.ODIA

    var refreshTimer by remember { mutableIntStateOf(45) }
    var currentFlowRateLps by remember { mutableIntStateOf(145) }
    var queueWaitMins by remember { mutableIntStateOf(15) }

    // Auto-update countdown and flow sensor simulation
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            if (refreshTimer > 1) {
                refreshTimer--
            } else {
                refreshTimer = 45
                // Slight dynamic variation to simulate live telemetry
                currentFlowRateLps = 140 + (0..15).random()
                queueWaitMins = (10..25).random()
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
                    Text(text = "⛰️", fontSize = 26.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isOdia) "ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ଝରଣା ଓ ଟ୍ରେକ୍ ରାଡାର୍" else "Panchalingeswar Waterfall & Trek",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F766E)
                        )
                    )
                }
                Text(
                    text = if (isOdia) "ଦେବଗିରି ପାହାଡ଼ ଜଳପ୍ରପାତ ବେଗ ଓ ୨୬୩ ପାହାଚ ସୁରକ୍ଷା" else "Devagiri Hill Waterfall Flow & 263-Step Climb Advisory",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
                )
            }

            Surface(
                color = Color(0xFFCCFBF1),
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
                            .background(Color(0xFF0D9488).copy(alpha = alphaAnim))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "LIVE (${refreshTimer}s)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F766E)
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Live Gauge Metrics Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDFA)),
            border = BorderStroke(1.dp, Color(0xFF99F6E4))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isOdia) "ପାହାଡ଼ୀ ଝରଣା ଓ ପବିତ୍ର ଶୈବପୀଠ ସ୍ଥିତି" else "Hill Stream & Sanctum Telemetry",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF115E59)
                        )
                    )
                    Surface(
                        color = Color(0xFF0D9488),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isOdia) "ସୁଗମ ଦର୍ଶନ" else "SMOOTH FLOW",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    WaterfallMetricBox(
                        label = if (isOdia) "ଜଳପ୍ରପାତ ବେଗ" else "Waterfall Flow",
                        value = "$currentFlowRateLps L/sec",
                        subtext = if (isOdia) "ସ୍ୱଚ୍ଛ ପାହାଡ଼ୀ ଧାରା" else "Perennial Stream",
                        color = Color(0xFF0D9488)
                    )
                    WaterfallMetricBox(
                        label = if (isOdia) "୨୬୩ ପାହାଚ ସ୍ଥିତି" else "263 Step Climb",
                        value = "Dry & Safe",
                        subtext = if (isOdia) "ରେଲିଂ ସୁରକ୍ଷିତ" else "Railing Intact",
                        color = Color(0xFF0284C7)
                    )
                    WaterfallMetricBox(
                        label = if (isOdia) "ଦର୍ଶନ ଧାଡ଼ି" else "Queue Wait",
                        value = "~$queueWaitMins Mins",
                        subtext = if (isOdia) "ସହଜ ପ୍ରବେଶ" else "Moderate Rush",
                        color = Color(0xFFD97706)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Devagiri Hill Trek Checkpoints
        Text(
            text = if (isOdia) "ଦେବଗିରି ଟ୍ରେକିଂ ଚେକପଏଣ୍ଟ ଓ ସୁବିଧା" else "Devagiri Trek Checkpoints & Facilities",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))

        val checkpoints = listOf(
            TrekCheckpoint(
                titleEn = "Base Foothill & OTDC Panthanivas",
                titleOd = "ପାଦଦେଶ ଓ OTDC ପାନ୍ଥନିବାସ",
                descEn = "Parking available, shoe counters, cane sticks & local coconut vendors.",
                descOd = "ପାର୍କିଂ, ଜୋତା ଷ୍ଟାଣ୍ଡ, ବାଉଁଶ ବାଡ଼ି ଓ ତାଜା ଡାବ ପାଣି ସୁବିଧା।",
                elevation = "Base (0m)"
            ),
            TrekCheckpoint(
                titleEn = "Step 120 Rest Shed & Spring Fountain",
                titleOd = "୧୨୦ ପାହାଚ ବିଶ୍ରାମ କୁଡ଼ିଆ ଓ ଝରଣା",
                descEn = "Covered sitting pavilion, fresh mountain water spout, first-aid box.",
                descOd = "ବସିବା ସ୍ଥାନ, ବିଶୁଦ୍ଧ ପାହାଡ଼ୀ ଝରଣା ଜଳ ଓ ପ୍ରାଥମିକ ଚିକିତ୍ସା କେନ୍ଦ୍ର।",
                elevation = "+65m"
            ),
            TrekCheckpoint(
                titleEn = "Top Sanctum: 5 Lingams in Running Water",
                titleOd = "ଶୀର୍ଷ ପୀଠ: ଝରଣା ତଳେ ୫ ଶିବଲିଙ୍ଗ",
                descEn = "Touch and feel the five naturally submerged Shiva lingams beneath the cold waterfall.",
                descOd = "ଜଳପ୍ରପାତ ତଳେ ଅବସ୍ଥିତ ପଞ୍ଚଲିଙ୍ଗ ସ୍ପର୍ଶ ଦର୍ଶନ ଓ ପୂଜା ଅର୍ଚ୍ଚନା।",
                elevation = "Sanctum (+140m)"
            )
        )

        checkpoints.forEach { cp ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0F2FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Terrain,
                            contentDescription = null,
                            tint = Color(0xFF0284C7),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (isOdia) cp.titleOd else cp.titleEn,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = cp.elevation,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F766E)
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isOdia) cp.descOd else cp.descEn,
                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Eco-Camp Booking Note
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFECFDF5)),
            border = BorderStroke(1.dp, Color(0xFFA7F3D0))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "⛺", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isOdia) "ନୀଳଗିରି ଇକୋ-ଟୁରିଜିମ୍ କ୍ୟାମ୍ପ୍" else "OFDC Nature Camp Booking",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF065F46)
                        )
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isOdia)
                        "ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ପାହାଡ଼ ତଳେ ତମ୍ବୁ ରହଣି ଓ ନୀଳଗିରି ରାଜପ୍ରାସାଦ ଭ୍ରମଣ ପାଇଁ ରାଜ୍ୟ ଇକୋ-ଟୁର୍ ପୋର୍ଟାଲ୍ ବ୍ୟବହାର କରନ୍ତୁ।"
                    else
                        "Enjoy night camping in Swiss cottages overlooking the Devagiri ridge. Book online via ecotourodisha.com or contact Nilagiri Forest Range Office.",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF047857))
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
                        Uri.parse("geo:21.4167,86.7167?q=Panchalingeswar+Temple+Nilagiri")
                    )
                    context.startActivity(mapIntent)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D9488)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = if (isOdia) "ମ୍ୟାପ୍ ନାଭିଗେସନ୍" else "Directions")
            }

            OutlinedButton(
                onClick = {
                    val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:06782233215"))
                    context.startActivity(callIntent)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = if (isOdia) "ମନ୍ଦିର ଡେସ୍କ" else "Temple Desk")
            }
        }
    }
}

@Composable
fun WaterfallMetricBox(label: String, value: String, subtext: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = color
            )
        )
        Text(
            text = subtext,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, color = Color(0xFF94A3B8))
        )
    }
}

private data class TrekCheckpoint(
    val titleEn: String,
    val titleOd: String,
    val descEn: String,
    val descOd: String,
    val elevation: String
)
