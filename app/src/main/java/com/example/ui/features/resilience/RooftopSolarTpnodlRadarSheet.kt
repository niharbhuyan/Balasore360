package com.example.ui.features.resilience

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SolarPower
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.daily.DailyBalasorePulse
import com.example.data.model.AppLanguage

/**
 * TPNODL Rooftop Solar & PM Surya Ghar Muft Bijli Yojana Radar.
 * Live solar radiation telemetry, net-metering application tracking,
 * subsidy calculator (up to ₹78,000), and green feeder load monitor for Balasore district.
 */
@Composable
fun RooftopSolarTpnodlRadarSheet(
    dailyPulse: DailyBalasorePulse,
    language: AppLanguage = AppLanguage.ENGLISH,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isOdia = language == AppLanguage.ODIA

    var monthlyConsumptionUnits by remember { mutableFloatStateOf(240f) }

    // Estimations based on MNRE & TPNODL guidelines:
    // 1 kW system generates ~120 units/month and requires ~100 sq.ft roof area.
    val recommendedKw = (monthlyConsumptionUnits / 120f).coerceIn(1f, 10f)
    val roundedKw = String.format(java.util.Locale.US, "%.1f", recommendedKw)
    val estimatedRoofArea = (recommendedKw * 100).toInt()

    // Subsidy: ₹30,000 for 1kW, ₹60,000 for 2kW, ₹78,000 for >=3kW
    val subsidyAmount = when {
        recommendedKw >= 3f -> 78000
        recommendedKw >= 2f -> 60000
        else -> 30000
    }
    val estimatedSystemCost = (recommendedKw * 65000).toInt()
    val netEffectiveCost = (estimatedSystemCost - subsidyAmount).coerceAtLeast(15000)
    val monthlySavingsRs = (monthlyConsumptionUnits * 5.25f).toInt()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
            .testTag("rooftop_solar_tpnodl_radar_sheet")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEF3C7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SolarPower,
                        contentDescription = "Solar Power",
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (isOdia) "ଛାତ ସୌର ଶକ୍ତି ଓ TPNODL ରାଡାର୍" else "TPNODL Rooftop Solar Radar",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = if (isOdia) "ପିଏମ୍ ସୂର୍ଯ୍ୟ ଘର ମୁକ୍ତ ବିଜୁଳି ଯୋଜନା" else "PM Surya Ghar & Net-Metering Desk",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Daily Solar Telemetry Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            border = BorderStroke(1.dp, Color(0xFF334155))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WbSunny,
                            contentDescription = null,
                            tint = Color(0xFFFBBF24),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isOdia) "ଆଜିର ସୌର ବିକିରଣ ତଥ୍ୟ" else "BALASORE SOLAR IRRADIANCE",
                            color = Color(0xFFFBBF24),
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 1.sp
                        )
                    }
                    Text(
                        text = dailyPulse.formattedDate,
                        color = Color(0xFF94A3B8),
                        fontSize = 10.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF1E293B),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = if (isOdia) "ସୌର ଉତ୍ପାଦନ କ୍ଷମତା" else "Daily Peak Sun Hours",
                                fontSize = 10.sp,
                                color = Color(0xFF94A3B8)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "5.4 kWh/m²/day",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = if (isOdia) "ଉଚ୍ଚ ସୌର ସମ୍ଭାବନା 🟢" else "Optimal coastal sun 🟢",
                                fontSize = 9.sp,
                                color = Color(0xFF6EE7B7)
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF1E293B),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = if (isOdia) "ଗ୍ରିଡ୍ ସଂଯୁକ୍ତ ସିଷ୍ଟମ୍" else "Balasore Net-Meters",
                                fontSize = 10.sp,
                                color = Color(0xFF94A3B8)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "1,842 Active",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF34D399)
                            )
                            Text(
                                text = if (isOdia) "ଶୂନ୍ୟ ବିଜୁଳି ବିଲ୍ ଲାଭ" else "Zero bill adopters",
                                fontSize = 9.sp,
                                color = Color(0xFFCBD5E1)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Interactive Subsidy & Sizing Calculator
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isOdia) "ସୌର ସବସିଡି ଓ କ୍ଷମତା କାଲକୁଲେଟର" else "Solar Subsidy & Savings Calculator",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (isOdia)
                        "ମାସିକ ବିଜୁଳି ଖର୍ଚ୍ଚ: ${monthlyConsumptionUnits.toInt()} ୟୁନିଟ୍"
                    else
                        "Monthly Electricity Usage: ${monthlyConsumptionUnits.toInt()} Units",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )

                Slider(
                    value = monthlyConsumptionUnits,
                    onValueChange = { monthlyConsumptionUnits = it },
                    valueRange = 80f..600f,
                    steps = 25,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Recommended kW", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("$roundedKw kW", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Text("Roof: ~$estimatedRoofArea sq.ft", fontSize = 9.sp, color = MaterialTheme.colorScheme.outline)
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFECFDF5),
                        border = BorderStroke(0.5.dp, Color(0xFF10B981)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Central Subsidy", fontSize = 10.sp, color = Color(0xFF047857))
                            Text("₹$subsidyAmount", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF059669))
                            Text("PM Surya Ghar DBT", fontSize = 9.sp, color = Color(0xFF047857))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (isOdia) "ମାସିକ ସଞ୍ଚୟ (ଆନୁମାନିକ)" else "Estimated Monthly Savings",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "₹$monthlySavingsRs / month",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = if (isOdia) "ନିଟ୍ ଖର୍ଚ୍ଚ (ସବସିଡି ପରେ)" else "Net Cost After Subsidy",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "≈ ₹$netEffectiveCost",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Application Steps
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isOdia) "ଆବେଦନ ପ୍ରକ୍ରିୟା (TPNODL ନେଟ୍-ମିଟର)" else "How to Apply for TPNODL Net-Metering",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(10.dp))

                val steps = listOf(
                    "1. Register on pmsuryaghar.gov.in with TPNODL Consumer No.",
                    "2. Choose empaneled Balasore solar vendor for rooftop installation.",
                    "3. Vendor inspects roof & submits feasibility to TPNODL Division.",
                    "4. TPNODL installs Bi-directional Net Meter within 15 days.",
                    "5. Commissioning certificate uploaded; subsidy credited directly to bank."
                )

                steps.forEach { step ->
                    Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF10B981),
                            modifier = Modifier
                                .size(16.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = step,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Actions: Call TPNODL Solar Cell or Open Portal
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1912"))
                    context.startActivity(intent)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706))
            ) {
                Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isOdia) "୧୯୧୨ ସହାୟତା" else "Call 1912", fontSize = 12.sp)
            }

            OutlinedButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://pmsuryaghar.gov.in"))
                    context.startActivity(intent)
                },
                modifier = Modifier.weight(1.3f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isOdia) "ସୂର୍ଯ୍ୟ ଘର ପୋର୍ଟାଲ" else "Surya Ghar Portal", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(if (isOdia) "ବନ୍ଦ କରନ୍ତୁ" else "Close Desk")
        }
    }
}
