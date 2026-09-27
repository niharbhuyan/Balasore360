package com.example.ui.features.health

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.ui.theme.*

/**
 * District Health & Live Blood Donor Grid connecting Fakir Mohan Medical College & Hospital (FM MCH),
 * District Headquarters Hospital (DHH) Blood Bank, Nilagiri Sub-Divisional Hospital, and Red Cross Balasore.
 * Features live inventory for rare negative blood groups, donor SOS callout, and 24x7 blood bank hotlines.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalasoreDistrictBloodDonorGridSheet(
    dailyPulse: DailyBalasorePulse,
    language: AppLanguage = AppLanguage.ENGLISH,
    onClose: () -> Unit = {}
) {
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .testTag("balasore_district_blood_donor_grid_sheet")
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
                            .background(Color(0xFFFEE2E2)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🩸", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଜିଲ୍ଲା ରକ୍ତଭଣ୍ଡାର ଓ ଦାତା ଗ୍ରିଡ୍" else "Balasore Blood Donor Grid",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଏଫ୍ଏମ୍ ଏମସିଏଚ୍ ଓ ଡିଏଚ୍ଏଚ୍ ରକ୍ତ ମହଜୁଦ ତଥ୍ୟ" else "FM MCH & DHH Live Blood Bank Telemetry",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = BentoSlate500,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.testTag("btn_close_blood_grid_sheet")
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Inventory Summary Banner
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF991B1B)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFCA5A5))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "LIVE BLOOD BANK RESERVES",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFECDD3),
                                    letterSpacing = 1.sp
                                )
                            )
                        }

                        Text(
                            text = "${dailyPulse.dhhBloodUnitsStoredTotal} Units Total",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "O+ Stored", fontSize = 11.sp, color = Color(0xFFFCA5A5))
                            Text(text = "${dailyPulse.fmmchBloodInventoryOposUnits} units", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Column {
                            Text(text = "Negative Reserves", fontSize = 11.sp, color = Color(0xFFFCA5A5))
                            Text(text = "${dailyPulse.fmmchNegativeUnitsReserve} units", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Column {
                            Text(text = "Shortage Group", fontSize = 11.sp, color = Color(0xFFFCA5A5))
                            Text(text = dailyPulse.fmmchCriticalBloodShortageGroup, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFED7AA))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth().weight(1f, fill = false)
            ) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F2)),
                        border = BorderStroke(1.dp, Color(0xFFFECDD3))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🚨", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଜରୁରୀକାଳୀନ ରକ୍ତ ଆବଶ୍ୟକତା ହଟଲାଇନ୍" else "24x7 Emergency Blood Bank Hotlines",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = BentoSlate900)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "• DHH Balasore Blood Bank: 06782-262057\n• FM MCH Remuna Campus Blood Bank: 06782-240108\n• Balasore Red Cross Society: 06782-262142\n• State Blood Helpline: 104",
                                style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate700, fontSize = 12.sp, lineHeight = 18.sp)
                            )
                        }
                    }
                }

                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                        border = BorderStroke(1.dp, Color(0xFFBBF7D0))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = if (language == AppLanguage.ODIA) "ସ୍ୱେଚ୍ଛାକୃତ ରକ୍ତଦାତା ତାଲିକାଭୁକ୍ତ କାର୍ଯ୍ୟକ୍ରମ" else "Voluntary Donor Registration & Citizen Grid",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Registered active blood donors across Balasore: ${dailyPulse.verifiedActiveDonorsCount} citizens. Volunteers receive emergency donor alerts only when hospital reserves dip below threshold.",
                                style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate600, fontSize = 12.sp)
                            )
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:06782262057"))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.weight(1f).testTag("btn_call_dhh_blood_bank"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "DHH Blood Bank", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:104"))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.weight(1f).testTag("btn_call_health_104"),
                            colors = ButtonDefaults.buttonColors(containerColor = OceanBlue),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Emergency, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Helpline 104", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(18.dp)) }
            }
        }
    }
}
