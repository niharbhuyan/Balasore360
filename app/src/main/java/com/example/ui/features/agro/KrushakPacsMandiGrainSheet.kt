package com.example.ui.features.agro

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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInNew
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
fun KrushakPacsMandiGrainSheet(
    dailyPulse: DailyBalasorePulse,
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val isOdia = language == AppLanguage.ODIA

    var refreshTimer by remember { mutableIntStateOf(45) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            if (refreshTimer > 1) {
                refreshTimer--
            } else {
                refreshTimer = 45
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

    val mandis = listOf(
        PacsMandiInfo(
            blockNameEn = "Remuna Block PACS",
            blockNameOd = "ରେମୁଣା ବ୍ଲକ୍ ସମବାୟ ମଣ୍ଡି (PACS)",
            mandiCenter = "Mandarpur & Patripal Sub-Mandi",
            arrivalBags = 2450,
            statusEn = "Open • Token Range #101-#350",
            statusOd = "ଖୋଲା ଅଛି • ଟୋକନ୍ #୧୦୧-#୩୫୦ ଚାଲୁଛି",
            avgMoisture = "14.2% (Accepted)",
            isTokenActive = true
        ),
        PacsMandiInfo(
            blockNameEn = "Soro Municipal Mandi",
            blockNameOd = "ସୋର ପୌର କୃଷି ମଣ୍ଡି",
            mandiCenter = "Ananthapur RMC Yard",
            arrivalBags = 3800,
            statusEn = "Open • Direct Bank Transfer Active",
            statusOd = "ଖୋଲା ଅଛି • ସିଧା ବ୍ୟାଙ୍କ ଖାତାକୁ ଟଙ୍କା",
            avgMoisture = "15.0% (Accepted)",
            isTokenActive = true
        ),
        PacsMandiInfo(
            blockNameEn = "Jaleswar Sub-Divisional Mandi",
            blockNameOd = "ଜଳେଶ୍ୱର ଉପଖଣ୍ଡ ଧାନ ମଣ୍ଡି",
            mandiCenter = "Raikamaleswar PACS Godown",
            arrivalBags = 1950,
            statusEn = "Weighing In Progress",
            statusOd = "ଓଜନ କାର୍ଯ୍ୟ ଚାଲୁଅଛି",
            avgMoisture = "15.8% (Borderline)",
            isTokenActive = true
        ),
        PacsMandiInfo(
            blockNameEn = "Basta & Baliapal Border Mandi",
            blockNameOd = "ବସ୍ତା ଓ ବାଲିଆପାଳ କୃଷକ ମଣ୍ଡି",
            mandiCenter = "Amarda Road RMC Sub-Center",
            arrivalBags = 1600,
            statusEn = "Open • Quality Testing Camp",
            statusOd = "ଖୋଲା ଅଛି • ଗୁଣବତ୍ତା ଯାଞ୍ଚ ଶିବିର",
            avgMoisture = "14.6% (Accepted)",
            isTokenActive = true
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
                    Text(text = "🌾", fontSize = 26.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isOdia) "କୃଷକ PACS ଧାନ ମଣ୍ଡି ଓ ଏମଏସପି ଡେସ୍କ" else "Krushak PACS Mandi & MSP Radar",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF15803D)
                        )
                    )
                }
                Text(
                    text = if (isOdia) "ସରକାରୀ ଧାନ କ୍ରୟ, ଆର୍ଦ୍ରତା ମାପ ଓ ଟୋକନ୍ ସ୍ଥିତି" else "Official Paddy Procurement, Moisture Testing & Token Tracking",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
                )
            }

            Surface(
                color = Color(0xFFDCFCE7),
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
                            .background(Color(0xFF16A34A).copy(alpha = alphaAnim))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "RMC (${refreshTimer}s)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF15803D)
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hero MSP Box
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
            border = BorderStroke(1.dp, Color(0xFFBBF7D0))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isOdia) "ସରକାରୀ ଧାନ କ୍ରୟ ମୂଲ୍ୟ (ନିର୍ଦ୍ଧାରିତ MSP)" else "Official Paddy Procurement Price (MSP)",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF14532D)
                        )
                    )
                    Surface(
                        color = Color(0xFF16A34A),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "GOVT APPROVED",
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
                    MandiMetricBox(
                        label = if (isOdia) "ସାଧାରଣ ଧାନ (MSP)" else "Common Paddy",
                        value = "₹2,300 / Qtl",
                        sub = if (isOdia) "ସିଧାସଳଖ DBT କ୍ରୟ" else "Direct DBT Bank Transfer",
                        color = Color(0xFF15803D)
                    )
                    MandiMetricBox(
                        label = if (isOdia) "ଗ୍ରେଡ୍-ଏ ଧାନ (MSP)" else "Grade-A Paddy",
                        value = "₹2,320 / Qtl",
                        sub = if (isOdia) "+ ଇନପୁଟ୍ ସହାୟତା" else "+ Input Subsidy",
                        color = Color(0xFF047857)
                    )
                    MandiMetricBox(
                        label = if (isOdia) "ସର୍ବାଧିକ ଆର୍ଦ୍ରତା" else "Max Moisture",
                        value = "< 17.0%",
                        sub = if (isOdia) "FAQ ମାନକ ଅନୁମୋଦିତ" else "FAQ Standard",
                        color = Color(0xFF0284C7)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mandi Center Live Status
        Text(
            text = if (isOdia) "ଜିଲ୍ଲାର ପ୍ରମୁଖ PACS ଓ RMC କ୍ରୟ କେନ୍ଦ୍ର" else "Major Block PACS & Procurement Yards",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))

        mandis.forEach { m ->
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
                                text = if (isOdia) m.blockNameOd else m.blockNameEn,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = m.mandiCenter,
                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
                            )
                        }
                        Surface(
                            color = Color(0xFFDCFCE7),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "${m.arrivalBags} Bags",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF15803D)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (isOdia) m.statusOd else m.statusEn,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF166534)
                            )
                        )
                        Text(
                            text = "Moisture: ${m.avgMoisture}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F766E)
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Moisture & FAQ Advisory Box
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
            border = BorderStroke(1.dp, Color(0xFFFDE68A))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isOdia) "ଚାଷୀ ଭାଇଙ୍କ ପାଇଁ ଜରୁରୀ ପରାମର୍ଶ" else "Moisture & FAQ Quality Advisory",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E)
                        )
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isOdia)
                        "ଧାନ ମଣ୍ଡିକୁ ନେବା ପୂର୍ବରୁ ଭଲ ଭାବରେ ଶୁଖାନ୍ତୁ। ଆର୍ଦ୍ରତା ୧୭% ରୁ କମ୍ ରହିଲେ ତତକ୍ଷଣାତ୍ ଓଜନ ଓ ସିଲ୍ ହୋଇ ୪୮ ଘଣ୍ଟା ମଧ୍ୟରେ ବ୍ୟାଙ୍କ ଖାତାରେ ଟଙ୍କା ଜମା ହୁଏ। କଟନୀ-ଛଟନୀ ଅଭିଯୋଗ ପାଇଁ ୧୯୬୭ କଲ୍ କରନ୍ତୁ।"
                    else
                        "Ensure paddy is properly sun-dried. Moisture must strictly stay below 17% FAQ limits for immediate token clearance and 48-hr direct bank transfer. Report illicit deduction (Katni-Chhatni) to civil supplies toll-free 1967.",
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
                    val portalIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://foododisha.in/"))
                    context.startActivity(portalIntent)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF15803D)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = if (isOdia) "ମଣ୍ଡି ପୋର୍ଟାଲ୍" else "Mandi Portal")
            }

            OutlinedButton(
                onClick = {
                    val callCivilSupplies = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1967"))
                    context.startActivity(callCivilSupplies)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = if (isOdia) "ଅଭିଯୋଗ ୧୯୬୭" else "Toll-Free 1967")
            }
        }
    }
}

@Composable
fun MandiMetricBox(label: String, value: String, sub: String, color: Color) {
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

private data class PacsMandiInfo(
    val blockNameEn: String,
    val blockNameOd: String,
    val mandiCenter: String,
    val arrivalBags: Int,
    val statusEn: String,
    val statusOd: String,
    val avgMoisture: String,
    val isTokenActive: Boolean
)
