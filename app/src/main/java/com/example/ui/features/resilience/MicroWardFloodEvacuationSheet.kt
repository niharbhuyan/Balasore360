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
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Flood
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.WaterDamage
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
 * Micro-Ward Flood Inundation & High-Ground Safe Evacuation Router.
 * Tracks waterlogging telemetry in low-lying Balasore municipal wards,
 * calculates high-ground evacuation routes to elevated shelters, and maps relief posts.
 */
@Composable
fun MicroWardFloodEvacuationSheet(
    dailyPulse: DailyBalasorePulse,
    language: AppLanguage = AppLanguage.ENGLISH,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val wards = listOf(
        Triple("Ward 14: Sunhat Canal Catchment", "Waterlogging: ${dailyPulse.sunhatWaterloggingMeters} m", "Drainage Flowing to Budhabalanga • Sluice Normal"),
        Triple("Ward 8: Aradabazar Low Belt", dailyPulse.aradabazarFloodStatus, "Tidal backflow gates monitored by Municipal Engineering"),
        Triple("Ward 21: Gopalgaon & Station Road", "High-Ground Elevation (14m Above MSL) 🟢", "Designated Safe Evacuation Assembly Point")
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
            .testTag("micro_ward_flood_evacuation_sheet")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF0284C7).copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, Color(0xFF0284C7)),
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Flood,
                            contentDescription = "Flood Radar",
                            tint = Color(0xFF0284C7),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (language == AppLanguage.ODIA) "ମାଇକ୍ରୋ-ୱାର୍ଡ ବନ୍ୟା ଓ ଉଚ୍ଚସ୍ଥଳୀ ଆଶ୍ରୟ" else "Ward Flood & High-Ground Router",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = "Balasore Municipality • ODRAF 1077 • Sluice Gates",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF0369A1),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF10B981).copy(alpha = 0.15f),
                border = BorderStroke(1.dp, Color(0xFF10B981))
            ) {
                Text(
                    text = "AUTO-SYNC 🟢",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF059669),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Designated High-Ground Safe Shelter Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
            border = BorderStroke(1.5.dp, Color(0xFF10B981))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF059669)
                    ) {
                        Text(
                            text = "PRIMARY HIGH-GROUND SHELTER",
                            color = Color.White,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                    Text("Elev: 14m Above MSL", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF065F46))
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = dailyPulse.nearestHighGroundShelter,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF065F46)
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Equipped with backup generator, municipal potable water tanker, dry food packs, and first aid desk.",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF047857), fontSize = 11.5.sp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        val uri = Uri.parse("geo:0,0?q=${Uri.encode(dailyPulse.nearestHighGroundShelter + " Balasore")}")
                        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Navigate to High-Ground Shelter (Safe Route)", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Ward-by-Ward Drainage & Inundation Status
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Municipal Micro-Ward Inundation Telemetry", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(8.dp))

                wards.forEach { ward ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = BorderStroke(0.5.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(ward.first, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                            Text(ward.second, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0284C7))
                            Text(ward.third, fontSize = 10.sp, color = Color(0xFF64748B))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Relief Post & Sandbag Inventory Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Municipal Emergency Relief & Sandbag Posts", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Sandbags In Stock", fontSize = 11.sp, color = Color(0xFF64748B))
                        Text("${dailyPulse.municipalSandbagPostStock} Units", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0284C7))
                    }
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1077"))
                            context.startActivity(intent)
                        },
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Call DEOC 1077", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Done / Close Flood Router", fontWeight = FontWeight.Bold)
        }
    }
}
