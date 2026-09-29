package com.example.ui.features.civic

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
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
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
fun WardSanitationVehicleTrackerSheet(
    dailyPulse: DailyBalasorePulse,
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val isOdia = language == AppLanguage.ODIA

    var refreshTimer by remember { mutableIntStateOf(30) }
    var selectedWardIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            if (refreshTimer > 1) {
                refreshTimer--
            } else {
                refreshTimer = 30
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

    val wards = listOf(
        WardSanitationStatus(
            wardNumber = 12,
            areaNameEn = "Motiganj & Phandi Chhak Sector",
            areaNameOd = "ମୋତିଗଞ୍ଜ ଓ ଫାଣ୍ଡି ଛକ ଅଞ୍ଚଳ",
            driverName = "Manoj Das",
            vehicleReg = "OD-01-AX-4821 (Tipper)",
            collectionWindow = "06:30 AM - 08:30 AM",
            statusEn = "In Transit • Ward Lane 4",
            statusOd = "ଗାଡ଼ି ଗଳି ନମ୍ବର ୪ ରେ ଚାଲୁଅଛି",
            etaMins = 12,
            isCompleted = false
        ),
        WardSanitationStatus(
            wardNumber = 5,
            areaNameEn = "Station Bazar & Cinema Chhak",
            areaNameOd = "ଷ୍ଟେସନ ବଜାର ଓ ସିନେମା ଛକ",
            driverName = "Pratap Jena",
            vehicleReg = "OD-01-BH-9102 (E-Cart)",
            collectionWindow = "06:00 AM - 08:00 AM",
            statusEn = "Completed 100% Today",
            statusOd = "ଆଜିର ସଂଗ୍ରହ ସମ୍ପୂର୍ଣ୍ଣ ହୋଇଛି",
            etaMins = 0,
            isCompleted = true
        ),
        WardSanitationStatus(
            wardNumber = 18,
            areaNameEn = "ITI Chhak & Mallikashpur Road",
            areaNameOd = "ଆଇଟିଆଇ ଛକ ଓ ମଲ୍ଲିକାଶପୁର",
            driverName = "Sukanta Behera",
            vehicleReg = "OD-01-CG-3140 (Tipper)",
            collectionWindow = "07:00 AM - 09:00 AM",
            statusEn = "Approaching Primary Road",
            statusOd = "ମୁଖ୍ୟ ରାସ୍ତା ଆଡ଼କୁ ଆସୁଅଛି",
            etaMins = 25,
            isCompleted = false
        ),
        WardSanitationStatus(
            wardNumber = 24,
            areaNameEn = "Remuna Golei & Sovarampur Colony",
            areaNameOd = "ରେମୁଣା ଗୋଲେଇ ଓ ଶୋଭାରାମପୁର",
            driverName = "Rabindra Nayak",
            vehicleReg = "OD-01-BH-7719 (Compactor)",
            collectionWindow = "07:30 AM - 09:30 AM",
            statusEn = "Scheduled Next Sector",
            statusOd = "ପରବର୍ତ୍ତୀ ସେକ୍ଟର ପାଇଁ ନିର୍ଦ୍ଧାରିତ",
            etaMins = 45,
            isCompleted = false
        )
    )

    val currentWard = wards[selectedWardIndex]

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
                    Text(text = "🚛", fontSize = 26.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isOdia) "ୱାର୍ଡ ପରିମଳ ଓ ଅଳିଆ ଗାଡ଼ି ଟ୍ରାକର୍" else "Ward Sanitation & Garbage Radar",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF15803D)
                        )
                    )
                }
                Text(
                    text = if (isOdia) "ବାଲେଶ୍ୱର ପୌରପାଳିକା ଘରକୁ ଘର ଅଳିଆ ସଂଗ୍ରହ ସ୍ଥିତି" else "Balasore Municipality Door-to-Door Waste Collection",
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
                        text = "LIVE (${refreshTimer}s)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF15803D)
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Card: Current Ward Status
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
                    Column {
                        Text(
                            text = if (isOdia) "ୱାର୍ଡ ${currentWard.wardNumber}: ${currentWard.areaNameOd}" else "Ward ${currentWard.wardNumber}: ${currentWard.areaNameEn}",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF14532D)
                            )
                        )
                        Text(
                            text = "Vehicle: ${currentWard.vehicleReg}",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
                        )
                    }

                    Surface(
                        color = if (currentWard.isCompleted) Color(0xFF16A34A) else Color(0xFF0284C7),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (currentWard.isCompleted) "COMPLETED" else "ETA ~${currentWard.etaMins}m",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
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
                    SanitationMetricBox(
                        label = if (isOdia) "ଗାଡ଼ି ସ୍ଥିତି" else "Route Status",
                        value = if (isOdia) currentWard.statusOd else currentWard.statusEn,
                        color = if (currentWard.isCompleted) Color(0xFF15803D) else Color(0xFF0284C7)
                    )
                    SanitationMetricBox(
                        label = if (isOdia) "ସଂଗ୍ରହ ସମୟ" else "Collection Hours",
                        value = currentWard.collectionWindow,
                        color = Color(0xFFB45309)
                    )
                    SanitationMetricBox(
                        label = if (isOdia) "ଚାଳକ" else "Staff / Driver",
                        value = currentWard.driverName,
                        color = Color(0xFF334155)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Ward Selector
        Text(
            text = if (isOdia) "ଆପଣଙ୍କ ୱାର୍ଡ ଚୟନ କରନ୍ତୁ" else "Select Your Municipality Ward",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))

        wards.forEachIndexed { idx, w ->
            val isSelected = selectedWardIndex == idx
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { selectedWardIndex = idx },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) Color(0xFFF0FDF4) else Color(0xFFFAFAFA)
                ),
                border = BorderStroke(
                    1.dp,
                    if (isSelected) Color(0xFF22C55E) else Color(0xFFE2E8F0)
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
                            .background(if (w.isCompleted) Color(0xFFDCFCE7) else Color(0xFFE0F2FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${w.wardNumber}",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (w.isCompleted) Color(0xFF15803D) else Color(0xFF0369A1)
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isOdia) "ୱାର୍ଡ ${w.wardNumber} - ${w.areaNameOd}" else "Ward ${w.wardNumber} - ${w.areaNameEn}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = if (w.isCompleted) "Completed Today" else "ETA ~${w.etaMins} mins • ${w.collectionWindow}",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Wet vs Dry Segregation Guide
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEFCE8)),
            border = BorderStroke(1.dp, Color(0xFFFEF08A))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "♻️", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isOdia) "ଅଳିଆ ପୃଥକୀକରଣ ନିୟମ (ଓଦା ଓ ଶୁଖିଲା)" else "Waste Segregation Guidelines",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF854D0E)
                        )
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isOdia)
                        "• ସବୁଜ ଡଷ୍ଟବିନ୍ (ଓଦା ଅଳିଆ): ପନିପରିବା ଚୋପା, ଖାଦ୍ୟ ଅବଶିଷ୍ଟ, ବଗିଚା ପତ୍ର।\n• ନୀଳ ଡଷ୍ଟବିନ୍ (ଶୁଖିଲା ଅଳିଆ): କାଗଜ, ପ୍ଲାଷ୍ଟିକ୍, କାଚ, ଟିଣ ଡବା।"
                    else
                        "• Green Bin (Wet Waste): Kitchen leftovers, vegetable peels, garden waste.\n• Blue Bin (Dry Waste): Clean plastics, newspapers, cardboard, glass bottles.",
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
                    val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:06782262035"))
                    context.startActivity(callIntent)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF15803D)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = if (isOdia) "ପୌରପାଳିକା ଡେସ୍କ" else "Municipality")
            }

            OutlinedButton(
                onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "Balasore Municipality Ward Sanitation Radar: Door-to-door waste collection vehicle is active in Ward ${currentWard.wardNumber}. Balasore 360 App."
                        )
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Sanitation Alert"))
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
fun SanitationMetricBox(label: String, value: String, color: Color) {
    Column {
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

private data class WardSanitationStatus(
    val wardNumber: Int,
    val areaNameEn: String,
    val areaNameOd: String,
    val driverName: String,
    val vehicleReg: String,
    val collectionWindow: String,
    val statusEn: String,
    val statusOd: String,
    val etaMins: Int,
    val isCompleted: Boolean
)
