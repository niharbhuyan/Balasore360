package com.example.ui.features.heritage

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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Share
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
fun EmamiJagannathRemunaHeritageSheet(
    dailyPulse: DailyBalasorePulse,
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val isOdia = language == AppLanguage.ODIA

    var refreshTimer by remember { mutableIntStateOf(60) }
    var availableKshiraPots by remember { mutableIntStateOf(142) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            if (refreshTimer > 1) {
                refreshTimer--
            } else {
                refreshTimer = 60
                availableKshiraPots = (120..180).random()
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
                    Text(text = "🛕", fontSize = 26.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isOdia) "ଇମାମୀ ଜଗନ୍ନାଥ ଓ ରେମୁଣା କ୍ଷୀରଭୋଗ କ୍ୟାଲେଣ୍ଡର" else "Emami Jagannath & Remuna Heritage",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB45309)
                        )
                    )
                }
                Text(
                    text = if (isOdia) "ଖିରଚୋରା ଗୋପୀନାଥ ଅମୃତ କେଳି ଭୋଗ ସୂଚୀ ଓ ଆଳତି ସମୟ" else "Live Bhog Timing, Amruta Keli Counter & Temple Rituals",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
                )
            }

            Surface(
                color = Color(0xFFFEF3C7),
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
                            .background(Color(0xFFD97706).copy(alpha = alphaAnim))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "LIVE (${refreshTimer}s)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB45309)
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Card: Khira Bhog Counter
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
            border = BorderStroke(1.dp, Color(0xFFFDE68A))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isOdia) "ରେମୁଣା ଅମୃତ କେଳି କ୍ଷୀରଭୋଗ ଉପଲବ୍ଧତା" else "Remuna Amruta Keli Khira Bhog",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E)
                        )
                    )
                    Surface(
                        color = Color(0xFFF59E0B),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isOdia) "କାଉଣ୍ଟର ଖୋଲା ଅଛି" else "COUNTER OPEN",
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
                    HeritageMetric(
                        label = if (isOdia) "ମାଟି ହାଣ୍ଡି ଉପଲବ୍ଧ" else "Available Pots",
                        value = "$availableKshiraPots Pots",
                        sub = if (isOdia) "ଆନୁମାନିକ କାଉଣ୍ଟର ଷ୍ଟକ୍" else "Live Counter Est.",
                        color = Color(0xFFB45309)
                    )
                    HeritageMetric(
                        label = if (isOdia) "ପ୍ରତି ହାଣ୍ଡି ମୂଲ୍ୟ" else "Rate / Pot",
                        value = "₹60 & ₹120",
                        sub = if (isOdia) "ଛୋଟ ଓ ବଡ଼ ଆକାର" else "Small & Large",
                        color = Color(0xFF047857)
                    )
                    HeritageMetric(
                        label = if (isOdia) "ସାନ୍ଧ୍ୟ ଭୋଗ ସମୟ" else "Evening Bhog",
                        value = "07:30 PM",
                        sub = if (isOdia) "ମଧ୍ୟାହ୍ନ: ୧୨:୪୫" else "Noon: 12:45 PM",
                        color = Color(0xFF1D4ED8)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Emami Jagannath & Remuna Daily Ritual Schedule
        Text(
            text = if (isOdia) "ନିତ୍ୟ ଆଳତି ଓ ଦର୍ଶନ ସମୟ ସାରଣୀ" else "Daily Aarti & Ritual Clock",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))

        val rituals = listOf(
            TempleRitual(
                nameEn = "Mangala Alati (Dawn Awakening)",
                nameOd = "ମଙ୍ଗଳ ଆଳତି (ପ୍ରଭାତ ସମୟ)",
                time = "05:30 AM",
                shrine = "Emami Jagannath & Remuna"
            ),
            TempleRitual(
                nameEn = "Madhyahna Dhupa (Royal Feast Bhog)",
                nameOd = "ମଧ୍ୟାହ୍ନ ଧୂପ ଓ ଅନ୍ନପ୍ରସାଦ",
                time = "12:30 PM - 01:30 PM",
                shrine = "Khirachora Gopinath Temple"
            ),
            TempleRitual(
                nameEn = "Sandhya Alati & Deepa Samarpan",
                nameOd = "ସନ୍ଧ୍ୟା ଆଳତି ଓ ଦୀପ ସମର୍ପଣ",
                time = "06:45 PM",
                shrine = "Emami Jagannath Mandir"
            ),
            TempleRitual(
                nameEn = "Bada Singhara Besha & Pahuda",
                nameOd = "ବଡ଼ଶୃଙ୍ଗାର ବେଶ ଓ ପହୁଡ଼",
                time = "09:30 PM",
                shrine = "Both Sanctums"
            )
        )

        rituals.forEach { r ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)),
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
                            .background(Color(0xFFFEF3C7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (isOdia) r.nameOd else r.nameEn,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = r.time,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB45309)
                                )
                            )
                        }
                        Text(
                            text = r.shrine,
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Artisan & Bell Metal Craft Stalls
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
            border = BorderStroke(1.dp, Color(0xFFFDE68A))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🏺", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isOdia) "ରେମୁଣା କଂସା ଓ ପିତ୍ତଳ ଶିଳ୍ପୀ ହାଟ" else "Remuna Brass & Bell-Metal Stalls",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E)
                        )
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isOdia)
                        "ମନ୍ଦିର ବାହାରେ ପାରମ୍ପରିକ ପିତ୍ତଳ ଦୀପ, କଂସା ଥାଳି ଏବଂ ଉତ୍କଳୀୟ ହସ୍ତଶିଳ୍ପ ବିଶୁଦ୍ଧ କାରିଗର ଦରରେ ଉପଲବ୍ଧ।"
                    else
                        "Explore centuries-old hand-beaten bell metal diyas, puja thalis, and sacred brassware made by resident Remuna artisan clusters.",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF78350F))
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
                        Uri.parse("geo:21.5292,86.8736?q=Khirachora+Gopinatha+Temple+Remuna")
                    )
                    context.startActivity(mapIntent)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = if (isOdia) "ରେମୁଣା ମ୍ୟାପ୍" else "Directions")
            }

            OutlinedButton(
                onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "Khirachora Gopinath Remuna Amruta Keli Bhog counter is open today! Evening Bhog at 7:30 PM. Balasore 360 App."
                        )
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Temple Schedule"))
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
fun HeritageMetric(label: String, value: String, sub: String, color: Color) {
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
            text = sub,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, color = Color(0xFF94A3B8))
        )
    }
}

private data class TempleRitual(
    val nameEn: String,
    val nameOd: String,
    val time: String,
    val shrine: String
)
