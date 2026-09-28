package com.example.ui.features.heritage

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
 * Baleswari GI-Tag Culinary Trail & Fresh Artisan Batch Tracker.
 * Visualizes hot artisan batch timers for Chhena Gaja, Remuna Amruta Keli Khira Bhog,
 * and Mudhi-Mansa with interactive artisan shop locators.
 */
@Composable
fun BaleswariGiCulinaryTrailSheet(
    dailyPulse: DailyBalasorePulse,
    language: AppLanguage = AppLanguage.ENGLISH,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val specialties = listOf(
        Triple(
            "Baleswari Chhena Gaja (ଛେନା ଗଜା)",
            "Deep-fried fresh cottage cheese squares crystallized in light cardamom syrup. Distinct from Pahala rasagola with golden crust.",
            "Next Batch: ${dailyPulse.chhenaGajaNextHotBatchTime} • Nandi Sweets & Town Market"
        ),
        Triple(
            "Remuna Amruta Keli Khira Bhog (କ୍ଷୀର ଭୋଗ)",
            "10th-century divine condensed milk delicacy cooked slowly in clay pots at Khirachora Gopinath Temple.",
            "Clay Pots Left: ${dailyPulse.remunaKhiraBhogPotsLeft} Pots • Distribution Active"
        ),
        Triple(
            "Baripada / Balasore Mudhi-Mansa (ମୁଢ଼ି ମାଂସ)",
            "Signature regional pairing of roasted puffed rice tossed with tender mutton gravy, onion, green chilies, and coriander.",
            "Stalls Open: ${dailyPulse.mudhiMansaAuthenticStallsOpen} Authentic Counters • Station Road"
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
            .testTag("baleswari_gi_culinary_trail_sheet")
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
                    color = Color(0xFFF59E0B).copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, Color(0xFFF59E0B)),
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🍯", fontSize = 20.sp)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱରୀ ଖାଦ୍ୟ ଐତିହ୍ୟ ଓ ତାଜା ବ୍ୟାଚ୍" else "Baleswari Culinary Trail & Batches",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = "Chhena Gaja • Remuna Khira Bhog • Mudhi Mansa",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFD97706),
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

        // Fresh Batch Notification Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
            border = BorderStroke(1.5.dp, Color(0xFFF59E0B))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFD97706)
                    ) {
                        Text(
                            text = "LIVE BATCH ALERTS",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                    Text("Today's Artisan Kitchens", fontSize = 11.sp, color = Color(0xFF92400E))
                }

                Spacer(modifier = Modifier.height(10.dp))

                specialties.forEach { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(0.5.dp, Color(0xFFFDE68A))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(item.first, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF78350F))
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(item.second, fontSize = 11.sp, color = Color(0xFF451A03), lineHeight = 16.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFFEF3C7)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.AccessTime, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(item.third, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Heritage Map & Cooperative Locator
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Verified Culinary Outlets in Balasore", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(8.dp))

                listOf(
                    Pair("Nandi Sweets (Chhena Gaja Pioneer)", "Cinema Square, OT Road, Balasore • Estd. 1954"),
                    Pair("Remuna Khirachora Temple Trust Counter", "Temple Inner Prakara, Remuna • Authentic Clay Pots"),
                    Pair("Hotel Milan & Baripada Mudhi Corner", "Station Road, Near SER Junction • Fresh Mutton Gravy")
                ).forEach { spot ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(spot.first, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(spot.second, fontSize = 10.5.sp, color = Color(0xFF64748B))
                        }
                        IconButton(
                            onClick = {
                                val uri = Uri.parse("geo:0,0?q=${Uri.encode(spot.first + " Balasore")}")
                                context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = "View on Map", tint = Color(0xFF0284C7), modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Done / Close Culinary Passport", fontWeight = FontWeight.Bold)
        }
    }
}
