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
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Refresh
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
fun BalaramgadiFishMandiSheet(
    dailyPulse: DailyBalasorePulse,
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val isOdia = language == AppLanguage.ODIA

    var refreshTimer by remember { mutableIntStateOf(30) }
    var trawlersDocked by remember { mutableIntStateOf(48) }
    var selectedFilter by remember { mutableStateOf("All") }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            if (refreshTimer > 1) {
                refreshTimer--
            } else {
                refreshTimer = 30
                trawlersDocked = (42..56).random()
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

    val fishItems = listOf(
        FishCatchRate(
            nameEn = "Padma / Estuary Hilsa (Ilish)",
            nameOd = "ସୁବର୍ଣ୍ଣରେଖା / ମୁହାଣ ଇଲିଶି",
            grade = "Grade 1 (>1.2 kg)",
            wholesaleRate = "₹1,250 - ₹1,400 / kg",
            retailRate = "₹1,500 - ₹1,650 / kg",
            freshness = "Just Docked 🟢",
            category = "Premium"
        ),
        FishCatchRate(
            nameEn = "Deep-Sea Tiger Prawn (Bagda)",
            nameOd = "ଗଭୀର ସମୁଦ୍ର ବାଘ ଚିଙ୍ଗୁଡ଼ି",
            grade = "Export Grade (8-12 count)",
            wholesaleRate = "₹680 - ₹750 / kg",
            retailRate = "₹850 - ₹950 / kg",
            freshness = "High Availability 🟢",
            category = "Prawn"
        ),
        FishCatchRate(
            nameEn = "White Pomfret (Chandi Chanda)",
            nameOd = "ଧଳା ଚାନ୍ଦି ଚାନ୍ଦା ମାଛ",
            grade = "Medium (300-500g)",
            wholesaleRate = "₹520 - ₹580 / kg",
            retailRate = "₹650 - ₹720 / kg",
            freshness = "Fresh Catch 🟢",
            category = "Premium"
        ),
        FishCatchRate(
            nameEn = "Subarnarekha Estuary Mud Crab (Kankada)",
            nameOd = "ମୁହାଣ କାଦୁଅ ଲାଲ୍ କଙ୍କଡ଼ା",
            grade = "Live Hard Shell (XL)",
            wholesaleRate = "₹480 - ₹550 / kg",
            retailRate = "₹600 - ₹680 / kg",
            freshness = "100% Live 🟢",
            category = "Crab"
        ),
        FishCatchRate(
            nameEn = "Sea Bass / Bhetki (Vetki)",
            nameOd = "ମଧୁର ମୁହାଣ ଭେଟକି ମାଛ",
            grade = "Table Size (1.5-2.5 kg)",
            wholesaleRate = "₹440 - ₹490 / kg",
            retailRate = "₹550 - ₹620 / kg",
            freshness = "Morning Auction 🟢",
            category = "Popular"
        )
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
                    Text(text = "🐟", fontSize = 26.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isOdia) "ବଳରାମଗଡ଼ି ମତ୍ସ୍ୟ ମଣ୍ଡି ଓ ଟ୍ରଲର ଦର" else "Balaramgadi Fish Mandi Rates",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0369A1)
                        )
                    )
                }
                Text(
                    text = if (isOdia) "ନୁନିଆଛତକ ମୁହାଣ ଦୈନିକ ହୋଲସେଲ୍ ଓ ଖୁଚୁରା ବଜାର ଦର" else "Daily Fresh Catch Wholesale & Retail Auction Price Index",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
                )
            }

            Surface(
                color = Color(0xFFE0F2FE),
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
                            .background(Color(0xFF0284C7).copy(alpha = alphaAnim))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "LIVE (${refreshTimer}s)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0369A1)
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Harbor Telemetry
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)),
            border = BorderStroke(1.dp, Color(0xFFBAE6FD))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isOdia) "ମତ୍ସ୍ୟ ବନ୍ଦର ଟ୍ରଲର ଅବତରଣ ସ୍ଥିତି" else "Harbor Docking & Landings Radar",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF075985)
                        )
                    )
                    Surface(
                        color = Color(0xFF0284C7),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isOdia) "ନିଲାମ ଚାଲୁଅଛି" else "AUCTION ACTIVE",
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
                    FishMetricBox(
                        label = if (isOdia) "ପ୍ରବେଶ ଟ୍ରଲର" else "Docked Trawlers",
                        value = "$trawlersDocked Boats",
                        sub = if (isOdia) "ବୁଢ଼ାବଳଙ୍ଗ ମୁହାଣ" else "Budhabalanga Mouth",
                        color = Color(0xFF0369A1)
                    )
                    FishMetricBox(
                        label = if (isOdia) "ପ୍ରମୁଖ ଆଗମନ" else "Top Arrival",
                        value = "Hilsa & Prawn",
                        sub = if (isOdia) "ପ୍ରଚୁର ଆମଦାନୀ" else "Heavy Landings",
                        color = Color(0xFF047857)
                    )
                    FishMetricBox(
                        label = if (isOdia) "ବରଫ କାରଖାନା" else "Ice Supply",
                        value = "Normal (₹140/block)",
                        sub = if (isOdia) "୮ କାରଖାନା ସକ୍ରିୟ" else "8 Plants Running",
                        color = Color(0xFFD97706)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Daily Rate Catalog
        Text(
            text = if (isOdia) "ଆଜିର ତାଜା ମାଛ ଓ କଙ୍କଡ଼ା ନିଲାମ ଦର" else "Today's Fresh Fish & Crab Rates",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))

        fishItems.forEach { fish ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (isOdia) fish.nameOd else fish.nameEn,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = fish.grade,
                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
                            )
                        }
                        Surface(
                            color = Color(0xFFECFDF5),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = fish.freshness,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF059669)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = if (isOdia) "ପାଇକାରୀ ଦର (ହୋଲସେଲ)" else "Wholesale (Arath)",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
                            )
                            Text(
                                text = fish.wholesaleRate,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0369A1)
                                )
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = if (isOdia) "ଖୁଚୁରା ବଜାର ଦର" else "Retail Estimate",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
                            )
                            Text(
                                text = fish.retailRate,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF059669)
                                )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Buying Advisory Box
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
            border = BorderStroke(1.dp, Color(0xFFBBF7D0))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF16A34A),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isOdia) "ଗ୍ରାହକ ପରାମର୍ଶ (କିଣିବା ସମୟ)" else "Buyer & Wholesale Advisory",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF166534)
                        )
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isOdia)
                        "ସକାଳ ୫:୩୦ ରୁ ୮:୦୦ ମଧ୍ୟରେ ବଳରାମଗଡ଼ି ମୁହାଣ ଘାଟରେ ସର୍ବୋତ୍ତମ ତାଜା ମାଛ ନିଲାମ ହୁଏ। ଖୁଚୁରା ପାଇଁ ବଡ଼ ବଜାର କିମ୍ବା ଷ୍ଟେସନ ବଜାର ମାଛ ହାଟ ଉପଯୁକ୍ତ।"
                    else
                        "Best wholesale auctions occur between 5:30 AM and 8:00 AM at the Budhabalanga river mouth. Retail buyers can also visit Station Bazar or Cinema Chhak morning marts.",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF14532D))
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
                        Uri.parse("geo:21.4683,87.0317?q=Balaramgadi+Fishing+Harbour+Balasore")
                    )
                    context.startActivity(mapIntent)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = if (isOdia) "ଘାଟ ନାଭିଗେସନ୍" else "Harbor GPS")
            }

            OutlinedButton(
                onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "Balaramgadi Marine Mandi Live Rates: Fresh Hilsa ₹1250/kg, Tiger Prawn ₹680/kg. Over 48 trawlers docked today! Balasore 360 App."
                        )
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Mandi Rates"))
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = if (isOdia) "ଦର ସେୟାର୍" else "Share Rates")
            }
        }
    }
}

@Composable
fun FishMetricBox(label: String, value: String, sub: String, color: Color) {
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

private data class FishCatchRate(
    val nameEn: String,
    val nameOd: String,
    val grade: String,
    val wholesaleRate: String,
    val retailRate: String,
    val freshness: String,
    val category: String
)
